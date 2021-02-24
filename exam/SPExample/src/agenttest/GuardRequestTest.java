/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenttest;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.Social.ServiceProvider.agent.GuardServiceProviderRequest;
import BESA.Kernel.Social.ServiceProvider.agent.ServiceProviderDataRequest;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import serviceprovider.RequestMessage;
import serviceprovider.SPServiceSendMessage;
import serviceprovider.ServiceProviderMessageAgent;

/**
 *
 * @author fabianjose
 */
public class GuardRequestTest extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        RequestMessage dataToSend = new RequestMessage("!Hi world¡");
        try {
            String spID = this.getAgent().getAdmLocal().lookupSPServiceInDirectory(ServiceProviderMessageAgent.SERVICEONDIRECTORY);
            AgHandlerBESA agH = AdmBESA.createInstance().getHandlerByAid(spID);
            ServiceProviderDataRequest spDataRequest = new ServiceProviderDataRequest(this.getAgent().getAid(), SPServiceSendMessage.SERVICE_NAME, dataToSend);
            EventBESA ev = new EventBESA(GuardServiceProviderRequest.class.getName(), spDataRequest);
            agH.sendEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
