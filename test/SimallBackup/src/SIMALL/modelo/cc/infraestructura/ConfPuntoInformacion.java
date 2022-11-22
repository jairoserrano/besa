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
public class ConfPuntoInformacion extends ConfLugar{
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

    public ConfPuntoInformacion(String nombre, String categoria, int unidades, int locales, int minTiempo, int avgTiempo, int maxTiempo, int x, int y) {
        super(nombre, categoria, unidades, locales, x, y, false);
        this.nombre = nombre;
        this.locales=locales;
        this.unidades = unidades;
        this.minTiempo = minTiempo;
        this.avgTiempo = avgTiempo;
        this.maxTiempo = maxTiempo;
    }
    
    public ConfPuntoInformacion(ConfPuntoInformacion confPuntoInformacion) {
        super(confPuntoInformacion.nombre, confPuntoInformacion.nicho, confPuntoInformacion.unidades, confPuntoInformacion.locales, confPuntoInformacion.x, confPuntoInformacion.y, false);
        this.nombre = confPuntoInformacion.nombre;
        this.locales= confPuntoInformacion.locales;
        this.unidades = confPuntoInformacion.unidades;
        this.minTiempo = confPuntoInformacion.minTiempo;
        this.avgTiempo = confPuntoInformacion.avgTiempo;
        this.maxTiempo = confPuntoInformacion.maxTiempo;
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
