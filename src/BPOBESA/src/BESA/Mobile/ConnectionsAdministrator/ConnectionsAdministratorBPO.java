package BESA.Mobile.ConnectionsAdministrator;

import BESA.Mobile.CommunicationChannel.BaseClasses.ICommunicationChannelBuilderBPO;
import BESA.Mobile.CommunicationChannel.BaseClasses.ServerBPO;
import BESA.Mobile.CommunicationChannel.CommunicationChannelFactoryBPO;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.CommunicationChannel.ServerConnectionListenerBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Andrea The ConnectionsAdministrator is in charge of: - Managing the
 * threads involved in the setup process for new communications. - Managing
 * server connection listener threads. - Managing the
 * communicationChannelFactory. - Managing the available connection information
 * for containers.
 */
public class ConnectionsAdministratorBPO {

    ArrayList<EstablishConnectionBPO> establishConnection_Threads;
    ArrayList<WaitForSetupInfoBPO> waitForSetupInfo_Threads;
    private ArrayList<ServerConnectionListenerBPO> connectionListeners;
    private CommunicationChannelFactoryBPO communicationChannelFactory;
    private HashMap<String, ContainerConnectionInformationBPO> containerConnectionInformation;

    public ConnectionsAdministratorBPO() {
        establishConnection_Threads = new ArrayList();
        waitForSetupInfo_Threads = new ArrayList();
        connectionListeners = new ArrayList();
        communicationChannelFactory = new CommunicationChannelFactoryBPO();
        containerConnectionInformation = new HashMap<String, ContainerConnectionInformationBPO>();
    }

    //Setup new connections
    public void connectTo(String containerID) {
        clean();
        if (!isEstablishConnectionExistant(containerID)) {
            EstablishConnectionBPO establishConnection = new EstablishConnectionBPO(containerID);
            establishConnection.start();
            establishConnection_Threads.add(establishConnection);
        }
    }

    private boolean isEstablishConnectionExistant(String containerID) {
        for (int i = 0; i < establishConnection_Threads.size(); i++) {
            if (establishConnection_Threads.get(i).getRemoteContainerID().equals(containerID)) {
                return true;
            }
        }
        return false;
    }

    public void acceptConnection(ICommunicationChannelBPO communicationChannel) {
        WaitForSetupInfoBPO wait = new WaitForSetupInfoBPO(communicationChannel);
        wait.start();
        waitForSetupInfo_Threads.add(wait);
        clean();
    }

    private void clean() {
        int i = 0;
        while (i < establishConnection_Threads.size()) {
            if (!establishConnection_Threads.get(i).isAlive()) {
                establishConnection_Threads.remove(i);
            } else {
                i++;
            }
        }

        i = 0;
        while (i < waitForSetupInfo_Threads.size()) {
            if (!waitForSetupInfo_Threads.get(i).isAlive()) {
                waitForSetupInfo_Threads.remove(i);
            } else {
                i++;
            }
        }
    }

    //Connection Listeners
    private void addServerListener(ServerBPO server) throws ExceptionBESACommunicationChannelFailedBPO {
        ServerConnectionListenerBPO s = new ServerConnectionListenerBPO(server);
        s.start();
        connectionListeners.add(s);
    }

    //Communication Channel Factory
    public final void addServerListener(int port, String connectionTypeName) throws ExceptionBESACommunicationChannelFailedBPO {
        ServerBPO server = communicationChannelFactory.getCommunicationChannelAsServer(port, connectionTypeName);
        addServerListener(server);
    }

    public final void addCommunicationChannelType(ICommunicationChannelBuilderBPO communicationChannelBuilder) {
        communicationChannelFactory.addCommunicationChannelBuilder(communicationChannelBuilder);
    }

    public final void removeCommunicationChannelType(String connectionTypeName) {
        communicationChannelFactory.removeCommunicationChannelBuilder(connectionTypeName);
    }

    public final ICommunicationChannelBPO getCommunicationChannelAsClient(ContainerConnectionInformationBPO info) throws ExceptionBESACommunicationChannelFailedBPO {
        return communicationChannelFactory.getCommunicationChannelAsClient(info);
    }

    //Container Connection Information
    public void addContainerConnectionInformation(String containerID, ContainerConnectionInformationBPO info) {
        containerConnectionInformation.put(containerID, info);
    }

    public void removeContainerConnectionInformation(String containerID) {
        containerConnectionInformation.remove(containerID);
    }

    public ContainerConnectionInformationBPO getContainerConnectionInformation(String containerID) {
        return containerConnectionInformation.get(containerID);
    }
    //--------------------------------------------------------

    public void shutDown() {
        System.out.println("ENTRO A CONNECTIONS ADMINISTRATOR SHUTDOWN");

        for (int i = 0; i < establishConnection_Threads.size(); i++) {
            establishConnection_Threads.get(i).kill();
        }

        for (int i = 0; i < waitForSetupInfo_Threads.size(); i++) {
            waitForSetupInfo_Threads.get(i).kill();
        }

        establishConnection_Threads.clear();
        waitForSetupInfo_Threads.clear();

        //Stop all server listeners
        for (int i = 0; i < connectionListeners.size(); i++) {
            System.out.println("CLOSE CONNECTION LISTENER FOR PORT: " + ((ServerConnectionListenerBPO) connectionListeners.get(i)).getPort());
            connectionListeners.get(i).kill();
        }

    }
}
