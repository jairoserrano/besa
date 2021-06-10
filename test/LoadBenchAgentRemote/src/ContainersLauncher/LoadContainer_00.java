/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

import BenchmarkTools.BenchmarkAgentReceiverGuard;
import BenchmarkTools.BenchmarkAgentState;
import BenchmarkTools.BenchmarkAgent;
import BenchmarkTools.BenchmarkAgentSenderGuard;
import BenchmarkTools.BenchmarkAgentReadyGuard;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import BenchmarkTools.BenchmarkAgentPingGuard;
import BenchmarkTools.BenchmarkAgentReceiverMessage;

/**
 *
 * @author jairo
 */
public class LoadContainer_00 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /**
         * Crea el administrador e instancia la config.
         */
        ReportBESA.debug("Lanzando config/Container_00.xml");
        AdmBESA adminBesa = AdmBESA.getInstance("config/Container_00.xml");
        BenchmarkConfig config = BenchmarkConfig.getConfig(args);

        try {
            /**
             * Crea el agente principal Benckmark y setea las guardas
             */
            BenchmarkAgentState BenchmarkState = new BenchmarkAgentState();
            StructBESA StructSender = new StructBESA();
            StructSender.bindGuard(BenchmarkAgentSenderGuard.class);            
            StructSender.bindGuard(BenchmarkAgentReceiverGuard.class);
            StructSender.bindGuard(BenchmarkAgentPingGuard.class);
            StructSender.bindGuard(BenchmarkAgentReadyGuard.class);
            BenchmarkAgent AgentSender = new BenchmarkAgent("BenchmarkAgent",
                    BenchmarkState,
                    StructSender,
                    0.91
            );
            AgentSender.start();
            adminBesa.registerAgent(AgentSender,
                    "BenchmarkAgent",
                    "BenchmarkAgent"
            );
            /**
             * Si la cooperación está activa, se chequean todos los agentes
             */            
            if (config.IsCooperationOn()) {
                AgentSender.checkReady();
                AgentSender.activateAgents();
            }

            /**
             * Envia el primer mensaje para desencadenar la simulación
             */
            try {
                EventBESA msj = new EventBESA(
                        BenchmarkAgentSenderGuard.class.getName(),
                        new BenchmarkAgentReceiverMessage("ready")
                );
                ReportBESA.info("Mensaje a BenchmarkAgentSenderGuard");
                AgentSender.sendEvent(msj);
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }

            /*
            if (config.IsBalancerOn()) {
                BalancerBESA balanceador = new BalancerBESA(10000, 1000000000L);
                balanceador.initBalancer();
            }
             */

            /*
            if (config.IsBackupOn()) {
                adminBesa.activateCheckpoint();
                adminBesa.getConfigBESA().setCheckpointTime(config.getBackupTime() * 1000);
            }
             */
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
