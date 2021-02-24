/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BalancerTest;

import BESA.Kernel.System.AdmBESA;
import BESA.Remote.Balancer.BalancerBESA;

/**
 *
 * @author bitas
 */
public class Jugador3 {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {

        AdmBESA admLocal = AdmBESA.getInstance("config/Container3.xml");
        
        BalancerBESA balanceador = new BalancerBESA(60000, 2000000000L);
        balanceador.initBalancer();
        
    }

  

}
