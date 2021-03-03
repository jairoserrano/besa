/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FibonacciAgent;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import java.text.DecimalFormat;

/**
 *
 * @author jairo
 */
public class FibonacciAgentGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        FibonacciAgentMessage mensaje = (FibonacciAgentMessage) event.getData();
        ReportBESA.info("Se calculará el fib de " + mensaje.getContent());
        ReportBESA.info("Fibonacci de " + mensaje.getContent() + " es " + new DecimalFormat("#").format(fib(Double.parseDouble(mensaje.getContent()))));
    }

    static double fib(double n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }

}
