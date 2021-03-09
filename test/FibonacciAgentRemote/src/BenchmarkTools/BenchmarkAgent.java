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
import FibonacciAgent.FibonacciAgentGuard;
import FibonacciAgent.FibonacciAgentMessage;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class BenchmarkAgent extends AgentBESA {

    public BenchmarkAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {
    }

    public void checkReady() {

        boolean ready = false;
        BenchmarkConfig config = new BenchmarkConfig();
        AgHandlerBESA ah;

        while (!ready) {
            ReportBESA.info("Checkeando agentes");
            try {
                for (int i = 1; i <= config.getNumberOfContainers(); i++) {
                    for (int j = 0; j < config.getNumberOfAgentsPerContainer(); j++) {
                        ah = this.getAdmLocal().getHandlerByAlias("FiboAgente_" + String.valueOf(i) + "_" + String.valueOf(j));
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
