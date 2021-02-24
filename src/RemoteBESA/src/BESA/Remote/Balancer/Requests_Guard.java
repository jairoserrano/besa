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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bitas
 */
public class Requests_Guard extends GuardBESA {

     /**
     * Recibe las solicitudes y determina si es viable hacer el traspaso
     */
    @Override
    public void funcExecGuard(EventBESA event) {

        AdmBESA adm = this.agent.getAdmLocal();
        Ag_Balancer_State estado = (Ag_Balancer_State) this.agent.getState();
        long max = estado.getMax();
        Data_Agent dataAgent = (Data_Agent) event.getData();
        AgHandlerBESA ah = null;
        Data_Request respuesta;
        
        /*Si la carga del contenedor incluyendo el agente que llega no supera 
        * el maximo se puede hacer el balanceo de carga con este contenedor
        */
        if (estado.getLoad() + dataAgent.getCarga() < max) {
            //Se crea la respuesta aceptada para enviar al contenedor
            respuesta = new Data_Request(this.agent.getAdmLocal().getAdmHandler().getAlias(),
                    dataAgent.getAlias(), dataAgent.getPasswd());
            respuesta.setAgentData(dataAgent);
            respuesta.setAceptado(true);
        } else {
            //De lo contrario se crea por defecto que viene rechazada
            respuesta = new Data_Request();
            respuesta.setAgentData(dataAgent);
        }
        EventBESA ev = new EventBESA(Answers_Guard.class.getName(), respuesta);
        try {
            ah = this.agent.getAdmLocal().getHandlerByAlias(dataAgent.getContenedor());
            ah.sendEvent(ev);
        } catch (ExceptionBESA ex) {
            Logger.getLogger(Requests_Guard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
