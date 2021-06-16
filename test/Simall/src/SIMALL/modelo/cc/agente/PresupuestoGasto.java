/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.agente;

import SIMALL.modelo.util.Punto;
import java.util.HashMap;

/**
 *
 * @author dsvalencia
 */
public class PresupuestoGasto {
    private HashMap<Integer,Punto> presupuestoGasto;

    public PresupuestoGasto(HashMap<Integer,Punto> presupuestoGasto) {
        this.presupuestoGasto = presupuestoGasto;
    }
    
    public Punto obtenerRangoPresupuestoGasto(int nivelEconomico){
        return (Punto)this.presupuestoGasto.get(nivelEconomico);
    }
}
