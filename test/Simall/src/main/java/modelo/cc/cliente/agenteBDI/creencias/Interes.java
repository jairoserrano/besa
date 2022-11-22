/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.creencias;

import SIMALL.modelo.cc.agente.Categoria;


/**
 *
 * @author dsvalencia
 */
public class Interes {
    private Categoria categoria; 
    private double intensidad; //min 0, max 1

    public Interes(Categoria categoria, double intensidad) {
        this.categoria = categoria;
        this.intensidad = intensidad;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public double getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(double intensidad) {
        this.intensidad = intensidad;
    }
    
    @Override
    public String toString(){
        String cadena="Intentereses: \n";
        cadena+=categoria.getNombre();
        return cadena+=" Intensidad: "+intensidad;
    }
}
