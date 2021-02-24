package BESA.Mobile.CommunicationChannel.BaseClasses;

import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;

/**
 *
 * @author Andrea
 */
public interface ICommunicationChannelBuilderBPO {

    public String getName();

    public ServerBPO getServer(int port) throws ExceptionBESACommunicationChannelFailedBPO;

    public ICommunicationChannelBPO getClient(String location) throws ExceptionBESACommunicationChannelFailedBPO;

    public ICommunicationChannelBPO getClient(String host, int port) throws ExceptionBESACommunicationChannelFailedBPO;
}
