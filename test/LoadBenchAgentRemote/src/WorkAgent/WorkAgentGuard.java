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
        ReportBESA.debug("Carga " + mensaje.getKindOfWork() + ", recibida en " + this.agent.getAlias());

        // @TODO: maximos CPU y RAM
        if ("Small".equals(mensaje.getKindOfWork())) {
            for (int i = 0; i < 5000; i++) {
                int b = i * 2;
            }
        }
        if ("Medium".equals(mensaje.getKindOfWork())) {
            for (int i = 0; i < 35000; i++) {
                int b = i * 2;
            }
        }
        if ("High".equals(mensaje.getKindOfWork())) {
            for (int i = 0; i < 70000; i++) {
                int b = i * 2;
            }
        }

        try {
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAlias("BenchmarkAgent");
            EventBESA msj = new EventBESA(
                    BenchmarkAgentReceiverGuard.class.getName(),
                    new BenchmarkAgentReceiverMessage(this.agent.getAlias() + " " + mensaje.getKindOfWork())
            );
            ah.sendEvent(msj);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        ReportBESA.debug("Terminada carga " + mensaje.getKindOfWork() + ", en " + this.agent.getAlias());
    }

}
