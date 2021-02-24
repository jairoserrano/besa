/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Remote.Balancer;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;

/**
 *
 * @author bitas
 */
public class Ag_Balancer extends AgentBESA {

    /**
     *
     * @param alias
     * @param state
     * @param structAgent
     * @param passwd
     * @throws KernelAgentExceptionBESA
     */
    public Ag_Balancer(String alias, StateBESA state, StructBESA structAgent,
            double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);

    }

    @Override
    public void setupAgent() {

    }

    @Override
    public void shutdownAgent() {

    }

}
