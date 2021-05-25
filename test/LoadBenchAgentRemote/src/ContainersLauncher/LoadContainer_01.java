/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BenchmarkTools.BenchmarkAgent;
import BenchmarkTools.BenchmarkAgentPingMessage;
import BenchmarkTools.BenchmarkAgentSenderGuard;
import WorkAgent.WorkAgent;
import WorkAgent.WorkAgentGuard;
import WorkAgent.WorkAgentMessage;
import WorkAgent.WorkAgentState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class LoadContainer_01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        BenchmarkConfig config = BenchmarkConfig.getConfig(args);
        ArrayList<WorkAgent> Agents = new ArrayList<>();
        int ContainerID = Integer.parseInt(LoadContainer_01.class.getName().split("_")[1]);
        String AgentName = "WorkAgent_";
        int AgentNumber;
        AdmBESA adminBesa = AdmBESA.getInstance("config/Container_0" + ContainerID + ".xml");

        try {
            /**
             * Lanzamiento del contenedor del nodo
             */
            ReportBESA.debug("Lanzando config/Container_0" + ContainerID + ".xml");

            WorkAgentState estado = new WorkAgentState();
            StructBESA Struct = new StructBESA();
            Struct.bindGuard(WorkAgentGuard.class);

            /**
             * Lanzamiento de todos los agentes del contenedor.
             */
            AgentNumber = ((ContainerID - 1) * config.getAgentsByContainer()) + 1;
            for (int i = 1; i <= config.getAgentsByContainer(); i++) {
                Agents.add(
                        new WorkAgent(
                                AgentName + String.valueOf(AgentNumber),
                                estado,
                                Struct,
                                0.91
                        )
                );
                AgentNumber++;
            }
            /**
             * Inicio y registro de agentes
             */
            for (int i = 0; i < Agents.size(); i++) {
                Agents.get(i).start();
                adminBesa.registerAgent(
                        Agents.get(i),
                        Agents.get(i).getAlias(),
                        Agents.get(i).getAlias()
                );
            }

        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(LoadContainer_01.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * Si no hay cooperación, el contenedor enviará los mensajes a los
         * agentes locales y no el principal.
         */
        if (!config.IsCooperationOn()) {
            /**
             * Revisando si existen los agentes locales
             */
            boolean ready = false;
            AgHandlerBESA ah;

            while (!ready) {
                try {
                    ReportBESA.debug("Checkeando agentes");
                    Thread.sleep(1000);
                    AgentNumber = ((ContainerID - 1) * config.getAgentsByContainer()) + 1;
                    for (int i = 1; i <= config.getAgentsByContainer(); i++) {
                        ah = adminBesa.getHandlerByAlias("WorkAgent_" + String.valueOf(AgentNumber));
                        EventBESA msj = new EventBESA(
                                BenchmarkAgentPingMessage.class.getName(),
                                null
                        );
                        ah.sendEvent(msj);
                        AgentNumber++;
                    }
                    ready = true;
                } catch (InterruptedException ex) {
                    Logger.getLogger(BenchmarkAgent.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExceptionBESA ex) {
                    ReportBESA.debug("Checkeo fallido");
                    ReportBESA.debug(ex.toString());
                }
            }

            /**
             * Generando lista de tareas
             */
            ArrayList<String> tasks = new ArrayList<>();
            for (int i = 0; i < config.getSmallLoads() / config.getNumberOfContainers(); i++) {
                tasks.add("Small");
            }
            for (int i = 0; i < config.getMediumLoads() / config.getNumberOfContainers(); i++) {
                tasks.add("Medium");
            }
            for (int i = 0; i < config.getHighLoads() / config.getNumberOfContainers(); i++) {
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
            AgentNumber = ((ContainerID - 1) * config.getAgentsByContainer()) + 1;
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
                if ((TaskID % config.getAgentsByContainer()) == 0) {
                    AgentNumber = ((ContainerID - 1) * config.getAgentsByContainer()) + 1;
                }
                /**
                 * Seleccionado el agente y enviado el mensaje.
                 */
                ReportBESA.debug("Task ID " + TaskID + ", Task Size " + tasks.size() + ", AgentNumber " + AgentNumber);
                try {
                    ah = adminBesa.getHandlerByAlias(AgentName + String.valueOf(AgentNumber));
                    ReportBESA.debug("Enviando mensaje a " + AgentName + String.valueOf(AgentNumber) + " " + KindOfLoad);
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
            ReportBESA.debug("Contenedores en cooperación");
        }
    }
}
