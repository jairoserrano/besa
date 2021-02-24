package BESA.Mobile.Message;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import java.util.ArrayList;
//import java.util.UUID;

/**
 *
 * @author Andrea Barraza
 */
public class MessageFactoryBPO {

    public synchronized static MessageBPO rebuildMessageFromString(String messageString) throws ExceptionBESAMessageInterpreterFailedBPO {
        String split[] = messageString.split(MessageBPO.strSeparatorA);
        if (split.length == 2) {
            MessageBPO message = null;
            if (split[0].equals(MessageBPO.Type.ACK.toString())) {
                message = new ACKMessageBPO();
            } else if (split[0].equals(MessageBPO.Type.EVENT.toString())) {
                message = new EventMessageBPO();
            } else if (split[0].equals(MessageBPO.Type.ADMINISTRATIVE.toString())) {
                message = new AdministrativeMessageBPO();
            } else if (split[0].equals(MessageBPO.Type.COMMUNICATION_CHANNEL.toString())) {
                message = new CommunicationChannelMessageBPO();
            }
            message.buildMessage(split[1]);

            return message;
        }
        throw new ExceptionBESAMessageInterpreterFailedBPO("Received String Message could not be converted to Message Object");
    }

    /****************EVENT***************************/
    public synchronized static EventMessageBPO createEventMessage(EventBESA event, String agIdReceiver, String agAliasReceiver) {
        EventMessageBPO eventMessage = new EventMessageBPO(getId(), event, agIdReceiver, agAliasReceiver);
        return eventMessage;
    }

    /****************ACK***************************/
    public synchronized static ACKMessageBPO createPositiveACK(String idMessage) {
        ACKMessageBPO ackMessage = new ACKMessageBPO(idMessage);
        return ackMessage;
    }

    public synchronized static ACKMessageBPO createNegativeACK(String idMessage, String exceptionMessage) {
        ACKMessageBPO ackMessage = new ACKMessageBPO(idMessage, exceptionMessage);
        return ackMessage;
    }

    /****************Administrative***************************/
    public synchronized static AdministrativeMessageBPO createAgentCreatedMessage(String newAgID, String newAgAlias) {
        AdministrativeMessageBPO admMessage = new AdministrativeMessageBPO(getId());
        admMessage.setCreateAgent(newAgID, newAgAlias);
        return admMessage;
    }

    public synchronized static AdministrativeMessageBPO createDoesAgentExistMessage(String newAgID, String newAgAlias) {
        AdministrativeMessageBPO admMessage = new AdministrativeMessageBPO(getId());
        admMessage.setDoesAgentExist(newAgID, newAgAlias);
        return admMessage;
    }

    public synchronized static AdministrativeMessageBPO createAgentKilledMessage(String deletedAgID, double deleteAgPassword) {
        AdministrativeMessageBPO admMessage = new AdministrativeMessageBPO(getId());
        admMessage.setEliminateAgent(deletedAgID, deleteAgPassword);
        return admMessage;
    }

    public synchronized static AdministrativeMessageBPO createAgentMovedMessage(String movedAgID, String movedAgAlias, String newDestination, double movedAgPassword) {
        AdministrativeMessageBPO admMessage = new AdministrativeMessageBPO(getId());
        admMessage.setMoveAgent(movedAgID, movedAgAlias, newDestination, movedAgPassword);
        return admMessage;
    }

    public synchronized static AdministrativeMessageBPO createAddServiceMessage(String serviceId) {
        AdministrativeMessageBPO admMessage = new AdministrativeMessageBPO(getId());
        admMessage.setAddService(serviceId);
        return admMessage;
    }

    public synchronized static AdministrativeMessageBPO createAddServiceMessage(String serviceId, ArrayList<String> descriptors) {
        AdministrativeMessageBPO admMessage = new AdministrativeMessageBPO(getId());
        admMessage.setAddService(serviceId, descriptors);
        return admMessage;
    }
    public synchronized static AdministrativeMessageBPO createBindServiceMessage(String agId, String serviceId) {
        AdministrativeMessageBPO admMessage = new AdministrativeMessageBPO(getId());
        admMessage.setBindService(agId, serviceId);
        return admMessage;
    }

    public synchronized static AdministrativeMessageBPO createBindSPServiceInDirectoryMessage(String agId,String directoryServiceName) {
        AdministrativeMessageBPO admMessage = new AdministrativeMessageBPO(getId());
        admMessage.setBindSPServiceInDirectory(agId, directoryServiceName);
        return admMessage;
    }

    /****************COMMUNICATIONCHANNEL***************************/
    public synchronized static CommunicationChannelMessageBPO createCommunicationChannelMessage(String messageString) {
        CommunicationChannelMessageBPO message = new CommunicationChannelMessageBPO("1", messageString);
        return message;
    }
    /****************Utils***************************/
//    public static String getId() {
//        return UUID.randomUUID().toString();
//    }
    //REVISAR-BORRAR
    private static int cont = 1;

    public static String getId() {
        return String.valueOf(cont++);
    }
}
