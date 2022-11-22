/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import wpsMain.util.Punto;
import java.util.HashMap;

/**
 *
 * @author dsvalencia
 */
public class ApetitoEndeudamiento {
    private HashMap<Integer,Punto> apetitosEndeudamiento;

    public ApetitoEndeudamiento(HashMap<Integer,Punto> apetitosEndeudamiento) {
        this.apetitosEndeudamiento = apetitosEndeudamiento;
    }
    
    public Punto obtenerRangoApetitoEndeudamiento(int nivelEconomico){
        return (Punto)this.apetitosEndeudamiento.get(nivelEconomico);
    }
}
