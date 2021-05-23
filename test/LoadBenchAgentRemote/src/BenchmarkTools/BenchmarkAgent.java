/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import ContainersLauncher.BenchmarkConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class BenchmarkAgent extends AgentBESA {

    BenchmarkConfig config = BenchmarkConfig.getConfig();
    long startTime;

    public BenchmarkAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
         startTime = System.nanoTime();
    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {
        ReportBESA.info("Tiempo de ejecución total: " + (System.nanoTime() - this.startTime)/1000000000);
        System.exit(0);
    }

    public void checkReady() {

        boolean ready = false;

        AgHandlerBESA ah;

        while (!ready) {
            ReportBESA.info("Checkeando agentes");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BenchmarkAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                for (int i = 1; i <= config.getNumberOfContainers(); i++) {
                    for (int j = 0; j < config.getNumberOfAgentsPerContainer(); j++) {
                        ah = this.getAdmLocal().getHandlerByAlias("WorkAgent_" + String.valueOf(i) + "_" + String.valueOf(j));
                        EventBESA msj = new EventBESA(
                                BenchmarkAgentPingMessage.class.getName(),
                                null
                        );
                        ah.sendEvent(msj);
                    }
                }
                ready = true;
            } catch (ExceptionBESA ex) {
                ReportBESA.info("Checkeo fallido");
            }
        }
        ReportBESA.info("Checkeo exitoso");
    }

}
