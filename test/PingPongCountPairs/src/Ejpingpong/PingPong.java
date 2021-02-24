/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejpingpong;

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
public class PingPong {
    
    String jugador;
    
    public void start(AdmBESA admLocal, String jugador) {
    
        if (admLocal.isCentralized() || jugador.equalsIgnoreCase("jugador1")) {
            
            AgHandlerBESA ah = null;
            try {
                ah = admLocal.getHandlerByAlias(jugador);
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            Data_Saque saque = new Data_Saque();
            EventBESA ev = new EventBESA(Guarda_Saque.class.getName(), saque);
            try {
                System.out.println("\n Nuevo juego \n");
                System.out.println("Saque");
                Delay();
                ah.sendEvent(ev);
            } catch (ExceptionBESA e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void Delay (){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PingPong.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
}
