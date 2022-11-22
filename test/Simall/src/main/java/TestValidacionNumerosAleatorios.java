
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL;

import SIMALL.modelo.util.Aleatorio;

/**
 *
 * @author dsvalencia
 */
public class TestValidacionNumerosAleatorios {
    public static void main(String[] args) {
        
        Aleatorio aleatorio=Aleatorio.getInstance();
        int j=0;
        int cantidad=10000000;
        int cantidad1=0;
        int cantidad0=0;
        int[] datos=new int[cantidad];
        for(int i=0;i<cantidad;i++){
            datos[i]=aleatorio.siguienteEnteroGeneracionClientes(0, 1);
        }
        for(int i=0;i<cantidad;i++){
            if(datos[i]==0){
                cantidad0++;
            }
            if(datos[i]==1){
                cantidad1++;
            }
            if(datos[i]!=0&&datos[i]!=1){
                System.err.println("Error :"+datos[i]);
                System.exit(0);
            }
        }
        System.out.println("0="+(double)(cantidad0*100)/(double)cantidad);
        System.out.println("1="+(double)(cantidad1*100)/(double)cantidad);
        
        double b=0;
        for(int i=0;i<cantidad;i++){
            double a=aleatorio.siguienteDobleGeneracionClientes(0, 1);
            b+=a;
            if(a<-1||a>1){
                System.err.println("Error 2:"+a);
                System.exit(0);
            }
        }
        System.out.println("b="+b/cantidad);
        
    }
}
