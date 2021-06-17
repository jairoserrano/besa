/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WorkerAgent;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import ClientAgent.ClientAgentReportGuard;
import ClientAgent.ClientAgentWorkerReadyGuard;
import Utils.BenchmarkMessage;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/**
 *
 * @author jairo
 */
public class AgentWorkerTaskExecuteGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {

        //
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();
        String KindOfWork = Message.getContent();

        if (KindOfWork == null) {
            //ReportBESA.debug("Closing agent " + this.agent.getAlias());
            this.agent.shutdownAgent();
        } else {

            //
            long startTime = System.currentTimeMillis();
            OperatingSystemMXBean operatingSystemMXBean
                    = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            //
            double load;
            byte[] memory;

            //
            switch (KindOfWork) {
                case "Small":
                    load = 40;
                    memory = new byte[1000000];
                    break;
                case "Medium":
                    load = 49;
                    memory = new byte[5000000];
                    break;
                case "High":
                    load = 52;
                    memory = new byte[10000000];
                    break;
                default:
                    load = 0;
                    memory = new byte[0];
                    break;
            }

            double result = fib(load);

            String respuesta = this.agent.getAlias() + ","
                    + result + ","
                    + KindOfWork + ","
                    + ((System.currentTimeMillis() - startTime) / 1000000000) + ","
                    + operatingSystemMXBean.getProcessCpuLoad() + ","
                    + ((operatingSystemMXBean.getTotalPhysicalMemorySize()
                    - operatingSystemMXBean.getFreePhysicalMemorySize()) / 1000000);

            this.sendResults(respuesta);

            memory = null;
            System.gc();
        }

    }

    public void sendResults(String results) {

        // Send results to BenchmarkAgent
        AgHandlerBESA ah;
        try {
            EventBESA msj = new EventBESA(
                    ClientAgentReportGuard.class.getName(),
                    new BenchmarkMessage(
                            results,
                            this.agent.getAlias()
                    )
            );
            ah = this.agent.getAdmLocal().getHandlerByAlias("ClientAg");
            ah.sendEvent(msj);

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

        // Ask for more jobs to BenchmarkAgent
        try {
            EventBESA msj = new EventBESA(
                    ClientAgentWorkerReadyGuard.class.getName(),
                    new BenchmarkMessage(
                            "getTaskToProcess",
                            this.agent.getAlias()
                    )
            );
            ah = this.agent.getAdmLocal().getHandlerByAlias("ClientAg");
            ah.sendEvent(msj);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

    /**
     *
     * @param n Number to calculate Fibonacci number
     * @return fibonacci number
     */
    double fib(double n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }

}