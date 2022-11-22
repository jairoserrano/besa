/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura.lugares.muro.agente;

import BESA.Kernel.Agent.StructBESA;
import SIMALL.modelo.cc.agente.Producto;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.MensajePromocional;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;
import SIMALL.modelo.cc.infraestructura.lugar.agente.guardas.AgregarClienteGuard;
import SIMALL.modelo.cc.infraestructura.lugar.agente.guardas.EliminarClienteGuard;
import SIMALL.modelo.cc.infraestructura.lugares.comercio.agente.ComercioState;
import SIMALL.modelo.util.DistribucionProbabilidadTriangular;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author dsvalencia
 */
public class MuroState extends LugarState{
    
    private LugarState lugarAsociado=null;
    
    private boolean esVitrina;
    
    public boolean esVitrina(){
        return esVitrina;
    }

    public LugarState getLugarAsociado() {
        return lugarAsociado;
    }

    public void setLugarAsociado(LugarState lugarAsociado) {
        this.lugarAsociado = lugarAsociado;
    }
    
    public MuroState(String alias, String categoria, DistribucionProbabilidadTriangular tiempoServicio, DistribucionProbabilidadTriangular valorProductos, DistribucionProbabilidadTriangular ventas, int x, int y, int unidades, boolean esVitrina) {
        super(alias, categoria, tiempoServicio, valorProductos, ventas, x, y, unidades);
        this.esVitrina=true;
        try{
            StructBESA muroStruct = new StructBESA();
            muroStruct.addBehavior("AgregarClienteGuard");
            muroStruct.bindGuard("AgregarClienteGuard",AgregarClienteGuard.class);                        
            muroStruct.addBehavior("EliminarClienteGuard");
            muroStruct.bindGuard("EliminarClienteGuard",EliminarClienteGuard.class);                        
            MuroAgent muroAgent = new MuroAgent(alias, this, muroStruct, 0.91); 
            muroAgent.start();
        }catch (Exception e) {
            LogAuditoria.getInstance().escribirError(this.getClass().getName(),alias,e);
        }
    }
    
    
    public MuroState(String alias, String categoria, DistribucionProbabilidadTriangular tiempoServicio, DistribucionProbabilidadTriangular valorProductos, DistribucionProbabilidadTriangular ventas, int x, int y, int unidades, LugarState lugarAsociado, boolean esVitrina) {
        super(alias, categoria, tiempoServicio, valorProductos, ventas, x, y, unidades);
        this.esVitrina=esVitrina;
        this.lugarAsociado=lugarAsociado;
        try{
            StructBESA muroStruct = new StructBESA();
            muroStruct.addBehavior("AgregarClienteGuard");
            muroStruct.bindGuard("AgregarClienteGuard",AgregarClienteGuard.class);                        
            muroStruct.addBehavior("EliminarClienteGuard");
            muroStruct.bindGuard("EliminarClienteGuard",EliminarClienteGuard.class);                        
            MuroAgent muroAgent = new MuroAgent(alias, this, muroStruct, 0.91); 
            muroAgent.start();
        }catch (Exception e) {
            LogAuditoria.getInstance().escribirError(this.getClass().getName(),alias,e);
        }
    }
    
    @Override
    public ArrayList<MensajePromocional> getMensajesPromocionales() {
        if(this.lugarAsociado!=null){
            if(this.lugarAsociado.getClass().getName().equals(ComercioState.class.getName())){
                if(esVitrina){
                    ArrayList<Producto> productosEnPromocion=((ComercioState)this.lugarAsociado).getInventario().getProductosEnPromocion();
                    if(productosEnPromocion!=null){
                        if(productosEnPromocion.size()>0){
                            return new ArrayList(Arrays.asList(new MensajePromocional(lugarAsociado.getAlias(),productosEnPromocion)));
                        }
                    }
                }
            }
        }
        return null;
    }
    
    
}
