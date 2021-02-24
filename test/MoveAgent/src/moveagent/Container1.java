/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moveagent;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import agenta.AgentA;
import agenta.AgentAGuard;
import agenta.AgentAState;
import agentb.AgentB;
import agentb.AgentBGuard;
import agentb.AgentBState;
import java.util.Enumeration;

/**
 *
 * @author fabianjose
 */
public class Container1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //--------------------------------------------------------------------//
        // Creates and starts the BESA container.                             //
        //--------------------------------------------------------------------//
        AdmBESA adm = AdmBESA.getInstance("res/Container1.xml");
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        AgentAState agentAState = new AgentAState();
        StructBESA agentAstruct = new StructBESA();
        agentAstruct.bindGuard(AgentAGuard.class);
        AgentA agentA = null;
        try {
            agentA = new AgentA("AgentA", agentAState, agentAstruct, 0.71);
        } catch (KernelAgentExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        AgentBState agentBState = new AgentBState();
        StructBESA agentBstruct = new StructBESA();
        agentBstruct.bindGuard(AgentBGuard.class);
        AgentB agentB = null;
        try {
            agentB = new AgentB("AgentB", agentBState, agentBstruct, 0.73);
        } catch (KernelAgentExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        agentB.start();
        agentA.start();
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        delay(3);
        try {
            ReportBESA.info("========================");
            ReportBESA.info("= The agent is moving. =");
            ReportBESA.info("========================");
            adm.moveAgent("AgentB", "Container2", 0.73);
            ReportBESA.info("==================================");
            ReportBESA.info("= The agent moved to Container2. =");
            ReportBESA.info("==================================");
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        delay(1);
        try {
            ReportBESA.info("========================");
            ReportBESA.info("= The agent is moving. =");
            ReportBESA.info("========================");
            adm.moveAgent("AgentB", "Container3", 0.73);
            ReportBESA.info("==================================");
            ReportBESA.info("= The agent moved to Container3. =");
            ReportBESA.info("==================================");
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        delay(10);
        ReportBESA.info("==================================");
        ReportBESA.info("Containers:");
        Enumeration<String> containers = AdmBESA.getInstance().getAdmAliasList();
        while(containers.hasMoreElements()){
            ReportBESA.info(containers.nextElement());    
        }
        ReportBESA.info("==================================");
    }

    /**
     * 
     * @param i 
     */
    private static void delay(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException ex) {
            ReportBESA.error(ex);
        }
    }
}
