/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import WorkAgent.WorkAgent;
import WorkAgent.WorkAgentGuard;
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
    public static void main(String[] args) {

        BenchmarkConfig config = new BenchmarkConfig();
        ArrayList<WorkAgent> Agents = new ArrayList<>();

        try {

            ReportBESA.info("Lanzando config/Container_02.xml");

            AdmBESA adminBesa = AdmBESA.getInstance("config/Container_02.xml");

            WorkAgentState estado = new WorkAgentState();
            StructBESA Struct = new StructBESA();
            Struct.bindGuard(WorkAgentGuard.class);

            for (int i = 0; i < config.getNumberOfAgentsPerContainer(); i++) {
                Agents.add(
                        new WorkAgent(
                                "WorkAgent_02_" + String.valueOf(i),
                                estado,
                                Struct,
                                0.91
                        )
                );
                Agents.get(i).start();
                adminBesa.registerAgent(Agents.get(i),
                        "WorkAgent_2_" + String.valueOf(i),
                        "WorkAgent_2_" + String.valueOf(i)
                );
            }

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

}
