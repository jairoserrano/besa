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
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author jairo
 */
public class FibonacciAgentMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int number = 0;

        try {
            AdmBESA adminBesa = AdmBESA.getInstance();

            FibonacciAgentState estado = new FibonacciAgentState();
            StructBESA Struct = new StructBESA();
            Struct.bindGuard(FibonacciAgentGuard.class);

            FibonacciAgent FiboAgent1 = new FibonacciAgent("Agente1", estado, Struct, 0.91);
            FiboAgent1.start();
            
            FibonacciAgent FiboAgent2 = new FibonacciAgent("Agente2", estado, Struct, 0.91);
            FiboAgent2.start();
            
            AgHandlerBESA ah1 = adminBesa.getHandlerByAid(FiboAgent1.getAid());            
            FibonacciAgentMessage message1 = new FibonacciAgentMessage("40");
            EventBESA msj1 = new EventBESA(FibonacciAgentGuard.class.getName(), message1);
            ah1.sendEvent(msj1);
            
            AgHandlerBESA ah2 = adminBesa.getHandlerByAid(FiboAgent2.getAid());
            FibonacciAgentMessage message2 = new FibonacciAgentMessage("5");
            EventBESA msj2 = new EventBESA(FibonacciAgentGuard.class.getName(), message2);
            ah2.sendEvent(msj2);

        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }

}
