/*
 * @(#)Main.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved. Takina
 * and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package baptest;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import agenta.AgentA;
import agenta.AgentAGuard;
import agenta.AgentAState;
import agenta.SayHelloData;

/**
 * This class represents the start point of the application.
 *
 * @author SIDRe - Pontificia Universidad Javeriana
 * @author Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since JDK1.0
 */
public class AgentAMain {

    /**
     * Administrator of the local BESA container.
     */
    private static AdmBESA admLocal;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        admLocal = AdmBESA.getInstance("res/Container1.xml");
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        ReportBESA.info(admLocal.getAdmHandler().getAlias() + " [OK].");
        //----------------------------------------------------------------//
        //
        //----------------------------------------------------------------//
        StructBESA structAgent = new StructBESA();
        structAgent.bindGuard(AgentAGuard.class);
        try {
            AgentA agentA = new AgentA("AgentA", new AgentAState(), structAgent, 77.77);
            agentA.start();
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        //----------------------------------------------------------------//
        //
        //----------------------------------------------------------------//
        ReportBESA.info("Start [OK].");
        try {
            for (int index = 0; index < 1; index++) {
                SayHelloData sayHelloData = new SayHelloData("Hello[" + index + "]");
                admLocal.getHandlerByAlias("AgentA").sendEvent(new EventBESA(AgentAGuard.class.getName(), sayHelloData));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ReportBESA.error(ex);
                }
            }
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
