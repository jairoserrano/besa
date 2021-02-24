/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenta;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import agentb.AgentBGuard;
import common.Message;

/**
 *
 * @author fabianjose
 */
public class AgentAGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        int x;
        for (x = 1; x <= 7000; x++) {
            Message message = new Message("" + x);
            EventBESA eventToSend = new EventBESA(AgentBGuard.class.getName(), message);
            try {
                AgHandlerBESA agh = AdmBESA.getInstance().getHandlerByAlias("AgentB");
                agh.sendEvent(eventToSend);
                ReportBESA.debug("Send " + x + " to agent B.");
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }
        x--;
        ReportBESA.debug("");
        ReportBESA.info("============================================");
        ReportBESA.info("= The agent A send " + x + " events to agent B. =");
        ReportBESA.info("============================================");
    }
}
