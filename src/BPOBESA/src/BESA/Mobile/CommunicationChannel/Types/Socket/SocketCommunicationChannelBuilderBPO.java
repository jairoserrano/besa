package BESA.Mobile.CommunicationChannel.Types.Socket;

import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.CommunicationChannel.BaseClasses.ICommunicationChannelBuilderBPO;
import BESA.Mobile.CommunicationChannel.BaseClasses.ServerBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;

/**
 *
 * @author Andrea
 */
public class SocketCommunicationChannelBuilderBPO implements ICommunicationChannelBuilderBPO {
    
    public final static String NAME="SOCKETS";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ServerBPO getServer(int port) throws ExceptionBESACommunicationChannelFailedBPO {
        return new ServerSocketBPO(port);
    }

    @Override
    public ICommunicationChannelBPO getClient(String location) throws ExceptionBESACommunicationChannelFailedBPO {
        return new ClientSocketBPO(location);
    }

    @Override
    public ICommunicationChannelBPO getClient(String host, int port) throws ExceptionBESACommunicationChannelFailedBPO {
        return new ClientSocketBPO(host, port);
    }
}
