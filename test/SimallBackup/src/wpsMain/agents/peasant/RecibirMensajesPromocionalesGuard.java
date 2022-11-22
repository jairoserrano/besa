/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import wpsMain.agents.peasant.ClienteBDIAgent;
import wpsMain.agents.peasant.ClienteBDIBelieves;

/**
 *
 * @author Daniel
 */
public class RecibirMensajesPromocionalesGuard extends GuardBESA{
    
    @Override
    public void funcExecGuard(EventBESA event) {
        ((ClienteBDIBelieves)((ClienteBDIAgent)this.getAgent()).getBelieves()).procesarMensajesPromocionales((PaqueteMensajesPromocionales)event.getData());
    }
    
}
