package BESA.Mobile.ConnectionsAdministrator;

import BESA.ExceptionBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.IAdmBPO;
import BESA.Mobile.Message.CommunicationChannelMessageBPO;
import BESA.Mobile.Message.MessageBPO;
import BESA.Mobile.Message.MessageFactoryBPO;

/**
 *
 * @author Andrea
 */
public abstract class SetUpCommunicationChannelServiceBPO extends Thread {

    protected ICommunicationChannelBPO communicationChannel;
    private boolean supportsObjects;
    private boolean doesRemoteSupportObjects;
    private String remoteContainerID;
    private String remoteServerListenerLocation;
    protected static final String strSeparator = "<spt>";
//    private String typeOfDevice;

    public SetUpCommunicationChannelServiceBPO() {
        supportsObjects = ((IAdmBPO) AdmBESA.getInstance()).getDeviceSupportsCommunicationByObjects();
        doesRemoteSupportObjects = false;
//        if(AdmBESA.getInstance() instanceof AdmBPOCE){
//            typeOfDevice="L"; //Light
//        }else{
//            typeOfDevice="H"; //Heavy
//        }
    }

    protected abstract void setUpCommunicationChannel() throws ExceptionBESACommunicationChannelFailedBPO, ExceptionBESA;

    protected void sendSetUpInformation() throws ExceptionBESACommunicationChannelFailedBPO {
        String setupInfo = "SETUP";
        // REVISAR - intercambiar el de abajo 
        setupInfo += strSeparator + AdmBESA.getInstance().getAdmHandler().getAlias();
        //con ... cuando listo ....
        //setupInfo += strSeparator + AdmBESA.getInstance().getAdmHandler().getAdmId();
        setupInfo += strSeparator + supportsObjects;
        setupInfo += strSeparator + AdmBESA.getInstance().getConfigBESA().getIpaddress() + ":" + AdmBESA.getInstance().getConfigBESA().getBpoPort();
//        setupInfo += strSeparator + typeOfDevice;
        //Send SetUp Information
        communicationChannel.write(MessageFactoryBPO.createCommunicationChannelMessage(setupInfo));

    }

    protected void receiveSetUpInformation() throws ExceptionBESACommunicationChannelFailedBPO, ExceptionBESAMessageInterpreterFailedBPO {
        //Receive setup information from remote container
        boolean receivedSetup = false;
        MessageBPO message = null;
        while (!receivedSetup) {
            message = communicationChannel.read();
            if ((message instanceof CommunicationChannelMessageBPO)) {
                if (((CommunicationChannelMessageBPO) message).getMessage().contains("SETUP")) {
                    receivedSetup = true;
                }
            }
        }
        String[] setupInformation = ((CommunicationChannelMessageBPO) message).getMessage().split(strSeparator);
        remoteContainerID = setupInformation[1];
        doesRemoteSupportObjects = setupInformation[2].equals("true");
        remoteServerListenerLocation = setupInformation[3];

//        if(setupInformation[4].equals("L")){
//            //REVISAR
//            //Me estoy conectando con un dispositivo movil traer el postman si existe
//        }
    }

    public void setUp() throws ExceptionBESACommunicationChannelFailedBPO {

        //Set Up container name or verify it
        if (communicationChannel.getRemoteContainerID() != null) {
            if (!communicationChannel.getRemoteContainerID().equals(remoteContainerID)) {
                throw new ExceptionBESACommunicationChannelFailedBPO("Connected to the wrong container.");
            }
        } else {
            communicationChannel.setRemoteContainerID(remoteContainerID);
        }

        //Set up communication protocol
        if (supportsObjects && doesRemoteSupportObjects) {
            communicationChannel.activateCommunicationByObject();
        }else{
            System.out.println("COMUNICACION STRING////////////////////");
        }
        communicationChannel.setRemoteServerListenerLocation(remoteServerListenerLocation);
    }
}
