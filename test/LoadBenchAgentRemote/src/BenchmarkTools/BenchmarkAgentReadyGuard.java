/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentReadyGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {

        /**
         * Llega el agente y lo guarda en el listado de los preparados.
         */
        BenchmarkAgentReceiverMessage mensaje = (BenchmarkAgentReceiverMessage) event.getData();
        BenchmarkAgentState AgentState = (BenchmarkAgentState) this.agent.getState();
        AgentState.setAgentsReady(mensaje.getContent());
        ReportBESA.debug("Agente listo: " + mensaje.getContent());

    }

}
