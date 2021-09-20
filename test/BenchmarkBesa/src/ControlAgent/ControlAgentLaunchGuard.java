/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlAgent;

import ClientAgent.*;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import ServerAgent.WorkerAgent;
import Utils.BenchmarkConfig;
import Utils.BenchmarkExperimentUnit;
import Utils.ExperimentUnitMessage;
import java.util.ArrayList;

/**
 *
 * @author jairo
 */
public class ControlAgentLaunchGuard extends GuardBESA {

    private ArrayList<WorkerAgent> Agents;

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        BenchmarkConfig configExp = BenchmarkConfig.getConfig();
        ExperimentUnitMessage Message = (ExperimentUnitMessage) event.getData();
        BenchmarkExperimentUnit ceUnit = Message.getExperiment();

        if (ceUnit == null) {

            // Kills last agent
            ReportBESA.info("Agente ceUnit " + Message.getAgentRef() + " regresa NULL.");
            killUnusedAgent(Message.getAgentRef());
            // Kills containers
            /*try {
                this.agent.getAdmLocal().kill(0.91);
                /*
                Enumeration<String> containers = AdmBESA.getInstance().getAdmAliasList();
                while (containers.hasMoreElements()) {
                    ReportBESA.info(containers.nextElement());
                }
             */
            /*} catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
                System.exit(1);
            }*/
            // Close program
            //System.exit(1);
        } else {

            ReportBESA.info("Started Experiment: " + ceUnit.getExperimentID());

            // ClientAg Agent created
            try {
                ClientAgentState BenchmarkState = new ClientAgentState();
                StructBESA StructSender = new StructBESA();
                StructSender.bindGuard(ClientAgentServerReadyGuard.class);
                StructSender.bindGuard(ClientAgentLaunchExperimentUnitGuard.class);
                StructSender.bindGuard(ClientAgentReportGuard.class);
                ClientAgent ClientAg = new ClientAgent(
                        getClientAgentName(
                                ceUnit.getExperimentID()
                        ),
                        BenchmarkState,
                        StructSender,
                        0.91
                );
                ClientAg.start();
                ReportBESA.info(
                        "\n\n*******************************\n"
                        + getClientAgentName(ceUnit.getExperimentID()) + " created\n"
                        + "*******************************\n"
                );
            } catch (KernelAgentExceptionBESA ex) {
                ReportBESA.error(ex);
            }

            // Update Agent Status
            try {
                AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAlias(
                        getClientAgentName(
                                ceUnit.getExperimentID()
                        )
                );
                ClientAgentState AgentState
                        = (ClientAgentState) ah.getAg().getState();
                AgentState.UpdateAgentState(
                        ceUnit,
                        configExp.getContainers(
                                ceUnit.getNumberOfContainers(),
                                ceUnit.getAgentsByContainer()
                        )
                );
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }

            // Kills Previous Client Agent
            // TODO WARNING
            //killUnusedAgent(getClientAgentName(ceUnit.getExperimentID() - 1));

            // First notification to start simulation
            try {
                this.agent.getAdmLocal().getHandlerByAlias(
                        getClientAgentName(
                                ceUnit.getExperimentID()
                        )
                ).sendEvent(
                        new EventBESA(
                                ClientAgentLaunchExperimentUnitGuard.class.getName(),
                                new ExperimentUnitMessage(
                                        ceUnit,
                                        getClientAgentName(
                                                ceUnit.getExperimentID()
                                        )
                                )
                        ));
                ReportBESA.info(
                        "First ClientAgentLaunchExperimentUnitGuard call from "
                        + this.agent.getAlias()
                );
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }
    }

    private void killUnusedAgent(String name) {
        try {
            this.agent.getAdmLocal().killAgent(
                    this.agent.getAdmLocal().getHandlerByAlias(
                            name
                    ).getAgId(),
                    0.91
            );
            ReportBESA.info("Closed " + name);
        } catch (ExceptionBESA ex) {
            ReportBESA.error("Error borrando: " + name);
            ReportBESA.error(ex);
        }
    }

    /**
     * @param exp
     * @return the AgentName
     */
    static String getClientAgentName(int exp) {
        return "ClientAg_E" + String.valueOf(exp);
    }

    /**
     *
     * @param time
     */
    public void waitAgents(int time) {
        try {
            for (int i = 1; i < time; i++) {
                System.out.print(i + " ");
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            ReportBESA.error(ex);
        }
        System.out.println("");
    }
}
