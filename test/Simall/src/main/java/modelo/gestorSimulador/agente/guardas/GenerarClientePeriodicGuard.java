/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.gestorSimulador.agente.guardas;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.gestorSimulador.agente.GestorSmuladorState;
import SIMALL.modelo.cc.agente.guardas.CrearClienteGuard;
import SIMALL.modelo.cc.cliente.agenteBDI.ClienteBDIAgent;
import SIMALL.modelo.util.Mensaje;

/**
 *
 * @author Daniel
 */
public class GenerarClientePeriodicGuard extends PeriodicGuardBESA{
    
    
    @Override
    public void funcPeriodicExecGuard(EventBESA event) {
        try{
            if(((GestorSmuladorState)this.getAgent().getState()).topeClientes()){
                String clienteAlias=((GestorSmuladorState)this.getAgent().getState()).crearCliente();
                ClienteBDIAgent clienteBDIAgent=new ClienteBDIAgent(clienteAlias);
                clienteBDIAgent.start();
                clienteBDIAgent.startTimers();
                Mensaje mensaje=new Mensaje(clienteAlias);
                AgHandlerBESA agHandler = AdmBESA.getInstance().getHandlerByAlias("CentroComercialAgent");
                EventBESA eventBesa = new EventBESA(CrearClienteGuard.class.getName(),mensaje);
                agHandler.sendEvent(eventBesa);
            }
        }catch(Exception e){
            LogAuditoria.getInstance().escribirError(this.getClass().getName(), this.getAgent().getAlias(), e);
        }
    }
    
}
