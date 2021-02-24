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
public class Data_Request extends DataBESA {
    
    //Indica si fue aceptada la solicitud
    private boolean aceptado;
    
    //Contenedor que va a recibir el agente
    private String contenedor;
    
    //Nombre del agente
    private String agente;
    
    //Clave del agente
    private double clave;
    
    //Datos del agente
    private Data_Agent agentData;

    public Data_Request(String contenedor, String agente, double clave) {
        this.aceptado = false;
        this.contenedor = contenedor;
        this.agente = agente;
        this.clave = clave;
        this.agentData = null;
    }

    public Data_Request() {
        this.aceptado=false;
        this.agentData = null;
        this.contenedor = "";
        this.agente = "";
        this.clave = 0.0;
    }

    public String getContenedor() {
        return contenedor;
    }

    public void setContenedor(String contenedor) {
        this.contenedor = contenedor;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public double getClave() {
        return clave;
    }

    public void setClave(double clave) {
        this.clave = clave;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    public Data_Agent getAgentData() {
        return agentData;
    }

    public void setAgentData(Data_Agent agentData) {
        this.agentData = agentData;
    }
    
}
