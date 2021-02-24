package BESA.Mobile.Agents.PostMan.Data;

import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;

/**
 *
 * @author Andrea
 */
public class CommunicationChannelDataBPO extends DataBESA {
    
    private ICommunicationChannelBPO communicationChannel;

    public CommunicationChannelDataBPO(ICommunicationChannelBPO communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public ICommunicationChannelBPO getCommunicationChannel() {
        return communicationChannel;
    }

    public void setCommunicationChannel(ICommunicationChannelBPO communicationChannel) {
        this.communicationChannel = communicationChannel;
    }
    
    
}
