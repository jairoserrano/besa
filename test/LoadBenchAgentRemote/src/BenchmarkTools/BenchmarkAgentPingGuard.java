/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import ContainersLauncher.BenchmarkConfig;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentPingGuard extends GuardBESA {

    BenchmarkConfig config = BenchmarkConfig.getConfig();

    @Override
    public synchronized void funcExecGuard(EventBESA event) {
        BenchmarkAgentReceiverMessage mensaje = (BenchmarkAgentReceiverMessage) event.getData();
        ReportBESA.info(mensaje.getContent());
    }

}
