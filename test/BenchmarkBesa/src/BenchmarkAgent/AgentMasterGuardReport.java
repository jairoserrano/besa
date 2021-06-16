/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkAgent;

import Utils.BenchmarkMessage;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author jairo
 */
public class AgentMasterGuardReport extends GuardBESA {

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        ReportBESA.debug("Llegó a  AgentMasterGuardReport");
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();
        ReportBESA.info(Message.getContent());

        //
        AgentMasterState AgentState = (AgentMasterState) this.agent.getState();
        AgentState.TaskDone();
        //ReportBESA.debug("Faltan " + AgentState.Tasks() + " tareas");

    }

}
