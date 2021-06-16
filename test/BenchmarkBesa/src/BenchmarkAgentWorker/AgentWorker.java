/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkAgentWorker;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BenchmarkAgent.AgentMasterGuardTask;
import Utils.BenchmarkMessage;

/**
 *
 * @author jairo
 */
public class AgentWorker extends AgentBESA {

    public AgentWorker(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
        if (!this.getAdmLocal().getAdmHandler().getAlias().equals("MAS_100")) {
            //
            ReportBESA.info(this.getAlias() + " en " + this.getAdmLocal().getAdmHandler().getAlias());
            try {
                AgHandlerBESA ah;
                EventBESA msj = new EventBESA(
                        AgentMasterGuardTask.class.getName(),
                        new BenchmarkMessage("getTask", this.getAlias())
                );
                ah = this.getAdmLocal().getHandlerByAlias("BenchmarkAgent");
                ah.sendEvent(msj);
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }

    }

    @Override
    public void shutdownAgent() {
        //ReportBESA.debug("Cerrando agente " + this.getAlias());
    }
}
