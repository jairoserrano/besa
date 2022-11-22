/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Util.PeriodicDataBESA;
import SIMALL.modelo.log.LogAuditoria;

/**
 *
 * @author Daniel
 */
public class IniciarGeneracionGuard extends GuardBESA{
    
    @Override
    public void funcExecGuard(EventBESA ebesa) {
        try{
            PeriodicDataBESA data = new PeriodicDataBESA(1, PeriodicGuardBESA.START_PERIODIC_CALL);
            AgHandlerBESA agHandler = AdmBESA.getInstance().getHandlerByAlias(this.getAgent().getAlias());
            EventBESA eventBesa = new EventBESA(GenerarClientePeriodicGuard.class.getName(),data);
            agHandler.sendEvent(eventBesa);
        }catch(Exception e){
            LogAuditoria.getInstance().escribirError(this.getClass().getName(), this.getAgent().getAlias(), e);
        }   
    }
}
