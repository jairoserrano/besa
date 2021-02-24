/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenta;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SAR
 */
public class AgentAGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {        
        try {
            //this.agent.getAdmLocal().getHandlerByAlias("AgentB").sendEvent(new EventBESA(AgentBGuard.class.getName(), event.getData()));
            ReportBESA.info("The agent A is saying hello to extent agent B...");
        } catch (Exception ex) {
            Logger.getLogger(AgentAGuard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
