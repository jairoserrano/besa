/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenttest;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import presentation.model.adapter.MessageData;

/**
 *
 * @author SAR
 */
public class GuardTest extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        MessageData messageData = (MessageData) event.getData();
        System.out.println("Message: " + messageData.getMessage());
    }
}
