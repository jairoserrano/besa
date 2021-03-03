/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FibonacciAgent;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import java.util.ArrayList;

/**
 *
 * @author jairo
 */
public class FibonacciAgentMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int NumberOfAgents = 55;
        ArrayList<FibonacciAgent> Agents = new ArrayList<FibonacciAgent>();

        try {
            AdmBESA adminBesa = AdmBESA.getInstance();

            FibonacciAgentState estado = new FibonacciAgentState();
            StructBESA Struct = new StructBESA();
            Struct.bindGuard(FibonacciAgentGuard.class);

            for (int i = 0; i < NumberOfAgents; i++) {
                Agents.add(new FibonacciAgent("Agente1", estado, Struct, 0.91));
            }

            for (int i = 0; i < NumberOfAgents; i++) {
                Agents.get(i).start();
            }

            for (int i = 0; i < NumberOfAgents; i++) {
                AgHandlerBESA ah = adminBesa.getHandlerByAid(Agents.get(i).getAid());
                EventBESA msj = new EventBESA(FibonacciAgentGuard.class.getName(), new FibonacciAgentMessage(String.valueOf(i)));
                ah.sendEvent(msj);                
            }

            adminBesa.kill(0.91);

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }

}
