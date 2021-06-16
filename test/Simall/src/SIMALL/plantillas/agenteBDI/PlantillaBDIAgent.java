/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.plantillas.agenteBDI;

import BESA.BDI.AgentStructuralModel.Agent.AgentBDI;
import BESA.BDI.AgentStructuralModel.GoalBDI;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import java.util.List;
import rational.mapping.Believes;

/**
 *
 * @author Daniel
 */
public class PlantillaBDIAgent extends AgentBDI{
    
    public PlantillaBDIAgent(String alias, Believes believes, List<GoalBDI> goals, double threshold, StructBESA structBESA) throws KernellAgentExceptionBESA, ExceptionBESA {
        super(alias, believes, goals, threshold, structBESA);
    }

    @Override
    public void setupAgentBDI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void shutdownAgentBDI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
