/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.util;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Daniel
 */
public class Aleatorio {
    private static final Aleatorio ALEATORIO=new Aleatorio();
    
    private static final Random r=new Random(System.currentTimeMillis());
    
    private static final Random rArregloLocales=new Random(100);
    
    private static final Random rGeneracionClientes=new Random(200);
    
    private Aleatorio(){
            
    }
    
    public static Aleatorio getInstance(){
        return ALEATORIO;
    }
    
    public int siguienteEntero(int minimo, int maximo){
        int n = maximo - minimo + 1;
        int i = Math.abs(r.nextInt()) % n;
        return minimo + i;
    }
    
    public int siguienteEnteroArregloLocales(int minimo, int maximo){
        int n = maximo - minimo + 1;
        int i = Math.abs(rArregloLocales.nextInt()) % n;
        return minimo + i;
    }
    
    public int siguienteEnteroGeneracionClientes(int minimo, int maximo){
        int n = maximo - minimo + 1;
        int i = Math.abs(rGeneracionClientes.nextInt()) % n;
        return minimo + i;
    }
    
    public double siguienteDobleGeneracionClientes(double minimo, double maximo){
        if(minimo<0||maximo<0){
            System.out.println("Error en siguienteDobleGeneracionClientes: No se permiten valores negativos: "+minimo+", "+maximo+".");
            System.exit(0);
        }
        double n = maximo - minimo;
        double i = Math.abs(rGeneracionClientes.nextDouble()*(Math.pow(10,((int)Math.log10(n))))) % n;
        return minimo + i;
    }
    
    public int siguienteOpcionGeneracionClientes(double[] porcentajes){
        if(porcentajes.length>0){
            double offset=0;
            ArrayList<Punto> rangos=new ArrayList();
            for(int i=0;i<porcentajes.length;i++){
                rangos.add(new Punto(offset,offset+porcentajes[i]));
                offset+=porcentajes[i];
            }
            double valorAleatorio=siguienteDobleGeneracionClientes(0,1);
            for(int i=0;i<rangos.size();i++){
                Punto rango=rangos.get(i);
                if(valorAleatorio>=rango.getX()&&valorAleatorio<=rango.getY()){
                    return i;
                }
            }
            System.out.println("test");
        }
        System.err.print("Se presentó un error al generar una de las siguiente porcentajes: ");
        for(int i=0;i<porcentajes.length;i++){
            System.err.print("{"+porcentajes[i]+"}");
        }
        return -1;
    }
}

