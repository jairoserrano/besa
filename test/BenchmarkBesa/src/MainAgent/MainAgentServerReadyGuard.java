/*/
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainAgent;

import BESA.ExceptionBESA;
import Utils.BenchmarkMessage;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import WorkerAgent.WorkerAgentTaskExecuteGuard;
import Utils.BenchmarkConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public class MainAgentServerReadyGuard extends GuardBESA {

    @Override
    public synchronized void funcExecGuard(EventBESA event) {

        ReportBESA.debug("Arrive to " + this.agent.getAlias());
        BenchmarkConfig configExp = BenchmarkConfig.getConfig();
        MainAgentState LocalAgentState = (MainAgentState) this.agent.getState();
        BenchmarkMessage Message = (BenchmarkMessage) event.getData();

        String Task = LocalAgentState.getTask();

        if (Task.equals("None")) {
            //TODO
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

        ReportBESA.debug("---------");
        ReportBESA.debug("Done " + LocalAgentState.getTaskListDone() + " of " + LocalAgentState.getTotalTasks() + " Tasks.");
        ReportBESA.debug("Total " + LocalAgentState.getRemainTasksNumber() + " tasks.\n");

        if (LocalAgentState.getTaskListDone() == LocalAgentState.getTotalTasks()) {
            Task = "KILL";
            ReportBESA.debug("Cantidad de agentes: " + LocalAgentState.getAgentsNumber());
            for (int i = 0; i < LocalAgentState.getAgentsNumber(); i++) {
                try {
                    EventBESA msj = new EventBESA(
                            WorkerAgentTaskExecuteGuard.class.getName(),
                            new BenchmarkMessage(
                                    Task,
                                    "WorkerAg_" + String.valueOf(i)
                            )
                    );
                    AgHandlerBESA ah = this.agent.getAdmLocal().
                            getHandlerByAlias("WorkerAg_" + String.valueOf(i));
                    ReportBESA.debug("Terminando WorkerAg_" + String.valueOf(i));
                    ah.sendEvent(msj);
                } catch (ExceptionBESA ex) {
                    ReportBESA.info("Ya había terminado");
                }
            }
            ReportBESA.debug("REPORT: " + LocalAgentState.getEndTime());
            System.exit(0);
        }

    }

}
