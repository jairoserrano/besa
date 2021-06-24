/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerAgent;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import ClientAgent.ClientAgentReportGuard;
import ClientAgent.ClientAgentServerReadyGuard;
import Utils.BenchmarkMessage;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/**
 *
 * @author jairo
 */
public class AgentServerTaskExecuteGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {

        //
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();
        AgentServerState ServerAgentState = (AgentServerState) this.agent.getState();
        String KindOfWork = Message.getContent();

        long startTime = System.currentTimeMillis();
        OperatingSystemMXBean operatingSystemMXBean
                = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        //
        double load;
        byte[] memory;

        //
        switch (KindOfWork) {
            case "Small":
                //load = 40;
                load = 2;
                memory = new byte[1000000];
                break;
            case "Medium":
                //load = 49;
                load = 2;
                memory = new byte[5000000];
                break;
            case "High":
                //load = 52;
                load = 2;
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
                + (System.currentTimeMillis() - startTime) + ","
                + operatingSystemMXBean.getProcessCpuLoad() + ","
                + ((operatingSystemMXBean.getTotalMemorySize()
                - operatingSystemMXBean.getFreeMemorySize()) / 1000000);

        this.sendResults(respuesta, ServerAgentState.getClientName());

        memory = null;

    }

    /**
     *
     * @param results
     * @param clientName
     */
    public void sendResults(String results, String clientName) {

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
            ah = this.agent.getAdmLocal().getHandlerByAlias(clientName);
            ah.sendEvent(msj);

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

        // Ask for more jobs to BenchmarkAgent
        try {
            this.agent.getAdmLocal().getHandlerByAlias(
                    clientName
            ).sendEvent(
                    new EventBESA(
                            ClientAgentServerReadyGuard.class.getName(),
                            new BenchmarkMessage(
                                    "getTaskToProcess",
                                    this.agent.getAlias()
                            )
                    )
            );
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

    /**
     *
     * @param n Number to calculate Fibonacci number
     * @return Fibonacci number
     */
    double fib(double n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }

}
