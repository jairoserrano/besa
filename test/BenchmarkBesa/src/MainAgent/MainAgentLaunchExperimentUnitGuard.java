/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainAgent;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Log.ReportBESA;
import Utils.BenchmarkMessage;
import WorkerAgent.WorkerAgent;
import WorkerAgent.WorkerAgentState;
import WorkerAgent.WorkerAgentTaskExecuteGuard;
import Utils.BenchmarkExperimentUnit;
import Utils.ExperimentUnitMessage;
import java.util.ArrayList;

/**
 *
 * @author jairo
 */
public class MainAgentLaunchExperimentUnitGuard extends GuardBESA {

    private ArrayList<WorkerAgent> Agents;

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        ReportBESA.debug("MainAgentLaunchExperimentUnitGuard");
        ExperimentUnitMessage Message = (ExperimentUnitMessage) event.getData();
        BenchmarkExperimentUnit ceUnit = Message.getExperiment();

        // Lanzamiento de todos los agentes del contenedor.
        MainAgentState AgentState = (MainAgentState) this.agent.getState();
        int agTotal = AgentState.CurrentExperiment.getNumberOfAgents();
        int agMainExp = AgentState.getExperimentID();
        Agents = new ArrayList<>();
        String ContainerAlias = "";

        for (int i = 0; i < agTotal; i++) {
            try {
                WorkerAgentState ServerState
                        = new WorkerAgentState(
                                getMainClientAgentName(agMainExp)
                        );
                StructBESA ServerStruct = new StructBESA();
                ServerStruct.bindGuard(WorkerAgentTaskExecuteGuard.class);
                this.Agents.add(new WorkerAgent(
                        getWorkerAgentName(i),
                        ServerState,
                        ServerStruct,
                        0.91
                )
                );
                Agents.get(i).start();
                // Moving agent
                ContainerAlias = AgentState.getCurrentContainerAlias();
                this.agent.getAdmLocal().moveAgent(
                        Agents.get(i).getAlias(),
                        ContainerAlias,
                        0.91
                );
                ReportBESA.debug("Relocated " + getWorkerAgentName(i) + " to " + ContainerAlias);

                String Task = AgentState.getTask();

                this.agent.getAdmLocal().getHandlerByAlias(getWorkerAgentName(i)
                ).sendEvent(new EventBESA(
                        WorkerAgentTaskExecuteGuard.class.getName(),
                        new BenchmarkMessage(
                                Task,
                                getMainClientAgentName(agMainExp)
                        )
                )
                );

                ReportBESA.debug(Task + " to " + getWorkerAgentName(i));
                AgentState.reduceRemainTasksNumber();
            } catch (KernelAgentExceptionBESA ex) {
                //ReportBESA.error(ex);
            } catch (ExceptionBESA ex) {
                //ReportBESA.error(ex);
            }
        }
        ReportBESA.debug("Created agents: " + agTotal);

    }

    /**
     * @param index
     * @param exp
     * @return the AgentName
     */
    public String getWorkerAgentName(int index) {
        String WorkerAgentName = "WorkerAg_";
        return WorkerAgentName + String.valueOf(index);
    }

    /**
     * @param exp
     * @return the AgentName
     */
    public String getMainClientAgentName(int exp) {
        return "MainAg";
    }

    /**
     *
     * @param time
     */
    public void waitAgents(int time) {
        try {
            for (int i = 1; i < time; i++) {
                System.out.print(i + " ");
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            ReportBESA.error(ex);
        }
        System.out.println("");
    }
}
