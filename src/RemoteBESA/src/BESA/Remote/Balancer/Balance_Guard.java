/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Remote.Balancer;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bitas
 */
public class Balance_Guard extends PeriodicGuardBESA {

    //Guarda la carga del agente con mas carga a la hora de balancear
    private long cargaMaxima;
    
    //Guarda el alias del agente con mas carga
    private String agenteSeleccionado;

     /**
     * Calcula periodicamente la carga del contenedor y mira si paso el maximo
     */
    @Override
    public void funcPeriodicExecGuard(EventBESA event) {

        Ag_Balancer_State estado = (Ag_Balancer_State) this.agent.getState();
        estado.setContenedores(new ArrayList<String>());
        long maxload = estado.getMax();
        long carga = 0;
        
        //Se obtiene el ThreadMXBean para saber el uso de CPU
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        //Se suma el uso de CPU de todos los hilos para saber la carga total
        for (Long threadID : threadMXBean.getAllThreadIds()) {
            carga = carga + threadMXBean.getThreadCpuTime(threadID);
        }
        
        /*Se actualiza la carga del contenedor restando el nuevo valor con
        *el anterior para tener la carga en el periodo de tiempo establecido
        */
        estado.setLoad(carga - estado.getLoad());

        /*Se mira si la carga actual supera la carga maxima que deberia 
        *tener el contenedor
        */
        if (estado.getLoad() > maxload) {
            //Se suspende el llamado periodico mientras el balanceo
            this.suspendPeriodicCall();
            iniciarBalanceo();
            //Una vez terminado se reanuda la guarda periodica
            this.startPeriodicCall();
        }
    }

     /**
     * Inicia el balanceo de carga calculando las cargas y buscando el contenedor
     */
    public void iniciarBalanceo() {

        this.cargaMaxima = 0;
        this.agenteSeleccionado = "";

        /*Se calculan las cargas dos veces con un delay de 10 segundos para
        * tener un valor reciente de la carga del agente
        */
        calcularCargas();
        delay(10);
        calcularCargas();

        //Si se encontro un agente se empiezan a buscar los contenedores
        if (!agenteSeleccionado.equalsIgnoreCase("")) {
            buscarContenedores();
        }

    }

     /**
     * Calcula la carga de cada uno de los agentes
     */
    public void calcularCargas() {

        Ag_Balancer_State estado = (Ag_Balancer_State) this.agent.getState();
        Map<String, Long> cargas = new HashMap<>();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        Long carga;
        /*Para cada uno delos agentes en el mapa se van a ver los hilos asociados
        * para mirar la carga
        */
        for (Map.Entry<String, ArrayList<Long>> entry : estado.getThreadsIDs().entrySet()) {
            carga = new Long(0);
            for (Long l : entry.getValue()) {
                carga = carga + threadMXBean.getThreadCpuTime(l);
            }
            //Si el agente aun no estaba se agrega
            if (cargas.containsKey(entry.getKey())) {
                carga = carga - cargas.get(entry.getKey());
                cargas.replace(entry.getKey(), carga);
            } else {
                cargas.put(entry.getKey(), carga);
            }
            // Se mira si es el agente con mayor carga para asignarlo
            if (carga > cargaMaxima) {
                cargaMaxima = carga;
                agenteSeleccionado = entry.getKey();
            }

        }
        estado.setAgentsLoads(cargas);

    }

    private static void delay(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException ex) {
            ReportBESA.error(ex);
        }
    }

     /**
     * Busca los contenedores a los cuales se le enviara la solicitud
     */
    public void buscarContenedores() {

        AdmBESA adm = this.agent.getAdmLocal();
        //Se obtiene la lista de contenedores
        Enumeration<String> enumContainers = adm.getAdmAliasList();
        AgHandlerBESA ah = null;
        try {
            ah = adm.getHandlerByAlias(agenteSeleccionado);
        } catch (ExceptionBESA ex) {
            Logger.getLogger(Balance_Guard.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Se crea la data que se va a enviar en el evento con los datos del agente
        Data_Agent dataAgent = new Data_Agent(agenteSeleccionado, cargaMaxima,
                this.agent.getAlias(), ah.getAg().getPassword());
        Ag_Balancer_State estado = (Ag_Balancer_State) this.agent.getState();

        // @TODO: calcular contenedor prometedor
        
        //Se obtienen los contenedores y se agregan a la lista
        List<String> listContainers = new ArrayList<>();
        while (enumContainers.hasMoreElements()) {
            String cont = enumContainers.nextElement();
            if (!cont.equalsIgnoreCase(adm.getIdAdm())) {
                listContainers.add(cont);
            }
        }
        //Se mezcla la lista para no siempre enviar los eventos al mismo contenedor
        Collections.shuffle(listContainers);
        estado.setContenedores(listContainers);
        enviarSolicitud(dataAgent);
    }

     /**
     * Envia los eventos de balanceo
     */
    public void enviarSolicitud(Data_Agent dataAgent) {

        Ag_Balancer_State estado = (Ag_Balancer_State) this.agent.getState();
        estado.getContenedores();
        AgHandlerBESA ah = null;

        try {
            // Seleccionamos el primer contenedor de la lista de contenedores 
            // prometedores para enviarle la propuesta
            ah = this.agent.getAdmLocal().getHandlerByAlias("balanceador"
                    + estado.getContenedores().get(0));
            if (ah != null) {
                EventBESA ev = new EventBESA(Requests_Guard.class.getName(), dataAgent);
                try {
                    //Envia la propuesta
                    ah.sendEvent(ev);
                } catch (ExceptionBESA ex) {
                    Logger.getLogger(Balance_Guard.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //Se remueve el contendor de los prometedores si no tiene balanceador de carga o no existe
                estado.getContenedores().remove(0);
                //Si ya no quedan contenedores no se realiza el balanceo
                if (estado.getContenedores().isEmpty()) {
                    System.out.println("No se puede realizar el balanceo de carga");
                } else {
                    enviarSolicitud(dataAgent);
                }
            }
        } catch (ExceptionBESA ex) {
            System.out.println("El contenedor "
                    + estado.getContenedores().get(0) + " no tiene un balanceador de carga");
        }
    }

}
