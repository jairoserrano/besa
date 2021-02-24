package BESA.Mobile.ConnectionsAdministrator;

import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.Exceptions.ExceptionBESAConnectionsDirectoryFailedBPO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Andrea
 */
public class CommunicationChannelDirectoryBPO {

    private HashMap<String, CommunicationChannelsForContainer> communicationChannelsByContainerID;
    private static CommunicationChannelDirectoryBPO instance;

    private CommunicationChannelDirectoryBPO() {
        communicationChannelsByContainerID = new HashMap();
    }

    public static CommunicationChannelDirectoryBPO get() {
        if (instance == null) {
            instance = new CommunicationChannelDirectoryBPO();
        }
        return instance;
    }

    public ICommunicationChannelBPO getReadCommunicationChannel(String containerID) throws ExceptionBESAConnectionsDirectoryFailedBPO {
        return getCommunicationChannel(containerID, false);
    }

    public ICommunicationChannelBPO getWriteCommunicationChannel(String containerID) throws ExceptionBESAConnectionsDirectoryFailedBPO {
        return getCommunicationChannel(containerID, true);
    }

    private ICommunicationChannelBPO getCommunicationChannel(String containerID, boolean isWrite) throws ExceptionBESAConnectionsDirectoryFailedBPO {
        removeClosedCommunicationChannelsFor(containerID); //Cleanup
        ICommunicationChannelBPO communicationChannel = null;
        if (communicationChannelsByContainerID.containsKey(containerID)) {
            if (isWrite) {
                communicationChannel = communicationChannelsByContainerID.get(containerID).getWrite();
            } else {
                communicationChannel = communicationChannelsByContainerID.get(containerID).getRead();
            }
        }
        if (communicationChannel != null) {
            return communicationChannel;
        }
        throw new ExceptionBESAConnectionsDirectoryFailedBPO("CommunicationChannels not found");
    }

    public void addReadCommunicationChannel(String containerID, ICommunicationChannelBPO communicationChannel) {
        addCommunicationChannel(containerID, communicationChannel, false);
    }

    public void addWriteCommunicationChannel(String containerID, ICommunicationChannelBPO communicationChannel) {
        addCommunicationChannel(containerID, communicationChannel, true);
    }

    private void addCommunicationChannel(String containerID, ICommunicationChannelBPO communicationChannel, boolean isWrite) {
        if (!communicationChannelsByContainerID.containsKey(containerID)) {
            CommunicationChannelsForContainer channels = new CommunicationChannelsForContainer();
            communicationChannelsByContainerID.put(containerID, channels);
        }

        if (isWrite) {
            communicationChannelsByContainerID.get(containerID).setWrite(communicationChannel);
        } else {
            communicationChannelsByContainerID.get(containerID).setRead(communicationChannel);
        }
    }

    private synchronized void removeClosedCommunicationChannelsFor(String containerID) {
        if (communicationChannelsByContainerID.containsKey(containerID)) {
            if (communicationChannelsByContainerID.get(containerID).getRead() != null) { //If there is a read channel
                if (communicationChannelsByContainerID.get(containerID).getRead().isClosed()) { //and it is closed
                    communicationChannelsByContainerID.get(containerID).getRead().close();
                    communicationChannelsByContainerID.get(containerID).setRead(null); //remove it
                }
            }
            if (communicationChannelsByContainerID.get(containerID).getWrite() != null) {
                if (communicationChannelsByContainerID.get(containerID).getWrite().isClosed()) {
                    communicationChannelsByContainerID.get(containerID).getWrite().close();
                    communicationChannelsByContainerID.get(containerID).setWrite(null);
                }
            }
            if (communicationChannelsByContainerID.get(containerID).getWrite() == null && communicationChannelsByContainerID.get(containerID).getRead() == null) {
                communicationChannelsByContainerID.remove(containerID);
            }
        }
    }

    private synchronized void closeAllCommunicationChannelsFor(String containerID) {
        if (communicationChannelsByContainerID.containsKey(containerID)) {
            if (communicationChannelsByContainerID.get(containerID).getRead() != null) {
                communicationChannelsByContainerID.get(containerID).getRead().close();
            }
            if (communicationChannelsByContainerID.get(containerID).getWrite() != null) {
                communicationChannelsByContainerID.get(containerID).getWrite().close();
            }
        }
    }

    public void shutDown() {
        for (Iterator it = communicationChannelsByContainerID.entrySet().iterator(); it.hasNext();) {
            String containerID = (String) ((Map.Entry) it.next()).getKey();
            closeAllCommunicationChannelsFor(containerID);
        }

        communicationChannelsByContainerID.clear();
    }

    private class CommunicationChannelsForContainer {

        private ICommunicationChannelBPO read;
        private ICommunicationChannelBPO write;

        public CommunicationChannelsForContainer() {
            this.read = null;
            this.write = null;
        }

        public ICommunicationChannelBPO getRead() {
            return read;
        }

        public void setRead(ICommunicationChannelBPO read) {
            this.read = read;
        }

        public ICommunicationChannelBPO getWrite() {
            return write;
        }

        public void setWrite(ICommunicationChannelBPO write) {
            this.write = write;
        }
    }
}
