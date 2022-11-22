/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.ia.fuzzy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class VariableDeSalida {
    
    public static final String METODO_COG="COG"; //Center of Gravity
    public static final String METODO_COGS="COGS";//Centre of Gravity for Singletons
    public static final String METODO_COA="COA";//Centre of Area
    public static final String METODO_LM="LM";//Left Most Maximum
    public static final String METODO_RM="RM";//Rght Most Maximum
    
    private String nombre;
    private ArrayList<TerminoLinguistico> terminosLinguisticos;
    private String metodo;
    private double defecto;
    
    private Point2D rango=null;

    public double getDefecto() {
        return defecto;
    }

    public void setDefecto(double defecto) {
        this.defecto = defecto;
    }

    public Point2D getRango() {
        return rango;
    }

    public void setRango(Point2D rango) {
        this.rango = rango;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

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

    public VariableDeSalida(String nombre, ArrayList<TerminoLinguistico> terminosLinguisticos, String metodo, double defecto) {
        this.nombre = nombre;
        this.terminosLinguisticos = terminosLinguisticos;
        this.metodo=metodo;
        this.defecto=defecto;
    }
    
    public VariableDeSalida(String nombre, ArrayList<TerminoLinguistico> terminosLinguisticos, String metodo, double defecto, Point2D rango) {
        this.nombre = nombre;
        this.terminosLinguisticos = terminosLinguisticos;
        this.metodo=metodo;
        this.defecto=defecto;
        this.rango=rango;
    }
    
    
}
