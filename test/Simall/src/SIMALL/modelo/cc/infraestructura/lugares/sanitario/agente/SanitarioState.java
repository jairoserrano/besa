/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura.lugares.sanitario.agente;

import BESA.Kernel.Agent.StructBESA;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;
import SIMALL.modelo.cc.infraestructura.lugar.agente.guardas.AgregarClienteGuard;
import SIMALL.modelo.cc.infraestructura.lugar.agente.guardas.EliminarClienteGuard;
import SIMALL.modelo.cc.infraestructura.lugares.sanitario.agente.SanitarioAgent;
import SIMALL.modelo.util.DistribucionProbabilidadTriangular;

/**
 *
 * @author dsvalencia
 */
public class SanitarioState extends LugarState{
    
    public SanitarioState(String alias, String categoria, DistribucionProbabilidadTriangular tiempoServicio, DistribucionProbabilidadTriangular valorProductos, DistribucionProbabilidadTriangular ventas, int x, int y, int unidades) {
        super(alias, categoria, tiempoServicio, valorProductos, ventas, x, y, unidades);
        
        try{
            StructBESA sanitarioStruct = new StructBESA();
            sanitarioStruct.addBehavior("AgregarClienteGuard");
            sanitarioStruct.bindGuard("AgregarClienteGuard",AgregarClienteGuard.class);                        
            sanitarioStruct.addBehavior("EliminarClienteGuard");
            sanitarioStruct.bindGuard("EliminarClienteGuard",EliminarClienteGuard.class);                        
            SanitarioAgent sanitarioAgent = new SanitarioAgent(alias, this, sanitarioStruct, 0.91); 
            sanitarioAgent.start();
        }catch (Exception e) {
            LogAuditoria.getInstance().escribirError(this.getClass().getName(),alias,e);
        }
    }

    
}
