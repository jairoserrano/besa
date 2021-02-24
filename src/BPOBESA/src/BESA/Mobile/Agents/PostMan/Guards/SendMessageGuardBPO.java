package BESA.Mobile.Agents.PostMan.Guards;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Mobile.Agents.PostMan.Data.MessageDataBPO;
import BESA.Mobile.Agents.PostMan.State.PostManAgentStateBPO;

/**
 *
 * @author Andrea Barraza
 */
public class SendMessageGuardBPO extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        if (event.getData() instanceof MessageDataBPO) {
             MessageDataBPO data = (MessageDataBPO) event.getData();
            ((PostManAgentStateBPO) this.agent.getState()).sendMessage(data.getMessage());
        }
    }
}
