/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Guardas;

import Agentes.FiboPingPongStatus;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import Logicas.Ping;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bitas
 */
public class Guarda_Saque extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {

        AgHandlerBESA ah;

        FiboPingPongStatus estado = (FiboPingPongStatus) this.agent.getState();
        estado.setGolpes(1);
        Ping ping = new Ping(estado.getGolpes());
        EventBESA ev = new EventBESA(Guarda_Respuesta.class.getName(), ping);

        if (this.agent.getAlias().equalsIgnoreCase("jugador1")) {
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias("jugador2");
                Delay();
                ah.sendEvent(ev);
                System.out.println("Pong");
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Guarda_Saque.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias("jugador1");
                Delay();
                ah.sendEvent(ev);
                System.out.println("Ping");
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Guarda_Saque.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void Delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Guarda_Saque.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
