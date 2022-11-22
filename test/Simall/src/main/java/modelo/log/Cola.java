/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SIMALL.modelo.log;

import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class Cola{

    private ArrayList lista;
    
    public Cola() {
        lista=new ArrayList();
    }
    
    public synchronized void push(Object objeto){
        lista.add(objeto);
    }
    
    public synchronized Object get(){
        if(lista.isEmpty()){
            return null;
        }
        Object o=lista.get(0);
        lista.remove(0);
        return o;
    }
    
    public synchronized boolean estaVacia(){
        return lista.isEmpty();
    }
    
}
