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
    public void funcExecGuard(EventBESA event) {
        BenchmarkAgentReceiverMessage mensaje = (BenchmarkAgentReceiverMessage) event.getData();
        ReportBESA.info(mensaje.getContent());

        BenchmarkAgentState AgentState = (BenchmarkAgentState) getAgent().getState();
        AgentState.decrementCounter();
        ReportBESA.debug("Contador va: " + AgentState.getCounter());
        if (AgentState.getCounter() == 0) {

            for (int i = 1; i <= config.getNumberOfContainers(); i++) {
                for (int j = 0; j < config.getNumberOfAgentsPerContainer(); j++) {
                    try {
                        this.agent.getAdmLocal().killAgent("WorkAgent_" + String.valueOf(i) + "_" + String.valueOf(j), 0.91);
                    } catch (ExceptionBESA ex) {
                        Logger.getLogger(BenchmarkAgentReceiverGuard.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            try {
                this.agent.getAdmLocal().killAgent("BenchmarkAgent", 0.91);
            } catch (ExceptionBESA ex) {
                Logger.getLogger(BenchmarkAgentReceiverGuard.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.exit(0);
        }
    }

}
