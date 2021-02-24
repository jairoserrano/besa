/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BalancerTest;

import Agentes.Ag_PingPong;
import Agentes.Estado_Ag_PingPong;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import BESA.Remote.Balancer.BalancerBESA;
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

        double agentPasswd = 1234;
        Estado_Ag_PingPong estado = new Estado_Ag_PingPong();
        Ag_PingPong jugador = null;
        try {
            jugador = new Ag_PingPong("jugador1", estado, jugStruct, agentPasswd);
            jugador.start();
        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        }
        jugador = null;
        try {
            jugador = new Ag_PingPong("jugador2",estado, jugStruct, agentPasswd);   
            jugador.start();
        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        } 
        jugador = null;
        try {
            jugador = new Ag_PingPong("jugador3", estado, jugStruct, agentPasswd);
            jugador.start();
        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        }
        jugador = null;
        try {
            jugador = new Ag_PingPong("jugador4",estado, jugStruct, agentPasswd);   
            jugador.start();
        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        BalancerBESA balanceador = new BalancerBESA(10000,1000000000L);
        balanceador.initBalancer();
        
            Jugador.Delay();
        
        juego.start(admLocal, "jugador1");
        juego.start(admLocal, "jugador3");
        
    }

    public static void Delay() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Guarda_Punto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
