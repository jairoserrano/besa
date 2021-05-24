/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

import BenchmarkTools.BenchmarkAgentReceiverGuard;
import BenchmarkTools.BenchmarkAgentMessage;
import BenchmarkTools.BenchmarkAgentState;
import BenchmarkTools.BenchmarkAgent;
import BenchmarkTools.BenchmarkAgentSenderGuard;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import WorkAgent.WorkAgentGuard;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class LoadContainer_00 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ReportBESA.debug("Lanzando config/Container_00.xml");
        AdmBESA adminBesa = AdmBESA.getInstance("config/Container_00.xml");
        BenchmarkConfig config = BenchmarkConfig.getConfig(args);
        
        try {

            BenchmarkAgentState BenchmarkState = new BenchmarkAgentState();
            StructBESA StructSender = new StructBESA();
            StructSender.bindGuard(BenchmarkAgentSenderGuard.class);
            StructSender.bindGuard(BenchmarkAgentReceiverGuard.class);
            BenchmarkAgent AgentSender = new BenchmarkAgent("BenchmarkAgent", BenchmarkState, StructSender, 0.91);
            AgentSender.start();
            adminBesa.registerAgent(AgentSender, "BenchmarkAgent", "BenchmarkAgent");

            AgentSender.checkReady();            
            
            AgHandlerBESA ah;
            try {
                ah = adminBesa.getHandlerByAlias("BenchmarkAgent");
                EventBESA msj = new EventBESA(
                        BenchmarkAgentSenderGuard.class.getName(),
                        new BenchmarkAgentMessage(
                                config.getNumberOfContainers(),
                                config.getNumberOfAgentsPerContainer()
                        )
                );
                ah.sendEvent(msj);
            } catch (ExceptionBESA ex) {
                Logger.getLogger(WorkAgentGuard.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
