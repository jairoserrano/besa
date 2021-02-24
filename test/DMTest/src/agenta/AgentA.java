/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenta;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;

/**
 *
 * @author SAR
 */
public class AgentA extends AgentBESA {

    public AgentA(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {
    }
}
