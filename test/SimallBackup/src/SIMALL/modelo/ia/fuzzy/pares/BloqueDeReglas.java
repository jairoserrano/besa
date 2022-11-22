/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.ia.fuzzy.pares;

import SIMALL.modelo.ia.fuzzy.Condicion;
import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class BloqueDeReglas {
    public static final String MIN="MIN";
    public static final String MAX="MAX";
    
    private String and; //AND
    private String act; //Activation Method
    private String accu;//Accumulation Method
    
    private ArrayList<Condicion> condiciones;

    public BloqueDeReglas(String and, String act, String accu, ArrayList<Condicion> condiciones) {
        this.and = and;
        this.act = act;
        this.accu = accu;
        this.condiciones = condiciones;
    }

    public String getAnd() {
        return and;
    }

    public void setAnd(String and) {
        this.and = and;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getAccu() {
        return accu;
    }

    public void setAccu(String accu) {
        this.accu = accu;
    }

    public ArrayList<Condicion> getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(ArrayList<Condicion> condiciones) {
        this.condiciones = condiciones;
    }
    
}
