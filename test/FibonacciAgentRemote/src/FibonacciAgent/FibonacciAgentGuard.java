/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FibonacciAgent;

import BenchmarkTools.BenchmarkAgentReceiverGuard;
import BenchmarkTools.BenchmarkAgentReceiverMessage;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class FibonacciAgentGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        FibonacciAgentMessage mensaje = (FibonacciAgentMessage) event.getData();
        ReportBESA.info("Mensaje recibido en " + this.agent.getAlias() + ", Se calculará el fib de " + mensaje.getContent());

        String fibo = String.valueOf(
                fib(
                        Double.parseDouble(
                                mensaje.getContent()
                        )
                )
        );
        ReportBESA.info("Enviando " + fibo);

        AgHandlerBESA ah;
        try {
            ah = this.agent.getAdmLocal().getHandlerByAlias("BenchmarkAgent");
            EventBESA msj = new EventBESA(
                    BenchmarkAgentReceiverGuard.class.getName(),
                    new BenchmarkAgentReceiverMessage("Cálculo recibido en " + this.agent.getAlias() + " " +fibo)
            );
            ah.sendEvent(msj);
        } catch (ExceptionBESA ex) {
            Logger.getLogger(FibonacciAgentGuard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static double fib(double n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }

}
