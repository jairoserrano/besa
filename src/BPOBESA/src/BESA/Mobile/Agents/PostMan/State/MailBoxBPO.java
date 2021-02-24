package BESA.Mobile.Agents.PostMan.State;

import BESA.Kernel.System.AdmBESA;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.IAdmBPO;
import BESA.Mobile.Message.ACKMessageBPO;
import BESA.Mobile.Message.BlockingMessageBPO;
import BESA.Mobile.Message.MessageBPO;
import BESA.Mobile.Message.MessageFactoryBPO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrea Barraza
 */
public final class MailBoxBPO {

    private List<MessageBPO> pendingToSend;
    private List<MessageBPO> pendingToSendWithPriority;
    private List<BlockingMessageBPO> pendingToReceiveACK;
    private MailingServiceBPO mailingService;
    private String remoteContainerID;
    private boolean retryConnectionWhenProblemDetected;
    private boolean attemptingSend;

    protected MailBoxBPO(String remoteContainerID, boolean retryConnectionWhenProblemDetected) {
        pendingToSend = new ArrayList();
        pendingToSendWithPriority = new ArrayList();
        pendingToReceiveACK = new ArrayList();
        mailingService = new MailingServiceBPO(this);
        this.remoteContainerID = remoteContainerID;
        System.out.println("\n\nSET in MailBox remoteContainerID "+remoteContainerID+"\n\n");
        this.retryConnectionWhenProblemDetected = retryConnectionWhenProblemDetected;
        attemptingSend = false;
    }

    protected void receiveMessage(MessageBPO message) throws ExceptionBESAMessageInterpreterFailedBPO {
        if (message instanceof ACKMessageBPO) {
            System.out.println("RECIBIO ACK " + message.getId() + " is Negative:" + ((ACKMessageBPO) message).isNegative() + " from " + remoteContainerID);
            int messagePosition = getMessagePosition(pendingToReceiveACK, message);
            if (messagePosition != -1) { //If the message is in the list
                BlockingMessageBPO msg = pendingToReceiveACK.remove(messagePosition);
                msg.setACK((ACKMessageBPO) message);
            }
        } else {
            ((IAdmBPO)AdmBESA.getInstance()).getPostOffice().interpretMessage(remoteContainerID, message);
        }
    }

    protected synchronized boolean sendMessage(MessageBPO message) {
        return sendMessageUsingQueues(message, false);
    }

    private synchronized boolean retrySendMessage(MessageBPO message) {
        //If the message is in the pendingToSendWithPriority list do not attempt to resend it again
        int posInPendingToSendWithPriority = getMessagePosition(pendingToSendWithPriority, message);
        if (posInPendingToSendWithPriority != -1) {
            return false;
        }
        return sendMessageUsingQueues(message, true);
    }

    private synchronized boolean sendMessageUsingQueues(MessageBPO message, boolean withPriority) {
        return sendMessageAux(message, withPriority, true);
    }

    private synchronized boolean sendMessageWithoutQueues(MessageBPO message) {
        return sendMessageAux(message, null, false);
    }

    private synchronized boolean sendMessageAux(MessageBPO message, Boolean withPriority, Boolean useWaitingQueues) {
        attemptingSend = true;
        if (mailingService.sendMessage(message)) {
            if (message instanceof BlockingMessageBPO) {
                System.out.println("ENVIO MSG " + message + " for " + remoteContainerID);
                pendingToReceiveACK.add((BlockingMessageBPO) message);
            }
            attemptingSend = false;
            return true;
        } else {
            if (useWaitingQueues) {
                if (!withPriority) {
                    System.out.println("Agrego a pendingToSend " + message.getId() + " for " + remoteContainerID);
                    pendingToSend.add(message);
                } else {
                    System.out.println("Agrego a pendingToSendWithPriority " + message.getId() + " for " + remoteContainerID);
                    pendingToSendWithPriority.add(message);
                }
            }
            attemptingSend = false;
            return false;
        }
    }

