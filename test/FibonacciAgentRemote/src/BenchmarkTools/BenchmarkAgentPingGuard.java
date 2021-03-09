/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentPingGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        ReportBESA.info("Ready " +  this.agent.getAlias());
    }

}
