/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkAgent;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Log.ReportBESA;
import Utils.BenchmarkConfig;

/**
 *
 * @author jairo
 */
public class AgentMasterBase extends AgentBESA {

    BenchmarkConfig config = BenchmarkConfig.getConfig();
    long startTime;

    public AgentMasterBase(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {
        ReportBESA.info("Tiempo de ejecución total: " + (System.currentTimeMillis() - this.startTime) / 1000000);
        System.exit(0);
    }
}
