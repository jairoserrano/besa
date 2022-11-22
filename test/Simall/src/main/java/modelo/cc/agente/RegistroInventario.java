/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.agente;

import java.text.DecimalFormat;

/**
 *
 * @author dsvalencia
 */
public class RegistroInventario {
    
    public static final int TIPO_UND=0;
    public static final int TIPO_LB=1;
    public static final int TIPO_G=2;
    public static final int TIPO_ML=3;
    public static final int TIPO_LT=4;
    public static final int TIPO_MG=5;
    
    private double precioUnitario;
    private double presentacion;
    private int cantidadDisponible;
    private int unidad;
    private Producto producto;

    public RegistroInventario(Producto producto, int unidad, double presentacion, double precioUnitario, int cantidadDisponible) {
        this.precioUnitario = precioUnitario;
        this.presentacion = presentacion;
        this.cantidadDisponible = cantidadDisponible;
        this.unidad = unidad;
        this.producto = producto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precio) {
        this.precioUnitario = precio;
    }

    public double getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(double presentacion) {
        this.presentacion = presentacion;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    
    @Override
    public String toString(){
        String cadena="";
        String sUnidad="Sin Unidad";
        switch(unidad){
            case TIPO_UND: sUnidad="UND"; break;
            case TIPO_G: sUnidad="G"; break;
            case TIPO_MG: sUnidad="MG"; break;
            case TIPO_ML: sUnidad="ML"; break;
            case TIPO_LT: sUnidad="LT"; break;
            case TIPO_LB: sUnidad="LB"; break;
        }
        cadena+=producto;
        if(precioUnitario!=-1){
            cadena+=" Precio:$"+new DecimalFormat().format(precioUnitario);
        }
        else{
            System.err.println("Precio es -1 en registro inventario de "+producto.getNombre());
            System.exit(0);
        }
        if(cantidadDisponible!=-1){
            cadena+=" Cantidad:"+cantidadDisponible;
        }
        else{
            System.err.println("Cantidad es -1 en registro inventario de "+producto.getNombre());
            System.exit(0);
        }
        if(unidad!=-1){
            cadena+=" Unidad:"+sUnidad;
        }
        else{
            System.err.println("Unidad es -1 en registro inventario de "+producto.getNombre());
            System.exit(0);
        }
        return cadena;
    }
}
