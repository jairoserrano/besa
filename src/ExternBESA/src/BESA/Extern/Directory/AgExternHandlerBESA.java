/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package BESA.Extern.Directory;

import BESA.Config.ConfigBESA;
import BESA.ExceptionBESA;
import BESA.Extern.BAP;
import BESA.Extern.OutputBAP.AgentBAP;
import BESA.Extern.OutputBAP.DataBAP;
import BESA.Extern.OutputBAP.GuardBAP;
import BESA.Kernel.Agent.AGENTSTATE;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author fabianjose
 */
public class AgExternHandlerBESA extends AgHandlerBESA {

    private String bPOLocation;
    private String aliasAgent;
    /**
     *
     */
    private ConfigBESA config;

    public AgExternHandlerBESA(ConfigBESA config, String bPOLocation, String agID, String aliasAgent) {
        super(agID, aliasAgent);
        this.bPOLocation = bPOLocation;
        this.aliasAgent = aliasAgent;
        this.config = config;
    }

    @Override
    public void sendEvent(EventBESA ev) throws ExceptionBESA {
        int i = 0;
        for (i = 0; i < this.config.getSendEventAttemps(); i++) {
            try {
                if (state == AGENTSTATE.KILL) {
                    ReportBESA.error("[AgHandlerBAP::sendEvent] Couldn't send the event because the agent " + this.aliasAgent + " has the kill state.");
                    throw new ExternDirectoryExceptionBESA("Couldn't send the event because the agent " + this.aliasAgent + " has the kill state.");
                } else if (state == AGENTSTATE.MOVE) {
                }
                AgHandlerBESA ah = AdmBESA.getInstance().getHandlerByAlias(AgentBAP.class.getName());
                
                DataBAP dataBAP;
                
                if(this.aliasAgent.contains("TempAlias")) {
                    dataBAP = new DataBAP(BAP.SEND_EVENT_BAP_BY_ID, ev, this.agId, bPOLocation);
                } else {
                    dataBAP = new DataBAP(BAP.SEND_EVENT_BAP_BY_ALIAS, ev, this.aliasAgent, bPOLocation);
                }
                
                EventBESA evBAP = new EventBESA(GuardBAP.class.getName(), dataBAP);
                ah.sendEvent(evBAP);
                i = 10;
            } catch (Exception e) {
                try {
                    this.wait(this.config.getSendEventTimeout());
                } catch (Exception ex) {
                    ReportBESA.error("[AgHandlerBAP::sendEvent] Happened a error into wait time: " + ex.toString());
                    throw new ExternDirectoryExceptionBESA("Happened a error into wait time: " + ex.toString());
                }
            }
        }
        if (i == this.config.getSendEventAttemps() - 1) {
        }
    }
}
