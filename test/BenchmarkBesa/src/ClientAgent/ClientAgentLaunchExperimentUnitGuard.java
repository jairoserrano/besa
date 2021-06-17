/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientAgent;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import Utils.BenchmarkMessage;
import WorkerAgent.AgentWorker;
import WorkerAgent.AgentWorkerState;
import WorkerAgent.AgentWorkerTaskExecuteGuard;
import java.util.ArrayList;

/**
 *
 * @author jairo
 */
public class ClientAgentLaunchExperimentUnitGuard extends GuardBESA {

    private String AgentName = "AgentWorker_";
    private ArrayList<AgentWorker> Agents;

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        ReportBESA.debug("ClientAgentWorkerReadyGuard");
        ClientAgentState AgentState = (ClientAgentState) this.agent.getState();
        int agNumber = AgentState.CurrentExperiment.getNumberOfAgents();
        Agents = new ArrayList<>();

        // Lanzamiento de todos los agentes del contenedor.
        for (int i = 0; i < agNumber; i++) {
            try {
                AgentWorkerState WorkerState = new AgentWorkerState();
                StructBESA WorkerStruct = new StructBESA();
                WorkerStruct.bindGuard(AgentWorkerTaskExecuteGuard.class);
                this.Agents.add(
                        new AgentWorker(
                                getAgentName(i),
                                WorkerState,
                                WorkerStruct,
                                0.91
                        )
                );
                Agents.get(i).start();
                ReportBESA.debug("Created " + Agents.get(i).getAlias());
            } catch (KernelAgentExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }

        // Moving agents to Execute Container
        for (int i = 0; i < agNumber; i++) {
            try {
                this.agent.getAdmLocal().moveAgent(getAgentName(i),
                        AgentState.getWorkerContainerAlias(),
                        0.91
                );
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
            ReportBESA.debug("Agent " + getAgentName(i) + " Relocated");
        }
        // Sending First Unit Task
        for (int i = 0; i < agNumber; i++) {
            try {
                String Task = AgentState.getTask();
                EventBESA msj = new EventBESA(
                        AgentWorkerTaskExecuteGuard.class.getName(),
                        new BenchmarkMessage(Task, "ClientAg"));

                AgHandlerBESA ah
                        = this.agent.getAdmLocal().getHandlerByAlias(
                                Agents.get(i).getAlias()
                        );
                ah.sendEvent(msj);
                ReportBESA.debug(Task + " sent to " + Agents.get(i).getAlias());
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }

    }

    // Asignar agente pequeño, mediano, con la tarea asignada
    /**
     * @param index
     * @return the AgentName
     */
    public String getAgentName(int index) {
        return AgentName + String.valueOf(index);
    }

    /**
     * @param AgentName the AgentName to set
     */
    public void setAgentName(String AgentName) {
        this.AgentName = AgentName;
    }
}
