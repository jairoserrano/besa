package BESA.Mobile;

import BESA.ExceptionBESA;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.ConnectionsAdministrator.ContainerConnectionInformationBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
//import BESA.PostOffice.CommunicationChannel.BaseClassesForNewType.ICommunicationChannelBuilderBPO;

/**
 *
 * @author Andrea
 */
public interface IAdmBPO {

    public abstract boolean getDeviceSupportsCommunicationByObjects();

//    public abstract void addCommunicationChannelType(ICommunicationChannelBuilderBPO communicationChannelBuilder);
//
//    public abstract void removeCommunicationChannelType(String connectionTypeName);
//
//    public abstract void addServerListener(int port, String connectionTypeName) throws ExceptionBESACommunicationChannelFailedBPO;

    public abstract ICommunicationChannelBPO getCommunicationChannelAsClient(ContainerConnectionInformationBPO info) throws ExceptionBESACommunicationChannelFailedBPO;

    public abstract ContainerConnectionInformationBPO getConnectionInfoFor(String containerID) throws ExceptionBESA;
    
    public IPostOfficeBPO getPostOffice();
   
}
