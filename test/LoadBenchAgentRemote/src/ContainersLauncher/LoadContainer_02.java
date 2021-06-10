/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import WorkAgent.WorkAgent;
import WorkAgent.WorkAgentLoadGuard;
import WorkAgent.WorkAgentPingGuard;
import WorkAgent.WorkAgentReadyGuard;
import WorkAgent.WorkAgentState;
import java.util.ArrayList;

/**
 *
 * @author jairo
 */
public class LoadContainer_02 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ExceptionBESA {

        BenchmarkConfig config = BenchmarkConfig.getConfig(args);
        ArrayList<WorkAgent> Agents = new ArrayList<>();
        int ContainerID = Integer.parseInt(LoadContainer_02.class.getName().split("_")[1]);
        String AgentName = "WorkAgent_";
        AdmBESA adminBesa = AdmBESA.getInstance("config/Container_0" + ContainerID + ".xml");
        int AgentNumber;

        try {
            /**
             * Lanzamiento del contenedor del nodo
             */
            ReportBESA.debug("Lanzando config/Container_0" + ContainerID + ".xml");

            WorkAgentState estado = new WorkAgentState();
            StructBESA Struct = new StructBESA();
            Struct.bindGuard(WorkAgentLoadGuard.class);
            Struct.bindGuard(WorkAgentPingGuard.class);
            Struct.bindGuard(WorkAgentReadyGuard.class);

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
            ReportBESA.error(ex);
        }

        /*if (config.IsBalancerOn()) {
                BalancerBESA balanceador = new BalancerBESA(10000, 1000000000L);
                balanceador.initBalancer();
            }*/
    }
}
