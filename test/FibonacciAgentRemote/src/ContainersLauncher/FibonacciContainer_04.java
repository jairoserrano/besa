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
public class FibonacciContainer_04 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int NumberOfAgents = 10;

        ArrayList<FibonacciAgent> Agents = new ArrayList<>();

        try {

            ReportBESA.info("Lanzando config/Container_04.xml");

            AdmBESA adminBesa = AdmBESA.getInstance("config/Container_04.xml");

            FibonacciAgentState estado = new FibonacciAgentState();
            StructBESA Struct = new StructBESA();
            Struct.bindGuard(FibonacciAgentGuard.class);

            for (int i = 0; i < NumberOfAgents; i++) {
                Agents.add(
                        new FibonacciAgent(
                                "FiboAgente_04_" + String.valueOf(i),
                                estado,
                                Struct,
                                0.91
                        )
                );
                Agents.get(i).start();
                adminBesa.registerAgent(Agents.get(i), "FiboAgente_4_" + String.valueOf(i), "FiboAgente_4_" + String.valueOf(i));
                ReportBESA.info("Lanzando FiboAgente_5_" + String.valueOf(i));
            }

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }

}
