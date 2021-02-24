package BESA.Mobile.CommunicationChannel;

import BESA.Mobile.CommunicationChannel.BaseClasses.CommunicationChannelBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;

/**
 *
 * @author Andrea Barraza
 */
public class RepairConnectionServiceBPO extends Thread {

    private final int retries = 10; 
    private CommunicationChannelBPO client;
    private boolean alive;
    private int attempts;

    public RepairConnectionServiceBPO(CommunicationChannelBPO clientSocket) {
        this.client = clientSocket;
        alive = true;
    }

    @Override
    public synchronized void run() {
        attempts = 1;                    
        while (client.isClosed() && alive) {
            try {
                client.connect();
                if (attempts >= retries) {
                    kill();
                }else{
                    attempts++;
                }
            } catch (ExceptionBESACommunicationChannelFailedBPO ex) {
            }
        }
        client.wakeUp();
    }

    public void kill() {
        alive = false;
    }

    public boolean getIsRepaired() {
        return !(attempts >= retries);
    }
}
