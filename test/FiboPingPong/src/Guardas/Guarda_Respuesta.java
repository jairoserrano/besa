/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Guardas;

import Agentes.FiboPingPongStatus;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import Logicas.Ping;
import Logicas.Pong;
import Logicas.Respuesta;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bitas
 */
public class Guarda_Respuesta extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {

        DataBESA data = event.getData();
        //Ball ball = ((Response) data).getBall();

        AgHandlerBESA ah = null;
        Respuesta respuesta = (Respuesta) event.getData();

        EventBESA ev = null;
        FiboPingPongStatus estado = (FiboPingPongStatus) this.agent.getState();
        estado.setGolpes(respuesta.getGolpes() + 1);

        if (this.agent.getAlias().equalsIgnoreCase("jugador1")) {
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias("jugador2");
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Guarda_Respuesta.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias("jugador1");
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Guarda_Respuesta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Random rand = new Random();
        int falla = rand.nextInt(10);

        if (falla != 5) {
            if (data instanceof Ping) {
                System.out.println("Po, se puede cruzar estas con la de las líneas actuales y puede ser interesante y efectivos los resultados.ng");
                respuesta = new Pong(estado.getGolpes());
            } else {
                System.out.println("Ping");
                respuesta = new Ping(estado.getGolpes());
            }
            ev = new EventBESA(Guarda_Respuesta.class.getName(), respuesta);
        } else {
            System.out.println("Fallé");
            System.out.println("\nNuevo Punto \n");
            respuesta = null;
            ev = new EventBESA(Guarda_Punto.class.getName(), respuesta);
        }
        try {
            Delay();
            ah.sendEvent(ev);
        } catch (ExceptionBESA e) {
            e.printStackTrace();
        }
    }

    static int fib(int n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }

    public void Delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Guarda_Respuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
