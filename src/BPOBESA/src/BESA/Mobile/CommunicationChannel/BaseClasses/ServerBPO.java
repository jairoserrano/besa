package BESA.Mobile.CommunicationChannel.BaseClasses;

import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;

/**
 *
 * @author Andrea
 */
public abstract class ServerBPO {

    private int port;

    public ServerBPO(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    //Note: Should not be blocking
    public abstract ICommunicationChannelBPO accept() throws ExceptionBESACommunicationChannelFailedBPO;

    public abstract void close();
}
