/*
 * @(#)AgentOdo.java 3.0	20/09/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package OdoAgent;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import java.util.ArrayList;

/**
 * This class represents the odometer agent.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 3.0, 20/09/11
 * @since   JDK1.4
 */
public class AgentOdo extends AgentBESA {

    private int INT_COUNTER;

    /**
     * Creates a new instance of AgentOdo.
     *
     * @param alias Agent alias.
     * @param state Agent state.
     * @param structOdo Agent StructBESA
     * @param passwd Password.
     * @throws BESA.Kernel.Agent.KernelAgentExceptionBESA
     */
    public AgentOdo(String alias, StateOdo state, StructBESA structOdo, double passwd) throws KernelAgentExceptionBESA {
        super(alias, (StateBESA) state, structOdo, passwd);
        this.INT_COUNTER = 0;
    }

    /**
     * Setups agent.
     */
    @Override
    public void setupAgent() {
        StateOdo stateOdo = (StateOdo) this.state;
        stateOdo.setCounter(new CounterOdo(INT_COUNTER));                       //Initialized the counter.
        stateOdo.setState(StateOdo.State.Init);  
        ArrayList<Integer> v = new ArrayList<>();                               //Sets the initial state.
        v.add(1);
        stateOdo.initState(v);
    }

    /**
     * shutdown agent.
     */
    @Override
    public void shutdownAgent() {
    }
}