    protected synchronized boolean sendAllUnsentMessages() {
        System.out.println("Send all unsent messages for " + remoteContainerID);
        //First send messages with priority
        int size = pendingToSendWithPriority.size();
        for (int i = 0; i < size; i++) {
            if (sendMessageWithoutQueues(pendingToSendWithPriority.get(0))) {
                pendingToSendWithPriority.remove(0);
            } else {
                return false;
            }
        }

        size = pendingToSend.size();
        for (int i = 0; i < size; i++) {
            if (sendMessageWithoutQueues(pendingToSend.get(0))) {
                pendingToSend.remove(0);
            } else {
                return false;
            }
        }
        return true;
    }

    protected synchronized void messageTimedOut(MessageBPO message) {
        System.out.println("MESSAGE TIMED-OUT " + message.getId() + " for " + remoteContainerID);
        
        //If the message is in the pendingToReceiveACK list
        int posInPendingToReceiveACK = getMessagePosition(pendingToReceiveACK, message);
        if (posInPendingToReceiveACK != -1) {
            //remove it
            pendingToReceiveACK.remove(posInPendingToReceiveACK);
        }
        //If the message is in the pendingToSend list
        int posInPendingToSend = getMessagePosition(pendingToSend, message);
        if (posInPendingToSend != -1) {
            //remove it
            pendingToSend.remove(posInPendingToSend);
        }
        //If the message is in the pendingToSendWithPriority it is not eliminated
        if ((message instanceof BlockingMessageBPO)) {
            if (((BlockingMessageBPO) message).getAlwaysRetryToSendMessage()) {
                retrySendMessage(message);
            }
        }
    }

    private int getMessagePosition(List list, MessageBPO message) {
        for (int i = 0; i < list.size(); i++) {
            if (((MessageBPO) list.get(i)).getId().equals(message.getId())) {
                return i;
            }
        }
        return -1;
    }

    protected void setCommunicationChannel(ICommunicationChannelBPO communicationChannel) {
        mailingService.setCommunicationChannel(communicationChannel);
    }

    public boolean isEmpty() {
        return pendingToSend.isEmpty() && pendingToSendWithPriority.isEmpty() && pendingToReceiveACK.isEmpty();
    }

    public String getRemoteContainerID() {
        return remoteContainerID;
    }

    protected void communicationProblemDetected() {
        System.out.println("SE DETECTO PROBLEMA COMUNICACION WITH " + getRemoteContainerID() + " retry " + retryConnectionWhenProblemDetected + "\n");
        if (retryConnectionWhenProblemDetected && (!isEmpty() || attemptingSend)) {
           ((IAdmBPO)AdmBESA.getInstance()).getPostOffice().connectTo(getRemoteContainerID());
        }
    }

    protected synchronized void shutDown() {
        shutdownMessageQueues();
        mailingService.shutdown();
    }

    private void shutdownMessageQueues() {
        String errorMessage = "MailBox ShutDown";

        for (int i = 0; i < pendingToSend.size(); i++) {
            if (pendingToSend.get(i) instanceof BlockingMessageBPO) {
                ACKMessageBPO negativeACK = MessageFactoryBPO.createNegativeACK(pendingToSend.get(i).getId(), errorMessage);
                ((BlockingMessageBPO) pendingToSend.get(i)).setACK(negativeACK);
            }
        }

        for (int i = 0; i < pendingToSendWithPriority.size(); i++) {
            if (pendingToSendWithPriority.get(i) instanceof BlockingMessageBPO) {
                ACKMessageBPO negativeACK = MessageFactoryBPO.createNegativeACK(pendingToSendWithPriority.get(i).getId(), errorMessage);
                ((BlockingMessageBPO) pendingToSendWithPriority.get(i)).setACK(negativeACK);
            }
        }

        for (int i = 0; i < pendingToReceiveACK.size(); i++) {
            ACKMessageBPO negativeACK = MessageFactoryBPO.createNegativeACK(pendingToReceiveACK.get(i).getId(), errorMessage);
            pendingToReceiveACK.get(i).setACK(negativeACK);
        }

        pendingToSend.clear();
        pendingToSendWithPriority.clear();
        pendingToReceiveACK.clear();
    }
}
