/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WorkAgent;

import BenchmarkTools.BenchmarkAgentReceiverGuard;
import BenchmarkTools.BenchmarkAgentReceiverMessage;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BenchmarkTools.BenchmarkAgentReadyGuard;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/**
 *
 * @author jairo
 */
public class WorkAgentLoadGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        /**
         *
         */
        long startTime = System.currentTimeMillis();
        WorkAgentMessage mensaje = (WorkAgentMessage) event.getData();
        OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        /**
         *
         */
        double load;
        byte[] memory;

        if (null == mensaje.getKindOfWork()) {
            load = 0;
            memory = new byte[0];
        } else {
            switch (mensaje.getKindOfWork()) {
                case "Small" -> {
                    load = 40;
                    memory = new byte[1000000];
                }
                case "Medium" -> {
                    load = 49;
                    memory = new byte[5000000];
                }
                case "High" -> {
                    load = 52;
                    memory = new byte[10000000];
                }
                default -> {
                    load = 0;
                    memory = new byte[0];
                }
            }
        }
        //ReportBESA.info("ejecutando fibo en " + this.agent.getAlias());
        double result = fib(load);

        String respuesta = this.agent.getAlias() + ","
                + result + ","
                + mensaje.getKindOfWork() + ","
                + ((System.currentTimeMillis() - startTime) / 1000000000) + ","
                + operatingSystemMXBean.getProcessCpuLoad() + ","
                + ((operatingSystemMXBean.getTotalMemorySize()
                - operatingSystemMXBean.getFreeMemorySize()) / 1000000);

        this.sendResults(respuesta);
        this.reActivateAgent();

        memory = null;
        System.gc();
    }

    public void sendResults(String results) {
        AgHandlerBESA ah;
        EventBESA msj;

        try {
            ah = agent.getAdmLocal().getHandlerByAlias("BenchmarkAgent");

            msj = new EventBESA(
                    BenchmarkAgentReceiverGuard.class.getName(),
                    new BenchmarkAgentReceiverMessage(results)
            );
            ah.sendEvent(msj);

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

    double fib(double n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }

    private void reActivateAgent() {
        AgHandlerBESA ah;
        EventBESA msj;

        try {
            ah = agent.getAdmLocal().getHandlerByAlias("BenchmarkAgent");

            ReportBESA.info("Reactivando " + agent.getAlias());
            msj = new EventBESA(
                    BenchmarkAgentReadyGuard.class.getName(),
                    new BenchmarkAgentReceiverMessage(agent.getAlias())
            );
            ah.sendEvent(msj);

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }

}
