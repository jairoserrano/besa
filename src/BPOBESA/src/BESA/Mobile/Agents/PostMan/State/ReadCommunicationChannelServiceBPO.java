package BESA.Mobile.Agents.PostMan.State;

/**
 *
 * @author Andrea Barraza
 */
public class ReadCommunicationChannelServiceBPO extends Thread {

    private MailingServiceBPO mailingService;
    private boolean isWaiting;
    private boolean alive;

    protected ReadCommunicationChannelServiceBPO(MailingServiceBPO mailingService) {
        this.mailingService = mailingService;
        this.isWaiting = false;
        this.alive = false;
    }

    @Override
    public void run() {
        while (alive) {
            if (!isWaiting) {
                mailingService.receiveMessage();
            } else {
                //Wait is done here to guarantee that the mailingService object is not blocked
                myWait();
            }
        }
    }

    private synchronized void myWait() {
        try {
            wait();
        } catch (InterruptedException ex) {
            //Ignore
        }
    }

    @Override
    public void start() {
        alive = true;
        super.start();
    }

    protected synchronized void standBy() {
        if (!isWaiting) {
            isWaiting = true;
        }
    }

    protected synchronized void carryOn() {
        if (isWaiting && alive) {
            isWaiting = false;
            this.notify();
        }
    }

    protected synchronized void kill() {
        if (alive) {
            alive = false;
            this.notify();
        }
    }

    
}
