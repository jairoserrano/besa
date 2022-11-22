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
public class ConfSanitario extends ConfLugar{
    private String nombre;
    
    private int locales;

    public int getLocales() {
        return locales;
    }

    public void setLocales(int locales) {
        this.locales = locales;
    }

    private int unidades;

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    private int minTiempo;
    private int avgTiempo;
    private int maxTiempo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public int getMinTiempo() {
        return minTiempo;
    }

    public void setMinTiempo(int minTiempo) {
        this.minTiempo = minTiempo;
    }

    public int getAvgTiempo() {
        return avgTiempo;
    }

    public void setAvgTiempo(int avgTiempo) {
        this.avgTiempo = avgTiempo;
    }

    public int getMaxTiempo() {
        return maxTiempo;
    }

    public void setMaxTiempo(int maxTiempo) {
        this.maxTiempo = maxTiempo;
    }

    public ConfSanitario(String nombre, String categoria, int unidades, int locales, int minTiempo, int avgTiempo, int maxTiempo, int x, int y) {
        super(nombre, categoria, unidades, locales, x, y, false);
        this.nombre = nombre;
        this.locales=locales;
        this.unidades = unidades;
        this.minTiempo = minTiempo;
        this.avgTiempo = avgTiempo;
        this.maxTiempo = maxTiempo;
    }
    
    public ConfSanitario(ConfSanitario confSanitario) {
        super(confSanitario.nombre, confSanitario.nicho, confSanitario.unidades, confSanitario.locales, confSanitario.x, confSanitario.y, false);
        this.nombre = confSanitario.nombre;
        this.locales= confSanitario.locales;
        this.unidades = confSanitario.unidades;
        this.minTiempo = confSanitario.minTiempo;
        this.avgTiempo = confSanitario.avgTiempo;
        this.maxTiempo = confSanitario.maxTiempo;
    }
    
    @Override
    public String toString(){
        String cadena="Nombre: "+nombre;
        cadena+=" unidades="+unidades;
        cadena+=" minTiempo="+minTiempo;
        cadena+=" avgTiempo="+avgTiempo;
        cadena+=" maxTiempo="+maxTiempo;
        return cadena;
        
    }
    
    
}
