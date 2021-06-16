/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.ia.fuzzy;

import SIMALL.modelo.util.Punto;
import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class TerminoLinguistico {

    public TerminoLinguistico(String nombre, ArrayList<Punto> puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Punto> getPuntos() {
        return puntos;
    }

    public void setPuntos(ArrayList<Punto> puntos) {
        this.puntos = puntos;
    }
    private String nombre;
    ArrayList<Punto> puntos;
    
    
    
}
