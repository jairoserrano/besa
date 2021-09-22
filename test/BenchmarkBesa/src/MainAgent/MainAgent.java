/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientAgent;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author jairo
 */
public class ClientAgent extends AgentBESA {

    private long startTime;

    public ClientAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
        startTime = System.currentTimeMillis();
    }

    /**
     *
     */
    @Override
    public void shutdownAgent() {
        ReportBESA.info("Closing " + this.getAlias());
        ReportBESA.info("Total Execution time: " + (System.currentTimeMillis() - this.startTime));
        //System.gc();
    }

    @Override
    public void setupAgent() {
    }

}
