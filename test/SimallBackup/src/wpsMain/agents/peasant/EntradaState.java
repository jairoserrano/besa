/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.Kernel.Agent.StructBESA;
import wpsMain.agents.peasant.LugarState;
import wpsMain.agents.peasant.AgregarClienteGuard;
import wpsMain.agents.peasant.EliminarClienteGuard;
import SIMALL.modelo.log.LogAuditoria;
import wpsMain.util.DistribucionProbabilidadTriangular;

/**
 *
 * @author Daniel
 */
public class EntradaState extends LugarState {

    public EntradaState(String alias, String categoria, DistribucionProbabilidadTriangular tiempoServicio, DistribucionProbabilidadTriangular valorProductos, DistribucionProbabilidadTriangular ventas, int x, int y, int unidades) {
        super(alias, categoria, tiempoServicio, valorProductos, ventas, x, y, unidades);
        try{
            StructBESA entradaStruct = new StructBESA();
            entradaStruct.addBehavior("AgregarClienteGuard");
            entradaStruct.bindGuard("AgregarClienteGuard",AgregarClienteGuard.class);                        
            entradaStruct.addBehavior("EliminarClienteGuard");
            entradaStruct.bindGuard("EliminarClienteGuard",EliminarClienteGuard.class);                        
            EntradaAgent entradaAgent = new EntradaAgent(alias, this, entradaStruct, 0.91); 
            entradaAgent.start();
        }catch (Exception e) {
            LogAuditoria.getInstance().escribirError(this.getClass().getName(),alias,e);
        }
    }
    
}
