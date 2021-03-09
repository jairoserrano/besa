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
import FibonacciAgent.FibonacciAgent;
import FibonacciAgent.FibonacciAgentGuard;
import FibonacciAgent.FibonacciAgentState;
import java.util.ArrayList;

/**
 *
 * @author jairo
 */
public class FibonacciContainer_03 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        BenchmarkConfig config = new BenchmarkConfig();
        ArrayList<FibonacciAgent> Agents = new ArrayList<>();

        try {

            ReportBESA.info("Lanzando config/Container_03.xml");

            AdmBESA adminBesa = AdmBESA.getInstance("config/Container_03.xml");

            FibonacciAgentState estado = new FibonacciAgentState();
            StructBESA Struct = new StructBESA();
            Struct.bindGuard(FibonacciAgentGuard.class);

            for (int i = 0; i < config.getNumberOfAgentsPerContainer(); i++) {
                Agents.add(
                        new FibonacciAgent(
                                "FiboAgente_03_" + String.valueOf(i),
                                estado,
                                Struct,
                                0.91
                        )
                );
                Agents.get(i).start();
                adminBesa.registerAgent(Agents.get(i),
                        "FiboAgente_3_" + String.valueOf(i),
                        "FiboAgente_3_" + String.valueOf(i)
                );
            }

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

}
