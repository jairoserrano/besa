/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WorkerAgent;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author jairo
 */
public class WorkerAgent extends AgentBESA {

    public WorkerAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
        ReportBESA.debug("Created " + this.getAlias());
    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {
        ReportBESA.debug("Closing " + this.getAlias());
    }
}
