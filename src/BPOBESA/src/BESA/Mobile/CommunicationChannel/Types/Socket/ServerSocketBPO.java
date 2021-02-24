package BESA.Mobile.CommunicationChannel.Types.Socket;

import BESA.Mobile.CommunicationChannel.BaseClasses.ServerBPO;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Andrea
 */
public class ServerSocketBPO extends ServerBPO {

    private ServerSocket server;

    public ServerSocketBPO(int port) throws ExceptionBESACommunicationChannelFailedBPO {
        super(port);
        try {
            this.server = new ServerSocket(getPort());
            server.setSoTimeout(10000);
        } catch (IOException ex) {
            throw new ExceptionBESACommunicationChannelFailedBPO("Server could not be created.");
        }
    }

    @Override
    public ICommunicationChannelBPO accept() throws ExceptionBESACommunicationChannelFailedBPO {
        try {
            Socket clientSocket = server.accept();
            ClientSocketBPO clientSocket_BPO = new ClientSocketBPO(clientSocket);
            return clientSocket_BPO;
        } catch (IOException ex) {
            throw new ExceptionBESACommunicationChannelFailedBPO("Server could not be created.");
        }
    }

    @Override
    public void close() {
        try {
            server.close();
        } catch (IOException ex) {
            server = null;
        }
    }
}
