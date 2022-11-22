/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura.lugar.agente.guardas;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Util.PeriodicDataBESA;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.IniciarLogroDeMetasGuard;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.MensajePromocional;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.PaqueteMensajesPromocionales;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.RecibirMensajesPromocionalesGuard;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarAgent;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;
import SIMALL.modelo.util.Mensaje;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class AgregarClienteGuard extends GuardBESA{
    
    @Override
    public void funcExecGuard(EventBESA ebesa) {
        try{
            ArrayList parametrosMensaje=(ArrayList)((Mensaje)ebesa.getData()).get();
            String aliasCliente=(String)parametrosMensaje.get(0);
            String aliasLugarAnterior=(String)parametrosMensaje.get(1);
            ((LugarAgent)this.getAgent()).agregarCliente(aliasCliente);
            int profundidad=2;
            ArrayList<LugarState> lugaresAlRededor=MapaEstructural.getInstance().getLugaresRededor((LugarState)((LugarAgent)this.getAgent()).getState(),profundidad);
            ArrayList<ArrayList<MensajePromocional>> totalMensajesPromocionales=new ArrayList();
            if(lugaresAlRededor!=null){
                //System.out.println("*********PUNTO ORIGEN "+((LugarState)((LugarAgent)this.getAgent()).getState()).getAlias());
                for(LugarState lugar:lugaresAlRededor){
                    //System.out.println("*********PUNTOS ALREDEDOR "+lugar.getAlias());
                    ArrayList<MensajePromocional> mensajesPromocionales=lugar.getMensajesPromocionales();
                    if(mensajesPromocionales!=null){
                        totalMensajesPromocionales.add(mensajesPromocionales);
                    }
                }
            }
            if(totalMensajesPromocionales.size()>0){
                PaqueteMensajesPromocionales paqueteMensajesPromocionales=new PaqueteMensajesPromocionales(totalMensajesPromocionales);
                AgHandlerBESA agHandler = AdmBESA.getInstance().getHandlerByAlias(aliasCliente);
                EventBESA eventBesa = new EventBESA(RecibirMensajesPromocionalesGuard.class.getName(),paqueteMensajesPromocionales);
                agHandler.sendEvent(eventBesa);
            }
            
            if(aliasLugarAnterior!=null){
                Mensaje mensaje=new Mensaje(aliasCliente);
                AgHandlerBESA agHandler = AdmBESA.getInstance().getHandlerByAlias(aliasLugarAnterior);
                EventBESA eventBesa = new EventBESA(EliminarClienteGuard.class.getName(),mensaje);
                agHandler.sendEvent(eventBesa);
            }
            else{
                PeriodicDataBESA data = new PeriodicDataBESA(10, 10, PeriodicGuardBESA.START_PERIODIC_CALL);
                AgHandlerBESA agHandlerCliente = AdmBESA.getInstance().getHandlerByAlias(aliasCliente);
                EventBESA eventBesaCliente = new EventBESA(IniciarLogroDeMetasGuard.class.getName(),data);
                agHandlerCliente.sendEvent(eventBesaCliente);
            }
        }catch(Exception e){
            LogAuditoria.getInstance().escribirError(this.getClass().getName(), this.getAgent().getAlias(), e);
        }
    }
}
