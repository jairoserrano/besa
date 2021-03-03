/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BenchmarkAgents.*;
import FibonacciAgent.FibonacciAgentGuard;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class FibonacciContainer_00 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ReportBESA.info("Lanzando config/Container_00.xml");
        AdmBESA adminBesa = AdmBESA.getInstance("config/Container_00.xml");

        try {

            BenchmarkAgentSenderState estadoSender = new BenchmarkAgentSenderState();
            StructBESA StructSender = new StructBESA();
            StructSender.bindGuard(BenchmarkAgentSenderGuard.class);
            BenchmarkAgentSender AgentSender = new BenchmarkAgentSender("AgentSender", estadoSender, StructSender, 0.91);
            AgentSender.start();
            adminBesa.registerAgent(AgentSender, "AgentSender", "AgentSender");

            BenchmarkAgentReceiverState estadoReceiver = new BenchmarkAgentReceiverState();
            StructBESA StructReceiver = new StructBESA();
            StructReceiver.bindGuard(BenchmarkAgentReceiverGuard.class);
            BenchmarkAgentReceiver AgentReceiver = new BenchmarkAgentReceiver("AgentReceiver", estadoReceiver, StructReceiver, 0.91);
            AgentReceiver.start();
            adminBesa.registerAgent(AgentReceiver, "AgentReceiver", "AgentReceiver");

            AgHandlerBESA ah;
            try {
                ah = adminBesa.getHandlerByAlias("AgentSender");
                EventBESA msj = new EventBESA(
                        BenchmarkAgentSenderGuard.class.getName(),
                        new BenchmarkAgentSenderMessage(5,10)
                );
                ah.sendEvent(msj);
            } catch (ExceptionBESA ex) {
                Logger.getLogger(FibonacciAgentGuard.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

        /*
        while (true) {
            ReportBESA.info("==================================");
            ReportBESA.info("Containers:");
            Enumeration<String> containers = adminBesa.getInstance().getAdmAliasList();
            while (containers.hasMoreElements()) {
                ReportBESA.info(containers.nextElement());
            }
            ReportBESA.info("==================================");
            ReportBESA.info("Agents:");
            Enumeration<String> agents = adminBesa.getIdList();
            while (agents.hasMoreElements()) {
                ReportBESA.info(agents.nextElement());
            }
            ReportBESA.info("==================================");
            Thread.sleep(2000);
        }
         */
    }

}
