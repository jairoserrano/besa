/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.agente;

import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class AtributoMercado {
    
    private ArrayList<RegistroAtributoMercado> registrosAtributoMercado;
    
    
    public AtributoMercado(){
        registrosAtributoMercado=new ArrayList();
    }
    
    public boolean agregarRegistroAtributoMercado(RegistroAtributoMercado registroAtributoMercado){
        if(registroAtributoMercado.getPorcentajeMercado()>=0&&registroAtributoMercado.getPorcentajeMercado()<=1){
            if(registrosAtributoMercado.isEmpty()){
                registrosAtributoMercado.add(registroAtributoMercado);
                return true;
            }
            else{
                double sumaPorcentajeMercadoExistente=0;
                boolean contieneRegistroAtributoMercado=false;
                RegistroAtributoMercado registroAtributoMercadoIdenticoExistente=null;
                for(RegistroAtributoMercado registroAtributoMercadoExistente:registrosAtributoMercado){
                    if(registroAtributoMercadoExistente.getValor().equals(registroAtributoMercado.getValor())){
                        contieneRegistroAtributoMercado=true;
                        registroAtributoMercadoIdenticoExistente=registroAtributoMercadoExistente;
                    }
                    sumaPorcentajeMercadoExistente+=registroAtributoMercadoExistente.getPorcentajeMercado();
                }
                if(contieneRegistroAtributoMercado && registroAtributoMercadoIdenticoExistente!=null){
                    sumaPorcentajeMercadoExistente-=registroAtributoMercadoIdenticoExistente.getPorcentajeMercado();
                    if(sumaPorcentajeMercadoExistente+registroAtributoMercado.getPorcentajeMercado()<=1){
                        registrosAtributoMercado.add(registroAtributoMercado);
                        return true;
                    }
                }else{
                    if(sumaPorcentajeMercadoExistente+registroAtributoMercado.getPorcentajeMercado()<=1){
                        registrosAtributoMercado.add(registroAtributoMercado);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public ArrayList<RegistroAtributoMercado> getRegistrosAtributoMercado(){
        return registrosAtributoMercado;
    }

    boolean evaluarCompatibilidad(Object valor) {
        for(RegistroAtributoMercado registroAtributoMercado:registrosAtributoMercado){
            if(registroAtributoMercado.evaluarCompatibilidad(valor)){
                return true;
            }
        }
        return false;
    }
}
