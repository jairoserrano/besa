/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.util;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author Daniel
 */
public class Mensaje extends DataBESA{
    
    private Object mensaje;

    public Object get() {
        return mensaje;
    }

    public void setMensaje(Object mensaje) {
        this.mensaje = mensaje;
    }

    public Mensaje(Object mensaje) {
        this.mensaje = mensaje;
    }
}
