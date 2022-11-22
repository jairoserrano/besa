/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;


/**
 *
 * @author Daniel
 */
public class LugarAgent extends AgentBESA{
    
    public LugarAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void agregarCliente(String aliasCliente) {
        MapaEstructural mapa=MapaEstructural.getInstance();
        mapa.moverCliente(aliasCliente,(LugarState)this.getState());
        ((LugarState)this.getState()).agregarCliente(aliasCliente);
    }
}
