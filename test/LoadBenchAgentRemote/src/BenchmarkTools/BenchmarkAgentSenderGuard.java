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
import ContainersLauncher.BenchmarkConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentSenderGuard extends GuardBESA {

    BenchmarkConfig config = BenchmarkConfig.getConfig();

    @Override
    public void funcExecGuard(EventBESA event) {

        AgHandlerBESA ah;

        /**
         * Si la cooperación está activa, se envia los mensajes a todos los
         * contenedores y agentes.
         */
        if (config.IsCooperationOn()) {

            /**
             * Ejecutando las tareas
             */
            String KindOfLoad;
            String SelectedAgent;

            /**
             * Se recorre el listado de tareas y se envia a cada agente
             * disponible
             */
            BenchmarkAgentState AgentState = (BenchmarkAgentState) this.agent.getState();
            ReportBESA.debug(" Tareas " + AgentState.Tasks() + ", Contador " + AgentState.getCounter());
            while (AgentState.getCounter() > 0) {
                // captura una tarea
                KindOfLoad = AgentState.getTask();
                ReportBESA.debug(KindOfLoad);
                SelectedAgent = AgentState.getAgentReady();
                ReportBESA.debug(SelectedAgent);
                //ReportBESA.debug(" KindOfLoad " + KindOfLoad + " SelectedAgent " + SelectedAgent);
                /**
                 * Seleccionado el agente y enviado el mensaje.
                 */
                try {
                    ReportBESA.debug("Enviando tarea "
                            + KindOfLoad + " a "
                            + SelectedAgent
                            + ", Tareas faltantes: " + AgentState.Tasks());
                    EventBESA msj = new EventBESA(
                            WorkAgentLoadGuard.class.getName(),
                            new WorkAgentMessage(KindOfLoad)
                    );
                    ah = this.agent.getAdmLocal().getHandlerByAlias(SelectedAgent);
                    ah.sendEvent(msj);
                } catch (ExceptionBESA ex) {
                    Logger.getLogger(BenchmarkAgentState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            ReportBESA.debug("Contenedores sin cooperación, cada contenedor lanzara sus tareas");
        }

    }

}
