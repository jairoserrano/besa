package BESA.Mobile.CommunicationChannel.Types.Socket;

import BESA.Mobile.CommunicationChannel.BaseClasses.CommunicationChannelBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Andrea Barraza
 */
public final class ClientSocketBPO extends CommunicationChannelBPO {

    private Socket socket;
    //IO Streams
    private ObjectInputStream inObject;
    private ObjectOutputStream outObject;

    protected ClientSocketBPO(Socket socket) throws ExceptionBESACommunicationChannelFailedBPO {
        super();
        this.socket = socket;
        startUpConnection();
    }

    public ClientSocketBPO(String host, int port) throws ExceptionBESACommunicationChannelFailedBPO {
        super(host, port);
    }

    public ClientSocketBPO(String location) throws ExceptionBESACommunicationChannelFailedBPO {
        super(location);
    }

    @Override
    protected Object readObject() throws ExceptionBESACommunicationChannelFailedBPO {
        try {
            Object message = inObject.readObject();
            return message;
        } catch (Exception ex) {
            throw new ExceptionBESACommunicationChannelFailedBPO("Message could not be read - InputStream Error");
        }
    }

    @Override
    protected void writeObject(Object message) throws ExceptionBESACommunicationChannelFailedBPO {
        try {
            outObject.writeObject(message);
            outObject.flush();
        } catch (Exception e) {
            throw new ExceptionBESACommunicationChannelFailedBPO("Message could not be sent - OutputStream Error");
        }
    }

    @Override
    protected boolean isCommunicationChannelClosed() {
        if (socket != null) {
            if (socket.isConnected() && socket.isBound() && !socket.isClosed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void openIOStreams() throws ExceptionBESACommunicationChannelFailedBPO {
        try {
            outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.flush();
            inObject = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            throw new ExceptionBESACommunicationChannelFailedBPO("Failed to open IOStreams");
        }
    }

    @Override
    protected synchronized void connectCommunicationChannel() throws ExceptionBESACommunicationChannelFailedBPO {
        //Attempt Connection
        try {
            socket = new Socket(getRemoteHost(), getRemotePort());
        } catch (Exception ex) {
            socket = null;
            throw new ExceptionBESACommunicationChannelFailedBPO("Connection could not be established.");
        }
    }

    @Override
    protected void closeCommunicationChannel() {
        //close IOStreams
        try {
            if (outObject != null) {
                outObject.close();
                outObject = null;
            }
        } catch (IOException ex) {
            outObject = null;
        }
        try {
            if (inObject != null) {
                inObject.close();
                inObject = null;
            }

        } catch (IOException ex) {
            inObject = null;
        }

        //close socket
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (Exception ex) {
            socket = null;
        }

    }
}
