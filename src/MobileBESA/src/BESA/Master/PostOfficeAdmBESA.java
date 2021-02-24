package BESA.Master;

import BESA.Config.ConfigBESA;
import BESA.ExceptionBESA;
import BESA.Extern.ExternAdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Mobile.CommunicationChannel.BaseClasses.ICommunicationChannelBuilderBPO;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.CommunicationChannel.Types.Socket.SocketCommunicationChannelBuilderBPO;
import BESA.Mobile.ConnectionsAdministrator.ContainerConnectionInformationBPO;
import BESA.Mobile.Directory.AgHandlerBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import BESA.Mobile.IAdmBPO;
import BESA.Mobile.IPostOfficeBPO;
import BESA.Mobile.Message.AdministrativeMessageBPO;
import BESA.Mobile.Message.MessageFactoryBPO;
import BESA.Mobile.PostOfficeBPO;
import BESA.Remote.DistributedExceptionBESA;

/**
 *
 * @author Andrea
 */
public class PostOfficeAdmBESA extends ExternAdmBESA implements IAdmBPO {

    public PostOfficeAdmBESA(ConfigBESA configBESA) throws SystemExceptionBESA, DistributedExceptionBESA {
        super(configBESA);
        initCommunicationChannels();
    }
    
    private void initCommunicationChannels() throws SystemExceptionBESA {
        addCommunicationChannelType(new SocketCommunicationChannelBuilderBPO());
        try {
            addServerListener(this.config.getBpoPort(), SocketCommunicationChannelBuilderBPO.NAME);
        } catch (ExceptionBESACommunicationChannelFailedBPO ex) {
            throw new SystemExceptionBESA(ex.getMessage());
        }
    }
    
    //----------------------POSTOFFICE----------------------------------//

    @Override
    public IPostOfficeBPO getPostOffice() { //Returns interface
        return PostOfficeBPO.get();
    }

    private PostOfficeBPO getPostOfficeForAdm() {
        return PostOfficeBPO.get();
    }

    //-----------------CONNECTION INFORMATION-----------------------------//
    @Override
    public ContainerConnectionInformationBPO getConnectionInfoFor(String containerID) throws ExceptionBESA {
        return getPostOfficeForAdm().getConnectionsAdministrator().getContainerConnectionInformation(containerID);
    }
    //--------------------------------------------------------//

    @Override
    public void killAgent(String agId, double agentPassword) throws ExceptionBESA {
        AgHandlerBESA agh = (AgHandlerBESA) this.getHandlerByAid(agId);
        if (agh != null) {
            if (agh instanceof AgHandlerBPO) {
                System.out.println("KILL AGENT FROM MD:" + agh.getAlias() + "\n");
                sendMessage(((AgHandlerBPO) agh).getLocation(), MessageFactoryBPO.createAgentKilledMessage(agId, agentPassword));
            } else if (agh.getAg() != null) {
                super.killAgent(agId, agentPassword);
            } else {
                super.erase(agh);
            }
        }
    }
    
    //--------------------KILL
    @Override
    public void kill(double containerPassword) throws ExceptionBESA {
        if (Math.abs(this.passwd - containerPassword) < 0.0001) {
            getPostOfficeForAdm().shutDown();
            super.kill(containerPassword);
        }
    }


    //SERVICES CONCERNING BPO ------------------------------------//
    private void sendMessage(String remoteContainerID, AdministrativeMessageBPO message) throws ExceptionBESA {
        getPostOfficeForAdm().sendMessageToLocation(remoteContainerID, message);
        waitForMessageACK(remoteContainerID, message);
    }

    private void waitForMessageACK(String remoteContainerID, AdministrativeMessageBPO message) throws ExceptionBESA {
        try {
            while (message.getACK() == null) {
                message.waitForACK(this.config.getSendEventTimeout());
                //Check if message timed out
                if (message.getACK() == null) {
                    PostOfficeBPO.get().messageTimedOut(remoteContainerID, message);
                }
            }
            if (message.getACK().isNegative()) {
                throw new ExceptionBESA(message.getACK().getExceptionMessage());
            }
        } catch (InterruptedException ex) {
            throw new ExceptionBESA("InterruptedException");
        }
    }

    //SERVICES CONCERNING COMMUNICATION CHANNELS ------------------------------------//
    @Override
    public boolean getDeviceSupportsCommunicationByObjects() {
        /* TODO: This information should be read from ConfigBESA and it is used 
         * if a certain device cannot communicate using objects through
         * the communication channel and therefore communication should be 
         * carried out by strings.
         */
        return true; //Default
    }

    private void addCommunicationChannelType(ICommunicationChannelBuilderBPO communicationChannelBuilder) {
        getPostOfficeForAdm().getConnectionsAdministrator().addCommunicationChannelType(communicationChannelBuilder);
    }
//
//    public final void removeCommunicationChannelType(String connectionTypeName) {
    /*
     * TODO: What happens with communication channels of this connectionType that are opened?
     */
//        getPostOfficeForAdm().getConnectionsAdministrator().removeCommunicationChannelType(connectionTypeName);
//    }

    private void addServerListener(int port, String connectionTypeName) throws ExceptionBESACommunicationChannelFailedBPO {
        getPostOfficeForAdm().getConnectionsAdministrator().addServerListener(port, connectionTypeName);
    }

    @Override
    public final ICommunicationChannelBPO getCommunicationChannelAsClient(ContainerConnectionInformationBPO info) throws ExceptionBESACommunicationChannelFailedBPO {
        return getPostOfficeForAdm().getConnectionsAdministrator().getCommunicationChannelAsClient(info);
    }
}
