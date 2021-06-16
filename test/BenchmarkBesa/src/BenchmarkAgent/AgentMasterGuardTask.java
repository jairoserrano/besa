/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkAgent;

import BESA.ExceptionBESA;
import Utils.BenchmarkMessage;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BenchmarkAgentWorker.AgentWorkerGuardExecuteTask;

/**
 *
 * @author jairo
 */
public class AgentMasterGuardTask extends GuardBESA {

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        // 
        ReportBESA.debug("Llegó a  AgentMasterGuardTask");
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();

        //
        AgentMasterState AgentState = (AgentMasterState) this.agent.getState();
        //ReportBESA.debug("Faltan " + (AgentState.Tasks()-AgentState.getTaskListID()) + " tareas");

        try {
            EventBESA msj = new EventBESA(
                    AgentWorkerGuardExecuteTask.class.getName(),
                    new BenchmarkMessage(AgentState.getTask(), "BenchmarkAgent")
            );
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAlias(Message.getAgentRef());
            ah.sendEvent(msj);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

}
