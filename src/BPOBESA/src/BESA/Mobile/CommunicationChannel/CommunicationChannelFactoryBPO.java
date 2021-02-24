package BESA.Mobile.CommunicationChannel;

import BESA.Mobile.CommunicationChannel.BaseClasses.ICommunicationChannelBuilderBPO;
import BESA.Mobile.CommunicationChannel.BaseClasses.ServerBPO;
import BESA.Mobile.ConnectionsAdministrator.ContainerConnectionInformationBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrea
 */
public class CommunicationChannelFactoryBPO {

    private List<ICommunicationChannelBuilderBPO> builders;

    public CommunicationChannelFactoryBPO() {
        builders = new ArrayList<ICommunicationChannelBuilderBPO>();
    }

    public void addCommunicationChannelBuilder(ICommunicationChannelBuilderBPO communicationChannelBuilder) {
        builders.add(communicationChannelBuilder);
    }

    public void removeCommunicationChannelBuilder(String connectionType) {
        for (int i = 0; i < builders.size(); i++) {
            if (builders.get(i).getName().equals(connectionType)) {
                builders.remove(i);
                return;
            }
        }
    }

    private ICommunicationChannelBuilderBPO getBuilderFor(String connectionType) throws ExceptionBESACommunicationChannelFailedBPO {
        for (int i = 0; i < builders.size(); i++) {
            if (builders.get(i).getName().equals(connectionType)) {
                return builders.get(i);
            }
        }
        throw new ExceptionBESACommunicationChannelFailedBPO("Failed to create communication channel.");
    }

    public ICommunicationChannelBPO getCommunicationChannelAsClient(ContainerConnectionInformationBPO containerConnectionInformation) throws ExceptionBESACommunicationChannelFailedBPO {
        return getBuilderFor(containerConnectionInformation.getConnectionType()).getClient(containerConnectionInformation.getContainerLocation());
    }

    public ServerBPO getCommunicationChannelAsServer(int port, String connectionType) throws ExceptionBESACommunicationChannelFailedBPO {
        return getBuilderFor(connectionType).getServer(port);
    }

}
