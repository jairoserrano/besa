/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura.lugares.muro.agente;

import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import wpsMain.agents.peasant.LugarAgent;


/**
 *
 * @author Daniel
 */
public class MuroAgent extends LugarAgent{
    
    public MuroAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
        
    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
