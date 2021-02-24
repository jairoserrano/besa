package BESA.Mobile;

import BESA.ExceptionBESA;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.ConnectionsAdministrator.ConnectionsAdministratorBPO;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.Message.MessageBPO;

/**
 *
 * @author Andrea
 */
public interface IPostOfficeBPO {

    public void messageTimedOut(String receiverContainerID, MessageBPO message) throws ExceptionBESA;

    public void interpretMessage(String emisorContainerID, MessageBPO message) throws ExceptionBESAMessageInterpreterFailedBPO;

    public void connectTo(String containerID);

    public void acceptConnection(ICommunicationChannelBPO communicationChannel);

    public void sendMessageToLocation(String containerID, MessageBPO message) throws ExceptionBESA;

    public void notifyChangeInWriteCommunicationChannel(String containerID) throws ExceptionBESA;

    public void notifyChangeInReadCommunicationChannel(String containerID) throws ExceptionBESA;

    public ConnectionsAdministratorBPO getConnectionsAdministrator();

    public void couldNotEstablishConnectionWith(String remoteContainerID);
}
