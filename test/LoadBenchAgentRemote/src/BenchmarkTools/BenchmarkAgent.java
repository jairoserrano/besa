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
import WorkAgent.WorkAgentPingGuard;
import WorkAgent.WorkAgentReadyGuard;
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
        startTime = System.currentTimeMillis();
        /**
         * Generando lista de tareas
         */
        ReportBESA.info("Generación de tareas iniciada");
        BenchmarkAgentState AgentState = (BenchmarkAgentState) this.getState();

        for (int i = 0; i < config.getSmallLoads(); i++) {
            AgentState.addTask("Small");
        }
        for (int i = 0; i < config.getMediumLoads(); i++) {
            AgentState.addTask("Medium");
        }
        for (int i = 0; i < config.getHighLoads(); i++) {
            AgentState.addTask("High");
        }

    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {
        ReportBESA.info("Tiempo de ejecución total: " + (System.currentTimeMillis() - this.startTime) / 1000000);
        System.exit(0);
    }

    public void checkReady() {

        boolean ready = false;
        AgHandlerBESA ah;

        while (!ready) {
            try {
                Thread.sleep(1000);
                //ReportBESA.debug("Checkeando agentes");
                for (int i = 1; i <= config.getNumberOfAgents(); i++) {
                    //ReportBESA.debug("Buscando agente WorkAgent_" + String.valueOf(i));
                    ah = this.getAdmLocal().getHandlerByAlias("WorkAgent_" + String.valueOf(i));
                    EventBESA msj = new EventBESA(
                            BenchmarkAgentPingMessage.class.getName(),
                            null
                    );
                    ah.sendEvent(msj);
                }
                ready = true;
            } catch (ExceptionBESA ex) {
                ReportBESA.error("Checkeo fallido");
            } catch (InterruptedException ex) {
                ReportBESA.error(ex);
            }
        }

        ReportBESA.debug("Checkeo exitoso");
    }
    
    public void activateAgents() {

        AgHandlerBESA ah;

        try {
            //ReportBESA.debug("Checkeando agentes");
            for (int i = 1; i <= config.getNumberOfAgents(); i++) {
                //ReportBESA.debug("Activando WorkAgent_" + String.valueOf(i));
                ah = this.getAdmLocal().getHandlerByAlias("BenchmarkAgent");
                EventBESA msj = new EventBESA(
                        BenchmarkAgentReadyGuard.class.getName(),
                        new BenchmarkAgentReceiverMessage("WorkAgent_" + String.valueOf(i))
                );
                ah.sendEvent(msj);
            }
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
    
    

}
