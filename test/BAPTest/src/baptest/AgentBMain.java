/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package baptest;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import agentb.AgentB;
import agentb.AgentBGuard;
import agentb.AgentBState;

/**
 *
 * @author fabianjose
 */
public class AgentBMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AdmBESA admLocal = AdmBESA.getInstance("res/Container2.xml");
        StructBESA structAgent = new StructBESA();
        structAgent.bindGuard(AgentBGuard.class);
        try {
            AgentB agentB = new AgentB("AgentB", new AgentBState(), structAgent, 77.77);
            agentB.start();
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        ReportBESA.info(admLocal.getAdmHandler().getAlias() + " [OK].");
    }
}
