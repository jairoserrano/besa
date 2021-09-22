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
import ControlAgent.*;
import Utils.BenchmarkConfig;
import Utils.BenchmarkExperimentUnit;
import Utils.ExperimentUnitMessage;

/**
 *
 * @author jairo
 */
public class ClientMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Create Config Instance
        BenchmarkConfig configExp = BenchmarkConfig.getConfig(args);
        BenchmarkExperimentUnit ceUnit = configExp.getNextExperiment();

        // Starting container
        AdmBESA adminBesa = AdmBESA.getInstance(
                "config/Container_MAS_00_00.xml"
        );
        ReportBESA.info("Started " + configExp.getContainerID() + " container");

        // Starting Control Agent
        try {
            ControlAgentState ControlAgentState = new ControlAgentState();
            StructBESA StructSender = new StructBESA();
            StructSender.bindGuard(ControlAgentLaunchGuard.class);
            ControlAgent ControlAgent = new ControlAgent(
                    "ControlAgent",
                    ControlAgentState,
                    StructSender,
                    0.91
            );
            ControlAgent.start();
            ReportBESA.info("ControlAgent created");
        } catch (KernelAgentExceptionBESA ex) {
            ReportBESA.error(ex);
        }

        // First notification to start simulation
        try {
            adminBesa.getHandlerByAlias("ControlAgent").sendEvent(
                    new EventBESA(
                            ControlAgentLaunchGuard.class.getName(),
                            new ExperimentUnitMessage(
                                    ceUnit,
                                    "ControlAgent"
                            )
                    )
            );
            ReportBESA.info("First ControlAgent call");
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

}
