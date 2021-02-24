/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logicas;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author bitas
 */
public class Respuesta extends DataBESA {

    int golpes;

    public Respuesta(int golpes) {
        this.golpes = golpes;
    }

    public int getGolpes() {
        return golpes;
    }

    public void setGolpes(int golpes) {
        this.golpes = golpes;
    }

}
