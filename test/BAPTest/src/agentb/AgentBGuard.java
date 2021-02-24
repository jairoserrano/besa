/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentb;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import agenta.SayHelloData;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SAR
 */
public class AgentBGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        ReportBESA.info("The agent BAPTestPeer recieves: " + ((SayHelloData)event.getData()).getMsg());
        try {
            AdmBESA.getInstance().killAgent(agent.getAid(), 77.77);
        } catch (ExceptionBESA ex) {
            Logger.getLogger(AgentBGuard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
