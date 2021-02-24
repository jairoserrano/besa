/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenttest;


import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import serviceprovider.ResponseMessage;

/**
 *
 * @author fabianjose
 */
public class GuardReplyTest extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        ResponseMessage responseMessage = (ResponseMessage) event.getData();
        System.out.println("Response: " + responseMessage.getResponse());
    }
}
