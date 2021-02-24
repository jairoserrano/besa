/*
 * @(#)AgentBAP.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern.OutputBAP;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import java.io.Serializable;

/**
 * This class represents the agent BAP that is charge of the comunication
 * between neighborhoods.
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class AgentBAP extends AgentBESA implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     *
     * @param alias Agent alias.
     * @param state Agent state.
     * @param structAgent Agent struct.
     * @param passwd Agent password.
     */
    public AgentBAP(String alias, StateBAP state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, (StateBESA) state, structAgent, passwd);
    }

    /**
     * Shutdown agent.
     */
    public void shutdownAgent() {
    }

    /**
     * Setup agent.
     */
    public void setupAgent() {
    }
}
