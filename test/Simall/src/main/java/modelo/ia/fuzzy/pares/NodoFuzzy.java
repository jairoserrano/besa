/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.ia.fuzzy.pares;

import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class NodoFuzzy {
    
    public static final int TIPO_PROPORCION_POSITIVA=0;
    public static final int TIPO_PROPORCION_NEGATIVA=1;
    
    private double valor;
    
    private int tipoProporcion;
    
    private String nombre;
    private String objeto;
    private String metodo;
    private ArrayList<NodoFuzzy> nodosFuzzyHijos;

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getTipoProporcion() {
        return tipoProporcion;
    }

    public void setTipoProporcion(int tipoProporcion) {
        this.tipoProporcion = tipoProporcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String clase) {
        this.metodo = clase;
    }

    public ArrayList<NodoFuzzy> getNodosFuzzyHijos() {
        return nodosFuzzyHijos;
    }

    public void setNodosFuzzyHijos(ArrayList<NodoFuzzy> nodosFuzzyHijos) {
        this.nodosFuzzyHijos = nodosFuzzyHijos;
    }

    public NodoFuzzy(int tipoProporcion, String nombre, String objeto, String clase) {
        this.valor = -1;
        this.tipoProporcion = tipoProporcion;
        this.nombre = nombre;
        this.objeto = objeto;
        this.metodo = clase;
        this.nodosFuzzyHijos = new ArrayList();
    }
    
    public NodoFuzzy(int tipoProporcion, String nombre, ArrayList<NodoFuzzy> nodosFuzzyHijos) {
        this.valor = -1;
        this.tipoProporcion = tipoProporcion;
        this.nombre = nombre;
        this.objeto = "";
        this.metodo = "";
        this.nodosFuzzyHijos = nodosFuzzyHijos;
    }
    
    public NodoFuzzy() {
        this.valor = -1;
        this.tipoProporcion = -1;
        this.nombre = "";
        this.objeto = "";
        this.metodo = "";
        this.nodosFuzzyHijos = new ArrayList();
    }
    
    public NodoFuzzy(NodoFuzzy nodoFuzzy) {
        this.valor = nodoFuzzy.valor;
        this.tipoProporcion = nodoFuzzy.tipoProporcion;
        this.nombre = nodoFuzzy.nombre;
        this.objeto = nodoFuzzy.objeto;
        this.metodo = nodoFuzzy.metodo;
        this.nodosFuzzyHijos = new ArrayList(nodoFuzzy.nodosFuzzyHijos);
    }
    
    @Override
    public String toString(){
        return toStringRecursivo(this,0);
    }
    
    private String toStringRecursivo(NodoFuzzy nodoFuzzy, int nivel){
        String cadena="";
        for(int i=0;i<nivel;i++){
            cadena+="\t";
        }
        switch(nodoFuzzy.tipoProporcion){
            case TIPO_PROPORCION_POSITIVA:  cadena+="+ ";
                                            break;
            case TIPO_PROPORCION_NEGATIVA:  cadena+="- ";
                                            break;
            default:                        cadena+="? "+tipoProporcion;
                                            break;
        }
        if(nodoFuzzy.valor==-1){
            cadena+="Valor: [Sin Asignar]";
        }
        else{
            cadena+="Valor: "+nodoFuzzy.valor;
        }
        cadena+=" Nombre: "+nodoFuzzy.nombre+" Objeto: "+nodoFuzzy.objeto+" Método: "+nodoFuzzy.metodo+"\n";
        for(int i=0;i<nodoFuzzy.nodosFuzzyHijos.size();i++){
            if(i==0){
                nivel++;
            }
            cadena+=toStringRecursivo(nodoFuzzy.nodosFuzzyHijos.get(i),nivel);
        }
        return cadena;
    }
}
