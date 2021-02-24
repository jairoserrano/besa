/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Guardas;

import Agentes.Estado_Ag_PingPong;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import Logicas.Ping;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bitas
 */
public class Guarda_Saque extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {

        AgHandlerBESA ah = null;

        Estado_Ag_PingPong estado = (Estado_Ag_PingPong) this.agent.getState();
        estado.setGolpes(1);

        if (this.agent.getAlias().equalsIgnoreCase("jugador1")) {
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias("jugador2");
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Guarda_Saque.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (this.agent.getAlias().equalsIgnoreCase("jugador2")) {
                try {
                    ah = this.agent.getAdmLocal().getHandlerByAlias("jugador1");
                } catch (ExceptionBESA ex) {
                    Logger.getLogger(Guarda_Saque.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (this.agent.getAlias().equalsIgnoreCase("jugador3")) {
                    try {
                        ah = this.agent.getAdmLocal().getHandlerByAlias("jugador4");
                    } catch (ExceptionBESA ex) {
                        Logger.getLogger(Guarda_Saque.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    if (this.agent.getAlias().equalsIgnoreCase("jugador4")) {
                        try {
                            ah = this.agent.getAdmLocal().getHandlerByAlias("jugador3");
                        } catch (ExceptionBESA ex) {
                            Logger.getLogger(Guarda_Saque.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }

            }

        }

        Ping ping = new Ping(estado.getGolpes());
        EventBESA ev = new EventBESA(Guarda_Respuesta.class.getName(), ping);
        try {
            //Delay();
            ah.sendEvent(ev);
            //System.out.println("Ping");
        } catch (ExceptionBESA e) {
            e.printStackTrace();
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
