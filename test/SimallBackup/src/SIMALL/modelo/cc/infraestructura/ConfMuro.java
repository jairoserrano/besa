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
public class ConfMuro extends ConfLugar{

    public ConfMuro(String nombre, String categoria, int unidades, int locales, int x, int y, boolean esVitrina) {
        super(nombre, categoria, unidades, locales, x, y, esVitrina);
    }
    
    public ConfMuro(ConfMuro confMuro) {
        super(confMuro.nombre, confMuro.nicho, confMuro.unidades, confMuro.locales, confMuro.x, confMuro.y, confMuro.esVitrina());
    }
}
