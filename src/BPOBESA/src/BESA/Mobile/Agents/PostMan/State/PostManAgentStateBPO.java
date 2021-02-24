package BESA.Mobile.Agents.PostMan.State;

import BESA.Kernel.Agent.StateBESA;
import BESA.Mobile.ConnectionsAdministrator.CommunicationChannelDirectoryBPO;
import BESA.Mobile.Exceptions.ExceptionBESAConnectionsDirectoryFailedBPO;
import BESA.Mobile.Message.MessageBPO;

/**
 *
 * @author Andrea Barraza
 */
public class PostManAgentStateBPO extends StateBESA {

    private String remoteContainerID;
    private MailBoxBPO mailbox_forWriting;
    private MailBoxBPO mailbox_forReading;

    public PostManAgentStateBPO(String remoteContainerID) {
        this.remoteContainerID = remoteContainerID;
        mailbox_forWriting = new MailBoxBPO(remoteContainerID, true);
        mailbox_forReading = new MailBoxBPO(remoteContainerID, false);
    }

    public synchronized void sendMessage(MessageBPO message) {
        mailbox_forWriting.sendMessage(message);
    }

    public synchronized void sendACK(MessageBPO message) {
        mailbox_forReading.sendMessage(message);
    }

    public synchronized void messageTimedOut(MessageBPO message) {
        mailbox_forWriting.messageTimedOut(message);
        mailbox_forReading.messageTimedOut(message); //REVISAR - ACK could time-out ???
    }

    public synchronized void changeWriteCommunicationChannel() throws ExceptionBESAConnectionsDirectoryFailedBPO {
        mailbox_forWriting.setCommunicationChannel(CommunicationChannelDirectoryBPO.get().getWriteCommunicationChannel(remoteContainerID));
    }

    public synchronized void changeReadCommunicationChannel() throws ExceptionBESAConnectionsDirectoryFailedBPO {
        mailbox_forReading.setCommunicationChannel(CommunicationChannelDirectoryBPO.get().getReadCommunicationChannel(remoteContainerID));
    }

    public synchronized void shutdownMailBox() {
        mailbox_forWriting.shutDown();
        mailbox_forReading.shutDown();
    }

    public String getRemoteContainerID() {
        return remoteContainerID;
    }
}
