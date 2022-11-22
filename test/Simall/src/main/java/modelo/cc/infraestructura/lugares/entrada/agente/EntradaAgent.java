/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura.lugares.entrada.agente;

import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarAgent;


/**
 *
 * @author Daniel
 */
public class EntradaAgent extends LugarAgent{
    
    public EntradaAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernellAgentExceptionBESA {
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
