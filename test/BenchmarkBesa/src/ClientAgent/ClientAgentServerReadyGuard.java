/*/
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
import ServerAgent.AgentServerTaskExecuteGuard;

/**
 *
 * @author jairo
 */
public class ClientAgentServerReadyGuard extends GuardBESA {

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        ReportBESA.debug("Arrive to " + this.agent.getAlias());
        ClientAgentState AgentState = (ClientAgentState) this.agent.getState();
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();

        String Task = AgentState.getTask();

        if (Task.equals("None")) {
            ReportBESA.info("Cerrando agente");
            try {
                this.agent.getAdmLocal().killAgent(
                        this.agent.getAdmLocal().getHandlerByAlias(
                                Message.getAgentRef()
                        ).getAgId(),
                        0.91
                );
                ReportBESA.info("cerrado agentes " + Message.getAgentRef());
                AgentState.AgentDone();
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        } else {
            // Send Message with Task
            try {
                EventBESA msj = new EventBESA(
                        AgentServerTaskExecuteGuard.class.getName(),
                        new BenchmarkMessage(
                                Task,
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

}
