/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import ContainersLauncher.BenchmarkConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentReceiverGuard extends GuardBESA {

    BenchmarkConfig config = BenchmarkConfig.getConfig();

    @Override
    public synchronized void funcExecGuard(EventBESA event) {
        BenchmarkAgentReceiverMessage mensaje = (BenchmarkAgentReceiverMessage) event.getData();
        ReportBESA.info("Cálculo recibido de " + mensaje.getContent());

        /**
         * Captura el estado y actualiza
         */
        BenchmarkAgentState AgentState = (BenchmarkAgentState) this.agent.getState();
        AgentState.decrementCounter();
        ReportBESA.debug("Contador va: " + AgentState.getCounter());
        /**
         * Si el contador de tareas llega a cero, cierra todos los agentes.
         */
        if (AgentState.getCounter() == 0) {

            for (int i = 1; i <= config.getNumberOfAgents(); i++) {
                try {
                    this.agent.getAdmLocal().killAgent("WorkAgent_" + String.valueOf(i), 0.91);
                } catch (ExceptionBESA ex) {
                    Logger.getLogger(BenchmarkAgentReceiverGuard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                this.agent.getAdmLocal().killAgent("BenchmarkAgent", 0.91);
            } catch (ExceptionBESA ex) {
                Logger.getLogger(BenchmarkAgentReceiverGuard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
