/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboPingPong;

import Agentes.FiboPingPongAgent;
import Agentes.FiboPingPongStatus;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import Guardas.Guarda_Punto;
import Guardas.Guarda_Respuesta;
import Guardas.Guarda_Saque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bitas
 */
public class Jugador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AdmBESA admLocal = AdmBESA.getInstance("config/Container.xml");

        PingPong juego = new PingPong();

        StructBESA jugStruct = new StructBESA();
        try {
            jugStruct.addBehavior("ComportamientoJugador");
            jugStruct.bindGuard("ComportamientoJugador", Guarda_Saque.class);
            jugStruct.bindGuard("ComportamientoJugador", Guarda_Respuesta.class);
            jugStruct.bindGuard("ComportamientoJugador", Guarda_Punto.class);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

        double agentPasswd = 0.91;
        FiboPingPongStatus estado = new FiboPingPongStatus();

        try {
            FiboPingPongAgent jugador;
            jugador = new FiboPingPongAgent("jugador1", estado, jugStruct, agentPasswd);
            jugador.start();
        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        }

        juego.start(admLocal, "jugador1");
    }

}
