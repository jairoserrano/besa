/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import BESA.Kernel.Agent.StateBESA;
import java.io.Serializable;

/**
 *
 * @author bitas
 */
public class Estado_Ag_PingPong extends StateBESA implements Serializable {

    private int golpes;
    private int puntos;

    public Estado_Ag_PingPong() {
        super();

    }

    public synchronized void initState() {
        this.puntos = 0;
        this.golpes = 0;
    }

    public synchronized int getPuntos() {
        return puntos;
    }

    public synchronized int getGolpes() {
        return golpes;
    }

    public synchronized void setGolpes(int golpes) {
        this.golpes = golpes;
    }

    public synchronized void setPuntos(int contador) {
        this.puntos = contador;
    }

    public synchronized void incrementarPuntos() {
        this.puntos = puntos + 1;
    }

}
