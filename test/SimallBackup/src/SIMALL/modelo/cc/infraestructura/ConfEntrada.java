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
public class ConfEntrada extends ConfLugar{

    public ConfEntrada(String nombre, String categoria, int unidades, int locales, int x, int y) {
        super(nombre, categoria, unidades, locales, x, y, false);
    }
    
    public ConfEntrada(ConfEntrada confEntrada) {
        super(confEntrada.nombre, confEntrada.nicho, confEntrada.unidades, confEntrada.locales, confEntrada.x, confEntrada.y, false);
    }
}
