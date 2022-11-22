/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import wpsMain.util.Mensaje;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class CrearClienteGuard extends GuardBESA{
    
    @Override
    public void funcExecGuard(EventBESA ebesa) {
        String clienteAlias=(String)((Mensaje)ebesa.getData()).get();
        try{
            EntradaState entrada=(EntradaState)MapaEstructural.getInstance().obtenerLugarAleatorio(LugarState.ENTRADA);
            AgHandlerBESA agHandler = AdmBESA.getInstance().getHandlerByAlias(entrada.getAlias());
            ArrayList parametrosMensaje=new ArrayList();
            parametrosMensaje.add(clienteAlias);
            parametrosMensaje.add(null);
            Mensaje mensaje=new Mensaje(parametrosMensaje);
            EventBESA eventBesa = new EventBESA(AgregarClienteGuard.class.getName(),mensaje);
            agHandler.sendEvent(eventBesa);
        }catch(Exception e){
            LogAuditoria.getInstance().escribirError(this.getClass().getName(), this.getAgent().getAlias(), e);
        }
    }
    
}
