/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.util;

/**
 *
 * @author Daniel
 */
public class DistribucionProbabilidadTriangular {
    
    
    public DistribucionProbabilidadTriangular(double minimo, double medio, double maximo) {
        this.minimo = minimo;
        this.medio = medio;
        this.maximo = maximo;
    }
    
    private double minimo;
    private double medio;
    private double maximo;
            
   
    public double siguienteValor() {
        double F = (maximo - minimo) / (medio - minimo);
        double aleatorio = Math.random();
        if (aleatorio < F) {
            return minimo + Math.sqrt(aleatorio * (medio - minimo) * (maximo - minimo));
        } else {
            return medio - Math.sqrt((1 - aleatorio) * (medio - minimo) * (medio - maximo));
        }
    }
}
