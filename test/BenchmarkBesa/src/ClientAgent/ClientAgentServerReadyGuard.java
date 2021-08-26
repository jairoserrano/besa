/*/
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientAgent;

import BESA.ExceptionBESA;
import Utils.BenchmarkMessage;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import ControlAgent.ControlAgentLaunchGuard;
import ServerAgent.WorkerAgentTaskExecuteGuard;
import Utils.BenchmarkConfig;
import Utils.ExperimentUnitMessage;

/**
 *
 * @author jairo
 */
public class ClientAgentServerReadyGuard extends GuardBESA {

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        ReportBESA.debug("Arrive to " + this.agent.getAlias());
        BenchmarkConfig configExp = BenchmarkConfig.getConfig();
        ClientAgentState LocalAgentState = (ClientAgentState) this.agent.getState();
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();

        String Task = LocalAgentState.getTask();

        if (Task.equals("None")) {
            // TODO: Incluir en lista para eliminar
            /*try {
                this.agent.getAdmLocal().killAgent(
                        this.agent.getAdmLocal().getHandlerByAlias(
                                Message.getAgentRef()
                        ).getAgId(),
                        0.91
                );
                ReportBESA.info("cerrado agente " + Message.getAgentRef());
                LocalAgentState.AgentDone();
            } catch (ExceptionBESA ex) {
                //ReportBESA.error(ex);
                LocalAgentState.AgentDone();
            }*/
        } else {
            // Send Message with Task
            try {
                EventBESA msj = new EventBESA(
                        WorkerAgentTaskExecuteGuard.class.getName(),
                        new BenchmarkMessage(
                                Task,
                                this.agent.getAlias()
                        )
                );
                AgHandlerBESA ah = this.agent.getAdmLocal().
                        getHandlerByAlias(Message.getAgentRef());
                ReportBESA.debug("Sent " + Task + " to " + Message.getAgentRef());
                ah.sendEvent(msj);
                LocalAgentState.reduceRemainTasksNumber();
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }

        ReportBESA.debug("Done " + LocalAgentState.getTaskListDone() + " of " + LocalAgentState.getTotalTasks() + " Tasks.");
        ReportBESA.debug("Done " + LocalAgentState.getAgentCountDone() + " of " + LocalAgentState.CurrentExperiment.getNumberOfAgents() + " agents.");
        ReportBESA.debug("Remain " + LocalAgentState.getRemainTasksNumber() + " tasks.");

        // Check if Experiment are Done
        //if (LocalAgentState.getAgentCountDone() == LocalAgentState.CurrentExperiment.getNumberOfAgents()) {
        if (LocalAgentState.getRemainTasksNumber() == 0) {
            ReportBESA.debug("Finish tasks");
            //System.exit(0);
        } else {
            try {
                this.agent.getAdmLocal().getHandlerByAlias(
                        "ControlAgent"
                ).sendEvent(
                        new EventBESA(
                                ControlAgentLaunchGuard.class.getName(),
                                new ExperimentUnitMessage(
                                        configExp.getNextExperiment(),
                                        Message.getAgentRef()
                                )
                        )
                );
                ReportBESA.info("ClientAgentLaunchExperimentUnitGuard call");
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }

    }

}
