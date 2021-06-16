/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura.lugares.puntoInformacion.agente;

import BESA.Kernel.Agent.StructBESA;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;
import SIMALL.modelo.cc.infraestructura.lugar.agente.guardas.AgregarClienteGuard;
import SIMALL.modelo.cc.infraestructura.lugar.agente.guardas.EliminarClienteGuard;
import SIMALL.modelo.cc.infraestructura.lugares.puntoInformacion.agente.PuntoDeInformacionAgent;
import SIMALL.modelo.util.DistribucionProbabilidadTriangular;

/**
 *
 * @author dsvalencia
 */
public class PuntoDeInformacionState extends LugarState{
    
    public PuntoDeInformacionState(String alias, String categoria, DistribucionProbabilidadTriangular tiempoServicio, DistribucionProbabilidadTriangular valorProductos, DistribucionProbabilidadTriangular ventas, int x, int y, int unidades) {
        super(alias, categoria, tiempoServicio, valorProductos, ventas, x, y, unidades);
        
        try{
            StructBESA puntoInformacionStruct = new StructBESA();
            puntoInformacionStruct.addBehavior("AgregarClienteGuard");
            puntoInformacionStruct.bindGuard("AgregarClienteGuard",AgregarClienteGuard.class);                        
            puntoInformacionStruct.addBehavior("EliminarClienteGuard");
            puntoInformacionStruct.bindGuard("EliminarClienteGuard",EliminarClienteGuard.class);                        
            PuntoDeInformacionAgent puntoInformacionAgent = new PuntoDeInformacionAgent(alias, this, puntoInformacionStruct, 0.91); 
            puntoInformacionAgent.start();
        }catch (Exception e) {
            LogAuditoria.getInstance().escribirError(this.getClass().getName(),alias,e);
        }
    }
    
}
