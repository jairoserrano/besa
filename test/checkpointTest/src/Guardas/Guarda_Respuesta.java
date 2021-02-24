/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Guardas;

import Agentes.Estado_Ag_PingPong;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import Logicas.Ping;
import Logicas.Pong;
import Logicas.Respuesta;
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

        AgHandlerBESA ah = null;
        Respuesta respuesta = (Respuesta) event.getData();

        EventBESA ev = null;
        Estado_Ag_PingPong estado = (Estado_Ag_PingPong) this.agent.getState();
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

        if (estado.getGolpes() >= 500) {
            
            System.out.println("Tarea terminada");
            respuesta = null;
            ev = new EventBESA(Guarda_Punto.class.getName(), respuesta);

        } else {

            if (data instanceof Ping) {
                System.out.println("Pong " + estado.getGolpes());
                respuesta = new Pong(estado.getGolpes());
            } else {
                System.out.println("Ping " + estado.getGolpes());
                respuesta = new Ping(estado.getGolpes());
            }
            ev = new EventBESA(Guarda_Respuesta.class.getName(), respuesta);

        }
        try {
            Delay();
            ah.sendEvent(ev);
            //Guardado de estado agente individual @jairo
            //this.agent.saveAgent();
        } catch (ExceptionBESA e) {
            e.printStackTrace();
        }
    }

    public void Delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Guarda_Respuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
