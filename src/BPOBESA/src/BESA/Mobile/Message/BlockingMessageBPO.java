package BESA.Mobile.Message;

/**
 *
 * @author Andrea Barraza
 */
public abstract class BlockingMessageBPO extends MessageBPO {

    private ACKMessageBPO ACK;
    private boolean alwaysRetryToSendMessage;

    public BlockingMessageBPO(String id, MessageBPO.Type messageType) {
        super(id, messageType);
    }

    public ACKMessageBPO getACK() {
        return ACK;
    }

    public synchronized void setACK(ACKMessageBPO ACK) {
        this.ACK = ACK;
        notify();
    }

    public synchronized void waitForACK(long time) throws InterruptedException {
        wait(time);
    }

    public synchronized void waitForACK() throws InterruptedException {
        setAlwaysRetryToSendMessage(true); 
        wait();
    }

    public final boolean getAlwaysRetryToSendMessage() {
        return alwaysRetryToSendMessage;
    }

    protected final void setAlwaysRetryToSendMessage(boolean alwaysRetryToSendMessage) {
        this.alwaysRetryToSendMessage = alwaysRetryToSendMessage;
    }

    @Override
    abstract protected void buildMessage(String messageString);
}
