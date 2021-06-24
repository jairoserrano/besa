/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientAgent;

import BESA.ExceptionBESA;
import Utils.BenchmarkMessage;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import Utils.BenchmarkConfig;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author jairo
 */
public class ClientAgentReportGuard extends GuardBESA {

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        ReportBESA.debug("Arrive to " + this.agent.getAlias());
        BenchmarkConfig configExp = BenchmarkConfig.getConfig();
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();

        // Capture the state of Agent
        ClientAgentState AgentState = (ClientAgentState) this.agent.getState();

        // Increment the taskdone number
        ReportBESA.info("Incrementa tareas completadas");
        AgentState.TaskDone();

        // Write results to Logfile
        try {
            FileWriter fw = new FileWriter("ReporteBesa.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(AgentState.getExperimentID() + "," + Message.getContent());
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
            ReportBESA.error(ex);
        }

        ReportBESA.debug("Done " + AgentState.getTaskListDone() + " of " + AgentState.getTotalTasks() + " Tasks.");
        // Check if task are done and active next Experiment
        if (AgentState.getTaskListDone() == AgentState.getTotalTasks()) {
            configExp.setNextExperimentReady();
        }

        ReportBESA.debug("Done " + AgentState.getAgentCountDone() + " of " + AgentState.CurrentExperiment.getNumberOfAgents() + " agents.");
        // Check if Experiment are Done
        if (AgentState.getAgentCountDone() == AgentState.CurrentExperiment.getNumberOfAgents()) {
            if (AgentState.getTaskListDone() == AgentState.getTotalTasks()) {
                //this.agent.shutdownAgent();
            }
        }

    }

}
