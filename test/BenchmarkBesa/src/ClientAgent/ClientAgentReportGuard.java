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

/**
 *
 * @author jairo
 */
public class ClientAgentReportGuard extends GuardBESA {

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        //ReportBESA.debug("Llegó a  ClientAgentReportGuard");
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();
        ReportBESA.info(Message.getContent());

        //
        ClientAgentState AgentState = (ClientAgentState) this.agent.getState();
        AgentState.TaskDone();
        ReportBESA.debug("Van " + AgentState.getTaskListDone() + " de " + AgentState.getTotalTasks() + " tareas");

    }

}
