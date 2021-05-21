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
import com.martensigwart.fakeload.FakeLoad;
import com.martensigwart.fakeload.FakeLoadExecutor;
import com.martensigwart.fakeload.FakeLoadExecutors;
import com.martensigwart.fakeload.FakeLoads;
import com.martensigwart.fakeload.MemoryUnit;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class WorkAgentGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        WorkAgentMessage mensaje = (WorkAgentMessage) event.getData();
        ReportBESA.info("Mensaje recibido en " + this.agent.getAlias() + ", procesando " + mensaje.getContent());

        
        // @TODO: maximos CPU y RAM
        try {
            ReportBESA.info(100 / 400);
            FakeLoad fakeload = FakeLoads.create().
                    lasting(20, TimeUnit.SECONDS).
                    withCpu(100 / 400).
                    withMemory(20, MemoryUnit.MB);

            FakeLoadExecutor executor = FakeLoadExecutors.newDefaultExecutor();
            executor.execute(fakeload);

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        ReportBESA.info("Terminada " + this.agent.getAlias() + " " + mensaje.getContent());

        AgHandlerBESA ah;
        try {
            ah = this.agent.getAdmLocal().getHandlerByAlias("BenchmarkAgent");
            EventBESA msj = new EventBESA(
                    BenchmarkAgentReceiverGuard.class.getName(),
                    new BenchmarkAgentReceiverMessage("Cálculo recibido en " + this.agent.getAlias() + " " + mensaje.getContent())
            );
            ah.sendEvent(msj);
        } catch (ExceptionBESA ex) {
            Logger.getLogger(WorkAgentGuard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
