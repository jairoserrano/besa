package BESA.Mobile.CommunicationChannel.BaseClasses;

import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.CommunicationChannel.RepairConnectionServiceBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.Message.CommunicationChannelMessageBPO;
import BESA.Mobile.Message.MessageFactoryBPO;
import BESA.Mobile.Message.MessageBPO;

/**
 *
 * @author Andrea
 */
public abstract class CommunicationChannelBPO implements ICommunicationChannelBPO {

    private boolean hasBeenConnected;
    private boolean hasBeenClosed;
    protected RepairConnectionServiceBPO repairClientSocketConnectionService;
    private boolean isCommunicationByString;
    private int port;
    private String host;
    private String remoteContainerID;
    private String remoteServerListenerLocation;

    protected CommunicationChannelBPO() throws ExceptionBESACommunicationChannelFailedBPO {
        init();
    }

    protected CommunicationChannelBPO(String host, int port) throws ExceptionBESACommunicationChannelFailedBPO {
        init();
        setRemoteAddress(host, port);
    }

    protected CommunicationChannelBPO(String location) throws ExceptionBESACommunicationChannelFailedBPO {
        init();
        String[] address = location.split(":");
        setRemoteAddress(address[0], (int) Integer.valueOf(address[1]));
    }

    protected final void init() {
        hasBeenConnected = false;
        hasBeenClosed = false;
        repairClientSocketConnectionService = (new RepairConnectionServiceBPO(this));
        isCommunicationByString = true;
    }

    protected void startUpConnection() throws ExceptionBESACommunicationChannelFailedBPO {
        hasBeenConnected = true;
        openIOStreams();
        isCommunicationByString = true;//String communication is opened by default
    }

    @Override
    public void connect() throws ExceptionBESACommunicationChannelFailedBPO {
        if (hasBeenConnected || hasBeenClosed) {
            throw new ExceptionBESACommunicationChannelFailedBPO("Communication Channel has been previously closed.");
        }
        //If socket is open... do nothing
        if (!isClosed()) {
            return;
        }

        //Attempt Connection
        try {
            connectCommunicationChannel();
            startUpConnection();
        } catch (Exception ex) {
            closeCommunicationChannel(); 
        }
        if (isClosed() && !repairClientSocketConnectionService.isAlive()) {
            initRepairCommunicationChannelService();
        }
    }

    private synchronized void initRepairCommunicationChannelService() throws ExceptionBESACommunicationChannelFailedBPO {
        repairClientSocketConnectionService.start();
        try {
            wait();
            if (!repairClientSocketConnectionService.getIsRepaired()) {
                close();
                throw new ExceptionBESACommunicationChannelFailedBPO("Could not establish connection.");
            }
        } catch (InterruptedException ex) {
            throw new ExceptionBESACommunicationChannelFailedBPO("Failure to create socket connection.");
        }
    }

    public synchronized void wakeUp() {
        notify();
    }

    @Override
    public void close() {
        if (hasBeenClosed) {
            return;
        }
        hasBeenClosed = true;
        repairClientSocketConnectionService.kill();
        repairClientSocketConnectionService = null;
        closeCommunicationChannel();
    }

    @Override
    public boolean isClosed() {
        if (hasBeenClosed) {
            return true;
        }
        if (!isCommunicationChannelClosed()) {
            try {
                write(MessageFactoryBPO.createCommunicationChannelMessage("PING"));
                return false;
            } catch (Exception ex) {
            }
        }
        return true;
    }

    @Override
    public MessageBPO read() throws ExceptionBESACommunicationChannelFailedBPO, ExceptionBESAMessageInterpreterFailedBPO {
        MessageBPO message = null;

        if (getIsCommunicationByString()) {
            message = MessageFactoryBPO.rebuildMessageFromString((String) readObject());
        } else {
            message = (MessageBPO) readObject();
        }
        if (message.getId() == null) {
            throw new ExceptionBESAMessageInterpreterFailedBPO("Received object could not be interpreted.");
        }

        //If the message is a PING then ignore it...
        if (message instanceof CommunicationChannelMessageBPO) {
            if (((CommunicationChannelMessageBPO) message).getMessage().contains("PING")) {
                //Ignore the message
                return read();
            }
        }
        return message;
    }

    @Override
    public void write(MessageBPO message) throws ExceptionBESACommunicationChannelFailedBPO {
        if (getIsCommunicationByString()) {
            writeObject(message.toString());
        } else {
            writeObject(message);
        }
    }

    private void setRemoteAddress(String host, int port) {
        this.port = port;
        this.host = host;
    }

    protected String getRemoteHost() {
        return host;
    }

    protected int getRemotePort() {
        return port;
    }

    protected boolean getIsCommunicationByString() {
        return isCommunicationByString;
    }

    @Override
    public void activateCommunicationByObject() {
        isCommunicationByString = false;
    }

    @Override
    public void setRemoteContainerID(String containerID) {
        this.remoteContainerID = containerID;
    }

    @Override
    public String getRemoteContainerID() {
        return remoteContainerID;
    }

    @Override
    public String getRemoteServerListenerLocation() {
        return remoteServerListenerLocation;
    }

    @Override
    public void setRemoteServerListenerLocation(String location) {
        remoteServerListenerLocation = location;
    }

    protected abstract Object readObject() throws ExceptionBESACommunicationChannelFailedBPO;

    protected abstract void writeObject(Object message) throws ExceptionBESACommunicationChannelFailedBPO;

    protected abstract void openIOStreams() throws ExceptionBESACommunicationChannelFailedBPO;

    protected abstract void connectCommunicationChannel() throws ExceptionBESACommunicationChannelFailedBPO;

    protected abstract void closeCommunicationChannel();

    protected abstract boolean isCommunicationChannelClosed();
}
