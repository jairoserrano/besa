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
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentSenderGuard extends GuardBESA {

    BenchmarkConfig config = BenchmarkConfig.getConfig();

    @Override
    public void funcExecGuard(EventBESA event) {
        BenchmarkAgentMessage message = (BenchmarkAgentMessage) event.getData();

        /**
         * Si la cooperación está activa, se envia los mensajes a todos los
         * contenedores y agentes.
         */
        if (config.IsCooperationOn()) {
            
            AgHandlerBESA ah;

            /**
             * Generando lista de tareas
             */
            ArrayList<String> tasks = new ArrayList<>();
            for (int i = 0; i < config.getSmallLoads(); i++) {
                tasks.add("Small");
            }
            for (int i = 0; i < config.getMediumLoads();i++) {
                tasks.add("Medium");
            }
            for (int i = 0; i < config.getHighLoads(); i++) {
                tasks.add("High");
            }
            /**
             * Aleatorizando la lista
             */
            if (!config.InOrder()) {
                Collections.shuffle(tasks);
            }
            /**
             * Ejecutando las tareas
             */
            // Número del agente
            int AgentNumber = 1;
            int TaskID = 1;
            /**
             * Se recorre el listado de tareas y se envia a cada agente
             * disponible
             */
            for (String KindOfLoad : tasks) {
                /**
                 * Si las tareas son mayor al número de agentes, se envian en un
                 * ciclo
                 */
                if ((TaskID % config.getNumberOfAgents()) == 0) {
                    AgentNumber = 1;
                }
                //ReportBESA.debug("tasks "+ tasks.size() + ", Reinicio " + (TaskID % config.getNumberOfAgents()));
                /**
                 * Seleccionado el agente y enviado el mensaje.
                 */
                ReportBESA.debug("Task ID " + TaskID + ", AgentNumber " + AgentNumber + ", NúmeroAgentes " + config.getNumberOfAgents());
                try {
                    ah = this.agent.getAdmLocal().getHandlerByAlias("WorkAgent_" + String.valueOf(AgentNumber));
                    ReportBESA.debug("Enviando mensaje a WorkAgent_" + String.valueOf(AgentNumber) + " " + KindOfLoad);
                    EventBESA msj = new EventBESA(
                            WorkAgentGuard.class.getName(),
                            new WorkAgentMessage(KindOfLoad)
                    );
                    ah.sendEvent(msj);
                } catch (ExceptionBESA ex) {
                    Logger.getLogger(BenchmarkAgentSenderGuard.class.getName()).log(Level.SEVERE, null, ex);
                }
                AgentNumber++;
                TaskID++;
            }

        } else {
            ReportBESA.debug("Contenedores sin cooperación, cada contenedor lanzara sus tareas");
        }

    }

}
