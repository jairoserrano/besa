package BESA.Mobile.ConnectionsAdministrator;

import BESA.ExceptionBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import BESA.Mobile.IAdmBPO;

/**
 *
 * @author Andrea
 */
public class EstablishConnectionBPO extends SetUpCommunicationChannelServiceBPO {

    private String remoteContainerID;
    private boolean alive;

    public EstablishConnectionBPO(String containerID) {
        this.remoteContainerID = containerID;
        alive = true;
    }

    @Override
    public void run() {
        while (alive) {
            try {
                ContainerConnectionInformationBPO info = ((IAdmBPO) AdmBESA.getInstance()).getConnectionInfoFor(remoteContainerID);
                communicationChannel = ((IAdmBPO) AdmBESA.getInstance()).getCommunicationChannelAsClient(info);
                communicationChannel.setRemoteContainerID(remoteContainerID);
                communicationChannel.connect();
                if (alive) {
                    setUpCommunicationChannel();
                }
                if (alive) {
                    CommunicationChannelDirectoryBPO.get().addWriteCommunicationChannel(remoteContainerID, communicationChannel);
                    ((IAdmBPO)AdmBESA.getInstance()).getPostOffice().notifyChangeInWriteCommunicationChannel(remoteContainerID);
                    kill();
                } else {
                    throw new Exception("Dead");
                }
            } catch (Exception ex) {
                if (communicationChannel != null) {
                    communicationChannel.close();
                    communicationChannel = null;
                }
                ((IAdmBPO)AdmBESA.getInstance()).getPostOffice().couldNotEstablishConnectionWith(remoteContainerID);
                kill();
            }
        }

    }

    public void kill() {
        alive = false;
    }

    public String getRemoteContainerID() {
        return remoteContainerID;
    }

    @Override
    protected void setUpCommunicationChannel() throws ExceptionBESACommunicationChannelFailedBPO, ExceptionBESA {
        sendSetUpInformation();
        receiveSetUpInformation();
        setUp();
    }
}
