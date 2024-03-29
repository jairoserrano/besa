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
import MainAgent.MainAgentReportGuard;
import MainAgent.MainAgentServerReadyGuard;
import Utils.BenchmarkMessage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/**
 *
 * @author jairo
 */
public class WorkerAgentTaskExecuteGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {

        //
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();
        WorkerAgentState WorkerAgentState = (WorkerAgentState) this.agent.getState();
        String KindOfWork = Message.getContent();

        if (KindOfWork.equals("KILL")){
            System.exit(0);
        }
        
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
                //load = 2;
                memory = new byte[1000000];
                break;
            case "Medium":
                load = 44;
                //load = 2;
                memory = new byte[5000000];
                break;
            case "High":
                load = 47;
                //load = 2;
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
                + operatingSystemMXBean.getSystemLoadAverage();// + ","
                //+ ((operatingSystemMXBean.get.getTotalMemorySize()
                //- operatingSystemMXBean..getFreeMemorySize()) / 1000000);

        this.sendResults(respuesta, WorkerAgentState.getClientName());

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
                    MainAgentReportGuard.class.getName(),
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
        // TODO: REVISAR
        try {
            this.agent.getAdmLocal().getHandlerByAlias(
                    clientName
            ).sendEvent(new EventBESA(
                            MainAgentServerReadyGuard.class.getName(),
                            new BenchmarkMessage(
                                    "Next",
                                    this.agent.getAlias()
                            )
                    )
            );
            ReportBESA.info("Tarea completada en " + this.agent.getAlias());
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
