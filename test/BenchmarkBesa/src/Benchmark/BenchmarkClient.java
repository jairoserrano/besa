/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Benchmark;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import ClientAgent.ClientAgent;
import ClientAgent.ClientAgentLaunchExperimentUnitGuard;
import ClientAgent.ClientAgentReportGuard;
import ClientAgent.ClientAgentState;
import ClientAgent.ClientAgentServerReadyGuard;
import Utils.BenchmarkConfig;
import Utils.BenchmarkExperimentUnit;
import Utils.BenchmarkMessage;

/**
 *
 * @author jairo
 */
public class BenchmarkClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Create Config Instance
        BenchmarkConfig configExp = BenchmarkConfig.getConfig(args);
        BenchmarkExperimentUnit ceUnit;
        ClientAgent ClientAg;
        // First experiment already ok
        configExp.setNextExperimentReady();
        // Starting container
        AdmBESA adminBesa = AdmBESA.getInstance("config/Container_MAS_00_00.xml");
        ReportBESA.info("Started " + configExp.getContainerID() + " container");

        // Start experiment by experiment
        for (int i = 0; i < configExp.getExperimentCount(); i++) {

            if (configExp.isNextExperimentReady()) {

                // Get First Experiment Unit
                ceUnit = configExp.getNextExperiment();
                ReportBESA.info(ceUnit);

                ReportBESA.info("Started Experiment: "
                        + ceUnit.getExperimentID()
                );

                // ClientAg Agent created
                try {
                    ClientAgentState BenchmarkState = new ClientAgentState();
                    StructBESA StructSender = new StructBESA();
                    StructSender.bindGuard(ClientAgentServerReadyGuard.class);
                    StructSender.bindGuard(ClientAgentLaunchExperimentUnitGuard.class);
                    StructSender.bindGuard(ClientAgentReportGuard.class);
                    ClientAg = new ClientAgent(
                            getClientAgentName(
                                    ceUnit.getExperimentID()
                            ),
                            BenchmarkState,
                            StructSender,
                            0.91
                    );
                    ClientAg.start();
                    ReportBESA.info(getClientAgentName(
                            ceUnit.getExperimentID()) + " created"
                    );
                } catch (KernelAgentExceptionBESA ex) {
                    ReportBESA.error(ex);
                }

                // Update Agent Status
                try {
                    AgHandlerBESA ah = adminBesa.getHandlerByAlias(
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

                // First notification to start simulation
                try {

                    adminBesa.getHandlerByAlias(
                            getClientAgentName(
                                    ceUnit.getExperimentID()
                            )
                    ).sendEvent(
                            new EventBESA(
                                    ClientAgentLaunchExperimentUnitGuard.class.getName(),
                                    new BenchmarkMessage(
                                            "AgInit",
                                            getClientAgentName(
                                                    ceUnit.getExperimentID()
                                            )
                                    )
                            ));
                    ReportBESA.info("First ClientAgentLaunchExperimentUnitGuard call");

                } catch (ExceptionBESA ex) {
                    ReportBESA.error(ex);
                }

            }

        }

    }

    /**
     * @param exp
     * @return the AgentName
     */
    static String getClientAgentName(int exp) {
        return "ClientAg_E" + String.valueOf(exp);
    }

}
