/*
 * @(#)BehaviorBAP.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern.OutputBAP;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.BehaviorBESA;

/**
 * This class represents the BAP behavior.
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class BehaviorBAP extends BehaviorBESA {

    /**
     * Creates a new instance.
     *
     * @param ag BESA Agent.
     */
    public BehaviorBAP(AgentBESA ag) {
        super(ag);
    }

    /**
     * Creates a new instance.
     *
     * @param ag BAP agent.
     */
    public BehaviorBAP(AgentBAP ag) {
        super((AgentBESA) ag);
    }

    /**
     * Setup behavior.
     */
    protected void setupBehavior() {
    }

    /**
     * Shutdown behavior.
     */
    protected void shutdownBehavior() {
    }
}
