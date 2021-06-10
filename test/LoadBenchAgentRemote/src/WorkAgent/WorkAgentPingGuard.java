/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WorkAgent;

import BenchmarkTools.BenchmarkAgentReceiverMessage;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BenchmarkTools.BenchmarkAgentReadyGuard;

/**
 *
 * @author jairo
 */
public class WorkAgentPingGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        ReportBESA.debug("Active " + this.agent.getAlias());
    }

}
