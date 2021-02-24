package BESA.Mobile.Agents.PostMan.Guards;

import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Mobile.Agents.PostMan.Data.StringDataBPO;
import BESA.Mobile.Agents.PostMan.State.PostManAgentStateBPO;
import BESA.Mobile.Exceptions.ExceptionBESAConnectionsDirectoryFailedBPO;

/**
 *
 * @author Andrea Barraza
 */
public class ChangeCommunicationChannelGuardBPO extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        DataBESA data = event.getData();
        if (data instanceof StringDataBPO) {
            try {
                if (((StringDataBPO) data).getMessage().contains("true")) {
                    ((PostManAgentStateBPO) this.agent.getState()).changeWriteCommunicationChannel();
                } else {
                    ((PostManAgentStateBPO) this.agent.getState()).changeReadCommunicationChannel();
                }
            } catch (ExceptionBESAConnectionsDirectoryFailedBPO ex) {
                //Do nothing
            }
        }

    }
}
