package BESA.Mobile.Agents.PostMan.State;

import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.Message.MessageBPO;

/**
 *
 * @author Andrea Barraza
 */
public final class MailingServiceBPO {

    private MailBoxBPO mailBox;
    private boolean mailingServiceEnabled;
    private ReadCommunicationChannelServiceBPO readCommunicationChannelService;
    private ICommunicationChannelBPO communicationChannel;

    protected MailingServiceBPO(MailBoxBPO mailBox) {
        this.mailBox = mailBox;
        this.mailingServiceEnabled = false;
        initReadCommunicationChannelService();
    }

    private void initReadCommunicationChannelService() {
        //Because at this point their is no communication channel the readCommunicationChannelService must 
        //be set to connectionInterrupted state
        this.readCommunicationChannelService = new ReadCommunicationChannelServiceBPO(this);
        readCommunicationChannelService.standBy();
        readCommunicationChannelService.start();
    }

    protected void setCommunicationChannel(ICommunicationChannelBPO communicationChannel) {
        disable();
        this.communicationChannel = communicationChannel;
        readCommunicationChannelService.carryOn();
        mailingServiceEnabled = true;
        if (!mailBox.sendAllUnsentMessages()) {
            communicationProblemDetected();
        }
    }

    protected boolean sendMessage(MessageBPO message) {
        if (isEnabled()) {
            try {
                communicationChannel.write(message);
                return true;
            } catch (ExceptionBESACommunicationChannelFailedBPO ex) {
            }
        }
        communicationProblemDetected();
        return false;
    }

    protected void receiveMessage() {
        if (isEnabled()) {
            try {
                MessageBPO message = communicationChannel.read();
                if (message == null) {
                    communicationProblemDetected();
                } else {
                    mailBox.receiveMessage(message);
                }
            } catch (ExceptionBESACommunicationChannelFailedBPO ex) {
                communicationProblemDetected();
            } catch (ExceptionBESAMessageInterpreterFailedBPO ex) {
                //Message Ignored if it can't be interpreted correctly
            } catch (NullPointerException ex) {
                communicationProblemDetected();
            } catch (Exception ex) {
                communicationProblemDetected();
            }
        }
    }

    protected void communicationProblemDetected() {
        if (!isWorking()) {
            disable();
            mailBox.communicationProblemDetected();
        }
    }

    private synchronized boolean isWorking() { //At least one communication channel is open
        if (isEnabled()) {
            return !communicationChannel.isClosed();
        }
        return false;
    }

    private boolean isEnabled() {
        if (!mailingServiceEnabled || communicationChannel == null) {
            return false;
        }
        return true;
    }

    protected synchronized void disable() {
        //Tries to securely disconnect the communication channel
        mailingServiceEnabled = false;
        readCommunicationChannelService.standBy();

        if (communicationChannel != null) {
            communicationChannel = null; //Connections Administrator would be in charge of closing the channel
        }
        System.out.println("Mailing Service for "    + mailBox.getRemoteContainerID() + " DISABLED");
    }

    protected void shutdown() {
        disable();
        readCommunicationChannelService.kill();
        System.out.println("Mailing Service for " + mailBox.getRemoteContainerID() + " SHUTDOWN");
    }
}
