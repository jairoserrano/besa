package BESA.Mobile.ConnectionsAdministrator;

import BESA.ExceptionBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.CommunicationChannel.Types.Socket.SocketCommunicationChannelBuilderBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import BESA.Mobile.IAdmBPO;

/**
 *
 * @author Andrea
 */
public class WaitForSetupInfoBPO extends SetUpCommunicationChannelServiceBPO {

    private boolean alive;

    public WaitForSetupInfoBPO(ICommunicationChannelBPO communicationChannel) {
        this.communicationChannel = communicationChannel;
        alive = true;
    }

    @Override
    public void run() {
        try {
            if (alive) {
                setUpCommunicationChannel();
            }
            if (alive) {
                //Register new connection information
                ContainerConnectionInformationBPO info = new ContainerConnectionInformationBPO();
                info.setConnectionType(SocketCommunicationChannelBuilderBPO.NAME);
                info.setContainerLocation(communicationChannel.getRemoteServerListenerLocation());
                ((IAdmBPO)AdmBESA.getInstance()).getPostOffice().getConnectionsAdministrator().addContainerConnectionInformation(communicationChannel.getRemoteContainerID(), info);
                //----
                CommunicationChannelDirectoryBPO.get().addReadCommunicationChannel(communicationChannel.getRemoteContainerID(), communicationChannel);
                ((IAdmBPO)AdmBESA.getInstance()).getPostOffice().notifyChangeInReadCommunicationChannel(communicationChannel.getRemoteContainerID());
            } else {
                throw new Exception("Dead");
            }

        } catch (Exception ex) {
            communicationChannel.close();
        }

    }

    public void kill() {
        alive = false;
    }

    @Override
    protected void setUpCommunicationChannel() throws ExceptionBESACommunicationChannelFailedBPO, ExceptionBESA {
        receiveSetUpInformation();
        sendSetUpInformation();
        setUp();
    }
}
