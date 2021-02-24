/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Remote.Balancer;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author bitas
 */
public class Data_Agent extends DataBESA {
    
    //Contenedor actual del agente
    String contenedor;
    
    //Alias del agente a enviar
    String alias;
    
    //Contraseña deñ agente a enviar
    double passwd;
    
    //Carga del agente a enviar
    long carga;
    
    public Data_Agent(String alias, long carga, String contenedor, double passwd) {
        this.alias = alias;
        this.carga = carga;
        this.contenedor = contenedor;
        this.passwd = passwd;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public long getCarga() {
        return carga;
    }

    public void setCarga(long carga) {
        this.carga = carga;
    }

    public String getContenedor() {
        return contenedor;
    }

    public void setContenedor(String contenedor) {
        this.contenedor = contenedor;
    }

    public double getPasswd() {
        return passwd;
    }

    public void setPasswd(double passwd) {
        this.passwd = passwd;
    }
    
}
