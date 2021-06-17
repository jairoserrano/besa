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
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import WorkerAgent.AgentWorkerTaskExecuteGuard;

/**
 *
 * @author jairo
 */
public class ClientAgentWorkerReadyGuard extends GuardBESA {

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        //ReportBESA.debug("Llegó a ClientAgentWorkerReadyGuard");
        ClientAgentState AgentState = (ClientAgentState) this.agent.getState();
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();

        try {
            String Task = AgentState.getTask();
            EventBESA msj = new EventBESA(
                    AgentWorkerTaskExecuteGuard.class.getName(),
                    new BenchmarkMessage(Task,
                            this.agent.getAlias()
                    )
            );
            AgHandlerBESA ah = this.agent.getAdmLocal().
                    getHandlerByAlias(Message.getAgentRef());
            ReportBESA.debug("Sent " + Task + " to " + Message.getAgentRef());
            ah.sendEvent(msj);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

}
