package BESA.Mobile.CommunicationChannel;

import BESA.Kernel.System.AdmBESA;
import BESA.Mobile.CommunicationChannel.BaseClasses.ServerBPO;
import BESA.Mobile.IAdmBPO;

/**
 *
 * @author Andrea
 */
public class ServerConnectionListenerBPO extends Thread {

    private boolean alive;
    private ServerBPO server;

    public ServerConnectionListenerBPO(ServerBPO server) {
        this.alive = true;
        this.server = server;
    }

    @Override
    public synchronized void run() {
        while (alive) {
            try {
                ICommunicationChannelBPO communicationChannel = server.accept();
                if (alive) {
                    ((IAdmBPO)AdmBESA.getInstance()).getPostOffice().acceptConnection(communicationChannel);
                }
            } catch (Exception e) {
            }
        }
    }

    public void kill() {
        alive = false;
        server.close();
    }

    public int getPort() {
        return server.getPort();
    }
}
