/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

import BESA.ExceptionBESA;
import WorkAgent.*;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import java.util.logging.Level;
import java.util.logging.Logger;
import ContainersLauncher.BenchmarkConfig;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentSenderGuard extends GuardBESA {
    
    BenchmarkConfig config = BenchmarkConfig.getConfig();

    @Override
    public void funcExecGuard(EventBESA event) {
        BenchmarkAgentMessage message = (BenchmarkAgentMessage) event.getData();
        ReportBESA.debug("Contenedores " + message.getNumberOfContainers() + " - Agentes por contenedor " + message.getNumberOfAgentsPerContainer());

        AgHandlerBESA ah;

        for (int i = 1; i <= message.getNumberOfContainers(); i++) {
            for (int j = 0; j < message.getNumberOfAgentsPerContainer(); j++) {
                try {
                    ah = this.agent.getAdmLocal().getHandlerByAlias("WorkAgent_" + String.valueOf(i) + "_" + String.valueOf(j));
                    ReportBESA.debug("Enviando mensaje a WorkAgent_" + String.valueOf(i) + "_" + String.valueOf(j) + " " + "uno");
                    EventBESA msj = new EventBESA(
                            WorkAgentGuard.class.getName(),
                            new WorkAgentMessage(
                                    "uno"
                            )
                    );
                    ah.sendEvent(msj);
                } catch (ExceptionBESA ex) {
                    Logger.getLogger(BenchmarkAgentSenderGuard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
