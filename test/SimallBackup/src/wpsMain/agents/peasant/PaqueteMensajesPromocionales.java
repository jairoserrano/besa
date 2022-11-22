/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.Kernel.Agent.Event.DataBESA;
import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class PaqueteMensajesPromocionales extends DataBESA{
    
    private ArrayList<ArrayList<MensajePromocional>> totalMensajesPromocionales;

    public ArrayList<ArrayList<MensajePromocional>> getTotalMensajesPromocionales() {
        return totalMensajesPromocionales;
    }

    public void setMensajesPromocionales(ArrayList<ArrayList<MensajePromocional>> totalMensajesPromocionales) {
        this.totalMensajesPromocionales = totalMensajesPromocionales;
    }

    public PaqueteMensajesPromocionales(ArrayList<ArrayList<MensajePromocional>> totalMensajesPromocionales) {
        this.totalMensajesPromocionales = totalMensajesPromocionales;
    }
}
