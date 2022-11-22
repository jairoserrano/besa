/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura.lugares.pasillo.agente;

import BESA.Kernel.Agent.StructBESA;
import wpsMain.agents.peasant.Producto;
import wpsMain.agents.peasant.MensajePromocional;
import SIMALL.modelo.log.LogAuditoria;
import wpsMain.agents.peasant.LugarState;
import wpsMain.agents.peasant.AgregarClienteGuard;
import wpsMain.agents.peasant.EliminarClienteGuard;
import wpsMain.agents.peasant.ComercioState;
import SIMALL.modelo.cc.infraestructura.lugares.pasillo.agente.PasilloAgent;
import wpsMain.util.DistribucionProbabilidadTriangular;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author dsvalencia
 */
public class PasilloState extends LugarState{
    
    private ArrayList<ComercioState> comerciosImpulsados=new ArrayList();
    
    public PasilloState(String alias, String categoria, DistribucionProbabilidadTriangular tiempoServicio, DistribucionProbabilidadTriangular valorProductos, DistribucionProbabilidadTriangular ventas, int x, int y, int unidades) {
        super(alias, categoria, tiempoServicio, valorProductos, ventas, x, y, unidades);
        
        try{
            StructBESA pasilloStruct = new StructBESA();
            pasilloStruct.addBehavior("AgregarClienteGuard");
            pasilloStruct.bindGuard("AgregarClienteGuard",AgregarClienteGuard.class);                        
            pasilloStruct.addBehavior("EliminarClienteGuard");
            pasilloStruct.bindGuard("EliminarClienteGuard",EliminarClienteGuard.class);                        
            PasilloAgent pasilloAgent = new PasilloAgent(alias, this, pasilloStruct, 0.91); 
            pasilloAgent.start();
        }catch (Exception e) {
            LogAuditoria.getInstance().escribirError(this.getClass().getName(),alias,e);
        }
    }

    public void agregarComercioImpulsado(ComercioState comercio) {
        if(comercio!=null){
            comerciosImpulsados.add(comercio);
        }
    }
    
    @Override
    public ArrayList<MensajePromocional> getMensajesPromocionales() {
        ArrayList<MensajePromocional> mensajesPromocionales=null;
        if(comerciosImpulsados!=null){
            if(comerciosImpulsados.size()>0){
                mensajesPromocionales=new ArrayList();
                for(ComercioState comercio:comerciosImpulsados){
                    ArrayList<Producto> productosEnPromocion=comercio.getInventario().getProductosEnPromocion();
                    if(productosEnPromocion!=null){
                        if(productosEnPromocion.size()>0){
                            mensajesPromocionales.add(new MensajePromocional(comercio.getAlias(),productosEnPromocion));
                        }
                    }
                }
            }
        }
        return mensajesPromocionales;
    }
    
}
