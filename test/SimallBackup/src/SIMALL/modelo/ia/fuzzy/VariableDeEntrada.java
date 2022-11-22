/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.ia.fuzzy;

import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class VariableDeEntrada {
    private String nombre;
    private ArrayList<TerminoLinguistico> terminosLinguisticos;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<TerminoLinguistico> getTerminosLinguisticos() {
        return terminosLinguisticos;
    }

    public void setTerminosLinguisticos(ArrayList<TerminoLinguistico> terminosLinguisticos) {
        this.terminosLinguisticos = terminosLinguisticos;
    }

    public VariableDeEntrada(String nombre, ArrayList<TerminoLinguistico> terminosLinguisticos) {
        this.nombre = nombre;
        this.terminosLinguisticos = terminosLinguisticos;
    }
    
}
