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
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();

        // Capture the state of Agent
        ClientAgentState LocalAgentState = (ClientAgentState) this.agent.getState();

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
