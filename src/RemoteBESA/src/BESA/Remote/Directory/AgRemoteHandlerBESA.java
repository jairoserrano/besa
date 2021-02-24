/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Remote.Directory;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AGENTSTATE;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Config.ConfigBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author SAR
 */
public class AgRemoteHandlerBESA extends AgHandlerBESA {

    /**
     *
     */
    private RemoteAdmHandlerBESA admRemoteHandler;	//solo para remoto RMI
    /**
     *
     */
    private ConfigBESA config;

    /**
     * Creates the remote agent handler.
     *
     * @param admH Administrator remote handler.
     * @param alias Agent alias.
     * @param agId Agent ID.
     * @param location Agent location.
     */
    public AgRemoteHandlerBESA(RemoteAdmHandlerBESA admH, String alias, String agId) {
        super(agId, alias);
        admRemoteHandler = admH;
        this.config = AdmBESA.getInstance().getConfigBESA();
    }

    /**
     * Builds a new agent remote handler associated to an administrator remote
     * handler.
     *
     * @param admRemoteHandler Administrator remote handler.
     */
    public AgRemoteHandlerBESA(RemoteAdmHandlerBESA admRemoteHandler) throws DistributedDirectoryExceptionBESA {
        super();
        this.admRemoteHandler = admRemoteHandler;
        this.config = AdmBESA.getInstance().getConfigBESA();
    }

    @Override
    public void sendEvent(EventBESA ev) throws ExceptionBESA {
        int i = 0;
        for (i = 0; i < this.config.getSendEventAttemps(); i++) {
            try {
                if (state == AGENTSTATE.KILL) {
                    ReportBESA.error("The remote agent has the kill state.");
                    throw new DistributedDirectoryExceptionBESA("The remote agent has the kill state.");
                } else if (state == AGENTSTATE.MOVE) {
                    //				this.wait();
                    int iii = 1 +2;
                }
                this.admRemoteHandler.getAdmRemote().sendEvent(ev, agId);
                i = 10;
            } catch (Exception e) {
                try {
                    this.wait(this.config.getSendEventTimeout());
                } catch (Exception e1) {
                    ReportBESA.error("Happened a error into the wait time: " + e1.getMessage());
                    throw new DistributedDirectoryExceptionBESA("Happened a error into the wait time: " + e1.getMessage());
                }
            }
        }
        if (i == this.config.getSendEventAttemps() - 1) {
            ReportBESA.error("Event non-delivery.");
            throw new DistributedDirectoryExceptionBESA("Event non-delivery.");
        }
    }

    /**
     * Updates the adminitrator remote handler instance.
     *
     * @param admRemoteHandler The new administrator remote handler.
     */
    public void update(RemoteAdmHandlerBESA admRemoteHandler) {
        this.admRemoteHandler = admRemoteHandler;
    }

    /**
     * Obtains a reference of the administrator remote handler.
     *
     * @return The administrator remote handler associated.
     */
    public RemoteAdmHandlerBESA getAdm() {
        return this.admRemoteHandler;
    }

    public void setAdmRemoteHandler(RemoteAdmHandlerBESA admRemoteHandler) {
        this.admRemoteHandler = admRemoteHandler;
    }
}
