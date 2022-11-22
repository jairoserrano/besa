/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import wpsMain.agents.peasant.ClienteBDIAgent;

/**
 *
 * @author Daniel
 */
public class IniciarLogroDeMetasGuard extends PeriodicGuardBESA{
    
    @Override
    public void funcPeriodicExecGuard(EventBESA event) {
        ((ClienteBDIAgent)this.getAgent()).impulsoBDI();
    }
    
}
