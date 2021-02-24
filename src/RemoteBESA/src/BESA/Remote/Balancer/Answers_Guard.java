/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Remote.Balancer;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author bitas
 */
public class Answers_Guard extends GuardBESA {

     /**
     * Espera la respuesta de la solicitud de balanceo para transferir el agente
     * o enviar la solicitud al siguiente contenedor
     */
    @Override
    public void funcExecGuard(EventBESA event) {

        AdmBESA adm = this.agent.getAdmLocal();
        Ag_Balancer_State estado = (Ag_Balancer_State) this.agent.getState();
        Data_Request respuesta = (Data_Request) event.getData();
        AgHandlerBESA ah = null;

        /*Si la respuesta del otro contenedor es afirmativa se procede a hacer 
        *el movimiento del agente.
        */ 
        if (respuesta.isAceptado()) {
            try {
                ReportBESA.info("========================");
                ReportBESA.info("= The agent " + respuesta.getAgente()
                        + " is moving to " + respuesta.getContenedor());
                ReportBESA.info("========================");
                //Se mueve el agente
                adm.moveAgent(respuesta.getAgente(),
                        respuesta.getContenedor(), respuesta.getClave());
                ReportBESA.info("==================================");
                ReportBESA.info("= The agent moved to "
                        + respuesta.getContenedor() + " . =");
                ReportBESA.info("==================================");
            } catch (ExceptionBESA ex) {
                System.out.println("Hubo error");
                ReportBESA.error(ex);
            }
            //Se remueve el agente de la lista de agentes del contenedor
            estado.getThreadsIDs().remove(respuesta.getAgente());
            estado.getAgentsLoads().remove(respuesta.getAgente());
            //Se crea el evento para actualizar la informacion del contenedor que recibio el agente
            EventBESA ev = new EventBESA(Update_Guard.class.getName(),
                    respuesta.getAgentData());
            try {
                ah = adm.getHandlerByAlias("balanceador" + respuesta.getContenedor());
                ah.sendEvent(ev);
            } catch (ExceptionBESA e) {
                e.printStackTrace();
            }
            /*Si la respuestas no es afirmativa se elimina ese contenedor de la lista de prometedores
            * y se envia la solicitud al siguiente en la lista
            */
        } else {
            if (!estado.getContenedores().isEmpty()) {
                estado.getContenedores().remove(0);
            }
            enviarSolicitud(respuesta);
        }
    }

    public void enviarSolicitud(Data_Request respuesta) {
        AgHandlerBESA ah = null;
        Ag_Balancer_State estado = (Ag_Balancer_State) this.agent.getState();
        try {
            /*Si la lista de contenedores no esta vacia se envia la solicitud
            * al siguente en la lista
            */
            if (!estado.getContenedores().isEmpty()) {
                ah = this.agent.getAdmLocal().getHandlerByAlias("balanceador"
                        + estado.getContenedores().get(0));
                if (ah != null) {
                    Data_Agent dataAgent = respuesta.getAgentData();
                    EventBESA ev = new EventBESA(Requests_Guard.class.getName(), dataAgent);
                    ah.sendEvent(ev);
                }
                //Si la lista esta vacia significa que no se pudo balancear
            } else {
                System.out.println("No se puede realizar el balanceo de carga");
            }
        } catch (ExceptionBESA ex) {
            /*Si se llega a la exepcion significa que el contenedor no
            * tiene balanceador de carga
            */
            System.out.println("El contenedor " + estado.getContenedores().get(0)
                    + " no tiene un balanceador de carga");
            estado.getContenedores().remove(0);
            if (estado.getContenedores().isEmpty()) {
                System.out.println("No se puede realizar el balanceo de carga");
            } else {
                enviarSolicitud(respuesta);
            }
        }
    }

}
