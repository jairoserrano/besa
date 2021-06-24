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
import BESA.Log.ReportBESA;
import Utils.BenchmarkMessage;
import ServerAgent.AgentServer;
import ServerAgent.AgentServerState;
import ServerAgent.AgentServerTaskExecuteGuard;
import java.util.ArrayList;

/**
 *
 * @author jairo
 */
public class ClientAgentLaunchExperimentUnitGuard extends GuardBESA {

    private ArrayList<AgentServer> Agents;

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        ReportBESA.debug("ClientAgentLaunchExperimentUnitGuard");
        ClientAgentState AgentState = (ClientAgentState) this.agent.getState();
        int agTotal = AgentState.CurrentExperiment.getNumberOfAgents();
        int agClientExp = AgentState.getExperimentID();
        Agents = new ArrayList<>();
        String ContainerAlias = "";

        // Lanzamiento de todos los agentes del contenedor.
        for (int i = 0; i < agTotal; i++) {
            try {
                AgentServerState ServerState
                        = new AgentServerState(
                                getClientAgentName(agClientExp)
                        );
                StructBESA ServerStruct = new StructBESA();
                ServerStruct.bindGuard(AgentServerTaskExecuteGuard.class);
                this.Agents.add(
                        new AgentServer(
                                getServerAgentName(i, agClientExp),
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
                ReportBESA.debug("Relocated " + getServerAgentName(i, agClientExp) + " to " + ContainerAlias);
                
                String Task = AgentState.getTask();

                this.agent.getAdmLocal().getHandlerByAlias(
                        getServerAgentName(
                                i,
                                agClientExp
                        )
                ).sendEvent(
                        new EventBESA(
                                AgentServerTaskExecuteGuard.class.getName(),
                                new BenchmarkMessage(
                                        Task,
                                        getClientAgentName(agClientExp)
                                )
                        )
                );
                ReportBESA.debug(Task + " to " + getServerAgentName(i, agClientExp));
                
            } catch (KernelAgentExceptionBESA ex) {
                ReportBESA.error(ex);
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }
        ReportBESA.debug("Created agents: " + agTotal);

    }

    /**
     * @param index
     * @param exp
     * @return the AgentName
     */
    public String getServerAgentName(int index, int exp) {
        String ServerAgentName = "ServerAg_E" + String.valueOf(exp) + "_";
        return ServerAgentName + String.valueOf(index);
    }

    /**
     * @param exp
     * @return the AgentName
     */
    public String getClientAgentName(int exp) {
        String ClientAgentName = "ClientAg_E";
        return ClientAgentName + String.valueOf(exp);
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
