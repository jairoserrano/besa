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
import ClientAgent.ClientAgentWorkerReadyGuard;
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
        BenchmarkExperimentUnit CurrentExperimentUnit;

        // Starting container
        AdmBESA adminBesa = AdmBESA.getInstance("config/Container_MAS_00_00.xml");
        ReportBESA.info("Started " + configExp.getContainerID() + " container");

        // Get First Experiment Unit
        CurrentExperimentUnit = configExp.getNextExperiment();
        // BUSCAR COMO ENTREGAR DE A UNO SIN ROMPER EL PARALELISMO

        // Benchmark Agent created
        ClientAgentState BenchmarkState
                = new ClientAgentState(
                        CurrentExperimentUnit,
                        "MAS_00_01",
                        "127.0.0.1"
                );
        StructBESA StructSender = new StructBESA();
        StructSender.bindGuard(ClientAgentWorkerReadyGuard.class);
        StructSender.bindGuard(ClientAgentLaunchExperimentUnitGuard.class);
        StructSender.bindGuard(ClientAgentReportGuard.class);

        try {
            ClientAgent ClientAg = new ClientAgent("ClientAg",
                    BenchmarkState,
                    StructSender,
                    0.91
            );
            ClientAg.start();
            ReportBESA.info("ClientAg created");
        } catch (KernelAgentExceptionBESA ex) {
            ReportBESA.error(ex);
        }

        try {
            EventBESA msj = new EventBESA(
                    ClientAgentLaunchExperimentUnitGuard.class.getName(),
                    new BenchmarkMessage("AgInit", "ClientAg"));

            AgHandlerBESA ah = adminBesa.getHandlerByAlias("ClientAg");
            ah.sendEvent(msj);
            ReportBESA.info("First ClientAgentLaunchExperimentUnitGuard call");
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

}
