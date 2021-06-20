/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientAgent;

import Utils.BenchmarkMessage;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
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

        BenchmarkConfig configExp = BenchmarkConfig.getConfig();
        ReportBESA.debug("Llegó a  ClientAgentReportGuard");
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();
        //ReportBESA.info(Message.getContent());

        // Capture the state of Agent
        ClientAgentState AgentState = (ClientAgentState) this.agent.getState();
        // Increment the taskdone number
        AgentState.TaskDone();

        //ReportBESA.debug("Done " + AgentState.getTaskListDone() + " of " + AgentState.getTotalTasks() + " tasks.");
        if (AgentState.getTaskListDone() == AgentState.getTotalTasks()) {
            ReportBESA.debug("Making ready another experiment.");
            configExp.setNextExperimentReady();
            //this.agent.shutdownAgent();
        }

        try {
            FileWriter fw = new FileWriter("ReporteBesa.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(AgentState.getExperimentID() + "," + Message.getContent());
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
            ReportBESA.error(ex);
        }

    }

}
