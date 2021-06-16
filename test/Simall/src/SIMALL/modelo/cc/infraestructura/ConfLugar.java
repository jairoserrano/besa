/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura;

/**
 *
 * @author Daniel
 */
public abstract class ConfLugar {
    
    public static final int X_DEFECTO=-1;
    public static final int Y_DEFECTO=-1;
    
    protected int x;
    protected int y;
    private boolean esVitrina;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    protected String nicho;

    public String getNicho() {
        return nicho;
    }

    public void setNicho(String nicho) {
        this.nicho = nicho;
    }
    
    protected String nombre;
    
    protected int locales;

    public int getLocales() {
        return locales;
    }

    public void setLocales(int locales) {
        this.locales = locales;
    }

    protected int unidades;

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public ConfLugar(String nombre, String nicho, int unidades, int locale, int x, int y, boolean esVitrina) {
        this.nombre = nombre;
        this.nicho = nicho;
        this.locales=locales;
        this.unidades = unidades;
        this.x=x;
        this.y=y;
        this.esVitrina=esVitrina;
    }
    
    @Override
    public String toString(){
        String cadena="Nombre: "+nombre;
        cadena+=" unidades="+unidades;
        cadena+=" locales="+locales;
        return cadena;
    }

    public boolean esVitrina() {
        return this.esVitrina;
    }
    
    
}
