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
import BESA.Log.ReportBESA;
import MainAgent.MainAgent;
import MainAgent.MainAgentLaunchExperimentUnitGuard;
import MainAgent.MainAgentReportGuard;
import MainAgent.MainAgentServerReadyGuard;
import MainAgent.MainAgentState;
import Utils.BenchmarkConfig;
import Utils.BenchmarkExperimentUnit;
import Utils.ExperimentUnitMessage;

/**
 *
 * @author jairo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Create Config Instance
        BenchmarkConfig configExp = BenchmarkConfig.getConfig(args);
        BenchmarkExperimentUnit experiment = configExp.getExperiment();
        MainAgent MainAg = null;

        // Starting container
        AdmBESA adminBesa = AdmBESA.getInstance(
                "config/Container_" + args[0] + ".xml"
        );
        ReportBESA.info("Started " + configExp.getContainerID() + " container");

        ReportBESA.info("Started Experiment: " + experiment.getExperimentID());

        // MainAg Agent created
        try {
            MainAgentState BenchmarkState = new MainAgentState();
            StructBESA StructSender = new StructBESA();
            StructSender.bindGuard(MainAgentServerReadyGuard.class);
            StructSender.bindGuard(MainAgentLaunchExperimentUnitGuard.class);
            StructSender.bindGuard(MainAgentReportGuard.class);
            MainAg = new MainAgent("MainAg",
                    BenchmarkState,
                    StructSender,
                    0.91
            );
            MainAg.start();
            ReportBESA.info(
                    "\n\n*******************************\n"
                    + "MainAg created\n"
                    + "*******************************\n"
            );
        } catch (KernelAgentExceptionBESA ex) {
            ReportBESA.error(ex);
        }

        // Update Agent Status
        MainAgentState AgentState = (MainAgentState) MainAg.getState();
        AgentState.UpdateAgentState(
                experiment,
                configExp.getContainers(
                        experiment.getNumberOfContainers(),
                        experiment.getAgentsByContainer()
                )
        );

        // First notification to start simulation
        try {
            adminBesa.getHandlerByAlias("MainAg"
            ).sendEvent(
                    new EventBESA(
                            MainAgentLaunchExperimentUnitGuard.class.getName(),
                            new ExperimentUnitMessage(
                                    experiment,
                                    "MainAg"
                            )
                    ));
            ReportBESA.info(
                    "First MainAgentLaunchExperimentUnitGuard call from "
                    + MainAg.getAlias()
            );
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }
}
