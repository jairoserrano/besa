/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CheckpointTest;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import Data.Data_Saque;
import Guardas.Guarda_Saque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bitas
 */
public class PingPongCountPair {

    String jugador;

    public void start(AdmBESA admLocal, String jugador) {

        // Activación del Checkpoint recurrente
        // @TODO: Cambiar a una variable de entorno para activarla o no, si no
        // se limpia la DB
        admLocal.activateCheckpoint();
        // Desactivación del Checkpoint recurrente
        //admLocal.deactivateCheckpoint();                
        
        if (admLocal.isCentralized() || jugador.equalsIgnoreCase("jugador1")) {
            
            try {
                AgHandlerBESA ah = admLocal.getHandlerByAlias(jugador);
                Data_Saque saque = new Data_Saque();
                EventBESA ev = new EventBESA(Guarda_Saque.class.getName(), saque);
                System.out.println("Saque");
                ah.sendEvent(ev);
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Jugador1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void Delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PingPongCountPair.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
