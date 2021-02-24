package BESA.Mobile.CommunicationChannel;

import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.Message.MessageBPO;

/**
 *
 * @author Andrea Barraza
 */
public interface ICommunicationChannelBPO {

    public abstract MessageBPO read() throws ExceptionBESACommunicationChannelFailedBPO, ExceptionBESAMessageInterpreterFailedBPO;

    public abstract void write(MessageBPO message) throws ExceptionBESACommunicationChannelFailedBPO;

    public abstract void connect() throws ExceptionBESACommunicationChannelFailedBPO;

    public abstract void close();

    public abstract boolean isClosed();

    public abstract void setRemoteContainerID(String containerID);

    public abstract String getRemoteContainerID();
    
    public abstract String getRemoteServerListenerLocation();

    public abstract void setRemoteServerListenerLocation(String location);

    public abstract void activateCommunicationByObject();
}
