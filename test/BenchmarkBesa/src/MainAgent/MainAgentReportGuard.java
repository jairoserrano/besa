/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainAgent;

import BESA.ExceptionBESA;
import Utils.BenchmarkMessage;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import WorkerAgent.WorkerAgentTaskExecuteGuard;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author jairo
 */
public class MainAgentReportGuard extends GuardBESA {

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        ReportBESA.debug("Arrive to " + this.agent.getAlias());
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();

        // Capture the state of Agent
        MainAgentState LocalAgentState = (MainAgentState) this.agent.getState();

        // Increment the taskdone number
        ReportBESA.info("Incrementa tareas completadas");
        LocalAgentState.TaskDone();

        // Write results to Logfile
        try {
            FileWriter fw = new FileWriter("ReporteBesa.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(LocalAgentState.getExperimentID() + "," + Message.getContent());
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
            ReportBESA.error(ex);
        }

    }

}
