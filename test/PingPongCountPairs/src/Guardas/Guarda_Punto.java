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
import Data.Data_Saque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bitas
 */
public class Guarda_Punto extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {

        Estado_Ag_PingPong estado = (Estado_Ag_PingPong) this.agent.getState();
        System.out.println("\n Hice Punto con " + estado.getGolpes() + " golpes");
        estado.incrementarPuntos();
        System.out.println("Voy " + estado.getPuntos() + " puntos");

        AgHandlerBESA ah = null;
        if (this.agent.getAlias().equalsIgnoreCase("jugador1")) {
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias("jugador2");
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Guarda_Saque.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias("jugador1");
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Guarda_Saque.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Data_Saque saque = new Data_Saque();
        EventBESA ev = new EventBESA(Guarda_Saque.class.getName(), saque);
        try {
            System.out.println("\n Nuevo punto \n");
            System.out.println("Saque");
            Delay();
            ah.sendEvent(ev);
        } catch (ExceptionBESA e) {
            e.printStackTrace();
        }
    }

    public void Delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Guarda_Punto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
