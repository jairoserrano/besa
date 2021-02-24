/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobiledevicetest;

import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import agenta.AgentA;
import agenta.AgentAGuard;
import agenta.AgentAState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabianjose
 */
public class MobileDeviceTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String name = "DM";
        System.out.println("THIS IS " + name + " \n\n\n");
        AdmBESA.getInstance();

        //*****************************************************************

        //CREAR AGENTE

        //----------------------------------------------------------------//
        StructBESA structAgent = new StructBESA();
        structAgent.bindGuard(AgentAGuard.class);
        AgentA agentA;
        try {
            agentA = new AgentA("AgentA", new AgentAState(), structAgent, 77.77);
            agentA.start();
            System.out.println("SE CREO AGENTE " + agentA.getAlias() + " CON ID " + agentA.getAid());
        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(MobileDeviceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        //----------------------------------------------------------------//
    }
}
