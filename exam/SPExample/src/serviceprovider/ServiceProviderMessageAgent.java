/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serviceprovider;

import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.Social.ServiceProvider.agent.ServiceProviderBESA;
import BESA.Kernel.Social.ServiceProvider.agent.StateServiceProvider;

/**
 *
 * @author fabianjose
 */
public class ServiceProviderMessageAgent extends ServiceProviderBESA {

    public static String SERVICEONDIRECTORY = "messaging";

    public ServiceProviderMessageAgent(String alias, StateServiceProvider state, StructBESA structAgent, double passwd) throws KernellAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
        this.getAdmLocal().bindSPServiceInDirectory(this.getAid(), ServiceProviderMessageAgent.SERVICEONDIRECTORY);
    }
}
