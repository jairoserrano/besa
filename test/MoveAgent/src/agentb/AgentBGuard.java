/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentb;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import common.Message;

/**
 *
 * @author fabianjose
 */
public class AgentBGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        Message message = (Message) event.getData();
        AgentBState agentBState = (AgentBState) agent.getState();
        agentBState.upCont();
        ReportBESA.debug("Received " + message.getMessage() + " from agent A.");
        ReportBESA.debug("Total = " + agentBState.getCont() + ".");
    }
}
