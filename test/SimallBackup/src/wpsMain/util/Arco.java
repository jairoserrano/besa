/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.util;

import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author dsvalencia
 */
public class Arco {
    private Object origen;
    private Object destino;
    public Arco(Object origen, Object destino){
        this.origen=origen;
        this.destino=destino;
    }
    public Object getOrigen(){
        return origen;
    }
    public Object getDestino(){
        return destino;
    }
}


