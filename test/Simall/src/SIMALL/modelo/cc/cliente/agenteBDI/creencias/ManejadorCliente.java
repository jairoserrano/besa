/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.creencias;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;
import SIMALL.modelo.cc.infraestructura.lugar.agente.guardas.AgregarClienteGuard;
import SIMALL.modelo.util.Mensaje;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class ManejadorCliente {

    private ArrayList<LugarState> rutaHaciaLugarObjetivo;
    
    private final String aliasCliente;

    public ManejadorCliente(ManejadorCliente manejadorCliente) {
        this.rutaHaciaLugarObjetivo=null;
        if(manejadorCliente.rutaHaciaLugarObjetivo!=null){
            this.rutaHaciaLugarObjetivo = new ArrayList(manejadorCliente.rutaHaciaLugarObjetivo);
        }
        
        this.aliasCliente=manejadorCliente.aliasCliente;
    }
    
    public void reiniciar(){
        this.rutaHaciaLugarObjetivo = null;
    }

    
    public ManejadorCliente(String aliasCliente) {
        this.rutaHaciaLugarObjetivo = null;
        this.aliasCliente=aliasCliente;
    }
    
    public void definirRutaHaciaLugarEspecífico(LugarState lugarEspecifico) {
        if (rutaHaciaLugarObjetivo == null) {
            rutaHaciaLugarObjetivo = MapaEstructural.getInstance().getRutaMasCortaALugarEspecifico(aliasCliente, lugarEspecifico);
        }
    }
    
    public void avanzarHaciaLugarObjetivo() {
        if(rutaHaciaLugarObjetivo!=null){
            if(rutaHaciaLugarObjetivo.size()>1){
                LugarState lugarActual=rutaHaciaLugarObjetivo.get(0);
                LugarState proximoLugar=rutaHaciaLugarObjetivo.get(1);
                try{
                    AgHandlerBESA agHandler = AdmBESA.getInstance().getHandlerByAlias(proximoLugar.getAlias());
                    ArrayList parametrosMensaje=new ArrayList();
                    parametrosMensaje.add(aliasCliente);
                    parametrosMensaje.add(lugarActual.getAlias());
                    Mensaje mensaje=new Mensaje(parametrosMensaje);EventBESA eventBesa = new EventBESA(AgregarClienteGuard.class.getName(),mensaje);
                    agHandler.sendEvent(eventBesa);
                    rutaHaciaLugarObjetivo.remove(lugarActual);
                }catch(Exception e){
                    LogAuditoria.getInstance().escribirError(this.getClass().getName(), aliasCliente, e);
                }
            }
        }
        
    }

    public boolean haLLegadoALugarObjetivo() {
        if(rutaHaciaLugarObjetivo!=null){
            if(rutaHaciaLugarObjetivo.size()==1){
                rutaHaciaLugarObjetivo=null;
                return true;
            }
        }
        return false;
    }

    public String getAliasLugarObjetivo() {
        if(rutaHaciaLugarObjetivo!=null){
            return rutaHaciaLugarObjetivo.get(rutaHaciaLugarObjetivo.size()-1).getAlias();
        }
        return null;
    }

    public boolean tieneRutaDefinida() {
        return rutaHaciaLugarObjetivo!=null;
    }

}
