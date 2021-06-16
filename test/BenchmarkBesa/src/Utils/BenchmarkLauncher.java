/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BenchmarkAgent.AgentMasterGuardReport;
import BenchmarkAgent.AgentMasterGuardTask;
import BenchmarkAgent.AgentMasterSingle;
import BenchmarkAgent.AgentMasterState;
import BenchmarkAgentWorker.AgentWorker;
import BenchmarkAgentWorker.AgentWorkerGuardExecuteTask;
import BenchmarkAgentWorker.AgentWorkerState;
import java.util.ArrayList;

/**
 *
 * @author jairo
 */
public class BenchmarkLauncher {

    BenchmarkExperimentUnit Experiment;
    BenchmarkConfig Config;
    AdmBESA AdminBesa;
    ArrayList<AgentWorker> Agents;
    String AgentName = "AgentWorker_";
    AgentMasterSingle BenchmarkAgent;

    public BenchmarkLauncher(BenchmarkExperimentUnit experiment, AdmBESA admin) {
        this.Experiment = experiment;
        this.Config = BenchmarkConfig.getConfig();
        this.AdminBesa = admin;
        Agents = new ArrayList<>();
    }

    public void runSimulation() {

        //
        //ReportBESA.debug(Experiment);
        // Crea el agente principal Benckmark y setea las guardas
        AgentMasterState BenchmarkState = new AgentMasterState(this.Experiment);
        StructBESA StructSender = new StructBESA();
        StructSender.bindGuard(AgentMasterGuardTask.class);
        StructSender.bindGuard(AgentMasterGuardReport.class);

        try {
            BenchmarkAgent = new AgentMasterSingle("BenchmarkAgent",
                    BenchmarkState,
                    StructSender,
                    0.91
            );
            BenchmarkAgent.start();
            this.AdminBesa.registerAgent(BenchmarkAgent,
                    "BenchmarkAgent",
                    "BenchmarkAgent"
            );
        } catch (KernelAgentExceptionBESA ex) {
            ReportBESA.error(ex);
        }

        // Se lanza el benchmark en caso de ser contenedor principal
        try {
            AgentWorkerState WorkerState = new AgentWorkerState();
            StructBESA WorkerStruct = new StructBESA();
            WorkerStruct.bindGuard(AgentWorkerGuardExecuteTask.class);

            // Lanzamiento de todos los agentes del contenedor.
            for (int i = 1; i <= this.Experiment.getNumberOfAgents(); i++) {
                ReportBESA.debug("Creando " + AgentName + String.valueOf(i));
                this.Agents.add(
                        new AgentWorker(
                                AgentName + String.valueOf(i),
                                WorkerState,
                                WorkerStruct,
                                0.91
                        )
                );
            }
            // 
            for (int i = 0; i < Agents.size(); i++) {
                Agents.get(i).start();
                this.AdminBesa.registerAgent(
                        Agents.get(i),
                        Agents.get(i).getAlias(),
                        Agents.get(i).getAlias()
                );
            }
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

        ReportBESA.info("esperando 5 segundos para mover agentes nuevos");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            ReportBESA.error(ex);
        }
        // @TODO: MOVERLOS A SU CONTENEDOR
        // AHORA TODOS SE MUEVEN A MAS_101
        for (int i = 1; i <= this.Experiment.getNumberOfAgents(); i++) {
            ReportBESA.debug("Moviendo agente " + AgentName + String.valueOf(i));
            try {
                this.AdminBesa.moveAgent(AgentName + String.valueOf(i), "MAS_101", 0.91);
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }
        /*ReportBESA.info("esperando 5 seg");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            ReportBESA.error(ex);
        }*/
    }

    public void stopSimulation() {

        AgentMasterState AgentState
                = (AgentMasterState) BenchmarkAgent.getState();
        while (AgentState.SimulationRunning()) {
            ReportBESA.debug("Simulación en ejecución...");
            ReportBESA.debug("Realizadas "
                    + AgentState.getTaskListDone()
                    + " tareas"
            );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ReportBESA.error(ex);
            }
        }

        try {
            this.AdminBesa.killAgent("BenchmarkAgent", 0.91);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

//        ReportBESA.info("esperando 5 seg");
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException ex) {
//            ReportBESA.error(ex);
//        }
//
//        for (int i = 1; i <= this.Experiment.getNumberOfAgents(); i++) {
//            try {
//                ReportBESA.debug("Matando agente " + AgentName + String.valueOf(i));
//                this.AdminBesa.killAgent(this.AgentName + String.valueOf(i),
//                        0.91);
//            } catch (ExceptionBESA ex) {
//                ReportBESA.error(ex);
//            }
//        }
    }

}
