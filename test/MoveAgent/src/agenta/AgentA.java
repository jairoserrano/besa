/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenta;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author fabianjose
 */
public class AgentA extends AgentBESA {

    public AgentA(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
        EventBESA eventToSend = new EventBESA(AgentAGuard.class.getName());
        try {
            AgHandlerBESA agh = AdmBESA.getInstance().getHandlerByAlias("AgentA");
            agh.sendEvent(eventToSend);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }

    @Override
    public void shutdownAgent() {
    }
}
