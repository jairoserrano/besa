package BESA.Mobile.Agents.PostMan;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Mobile.Agents.PostMan.State.PostManAgentStateBPO;

/**
 *
 * @author Andrea Barraza
 */
public class PostManAgentBPO extends AgentBESA {

    public PostManAgentBPO(String alias, PostManAgentStateBPO state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {
        ((PostManAgentStateBPO)this.state).shutdownMailBox();
    }
}
