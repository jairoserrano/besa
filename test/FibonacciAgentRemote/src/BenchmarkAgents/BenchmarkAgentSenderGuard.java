/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkAgents;

import BESA.ExceptionBESA;
import FibonacciAgent.*;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentSenderGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        BenchmarkAgentSenderMessage message = (BenchmarkAgentSenderMessage) event.getData();
        ReportBESA.info("Contenedores " + message.getNumberOfContainers() + " - Agentes por contenedor " + message.getNumberOfAgentsPerContainer());

        AgHandlerBESA ah;

        for (int i = 1; i <= message.getNumberOfContainers(); i++) {
            for (int j = 0; j < message.getNumberOfAgentsPerContainer(); j++) {
                try {
                    ah = this.agent.getAdmLocal().getHandlerByAlias("FiboAgente_0" + String.valueOf(i) + "_" + String.valueOf(j));
                    ReportBESA.info("Enviando mensaje a FiboAgente_0" + String.valueOf(i) + "_" + String.valueOf(j));
                    EventBESA msj = new EventBESA(
                            FibonacciAgentGuard.class.getName(),
                            new FibonacciAgentMessage("5")
                    );
                    ah.sendEvent(msj);
                } catch (ExceptionBESA ex) {
                    Logger.getLogger(BenchmarkAgentSenderGuard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
