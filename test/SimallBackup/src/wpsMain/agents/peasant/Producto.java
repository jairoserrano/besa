/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

/**
 *
 * @author dsvalencia
 */
public class Producto {
    
    private String nombre;
    private double minPrecioMercado;
    private double avgPrecioMercado;
    private double maxPrecioMercado;
    private String marca;
    private int unidad;
    private double presentacion;
    private String nicho;
    private String categoria;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNicho() {
        return nicho;
    }

    public void setNicho(String nicho) {
        this.nicho = nicho;
    }

    public Producto(String nombre, String marca, double minPrecioMercado, double avgPrecioMercado, double maxPrecioMercado, double presentacion, int unidad, String nicho, String categoria) {
        this.nombre = nombre;
        this.marca = marca;
        this.minPrecioMercado = minPrecioMercado;
        this.avgPrecioMercado = avgPrecioMercado;
        this.maxPrecioMercado = maxPrecioMercado;
        this.presentacion=presentacion;
        this.unidad=unidad;
        this.nicho=nicho;
        this.categoria=categoria;
    }

    public Producto(Producto producto) {
        this.nombre = producto.nombre;
        this.marca = producto.marca;
        this.minPrecioMercado = producto.minPrecioMercado;
        this.avgPrecioMercado = producto.avgPrecioMercado;
        this.maxPrecioMercado = producto.maxPrecioMercado;
        this.presentacion=producto.presentacion;
        this.unidad=producto.unidad;
        this.nicho=producto.nicho;
        this.categoria=producto.categoria;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getMinPrecioMercado() {
        return minPrecioMercado;
    }

    public void setMinPrecioMercado(double minPrecioMercado) {
        this.minPrecioMercado = minPrecioMercado;
    }

    public double getAvgPrecioMercado() {
        return avgPrecioMercado;
    }

    public void setAvgPrecioMercado(double avgPrecioMercado) {
        this.avgPrecioMercado = avgPrecioMercado;
    }
    
    

    public double getMaxPrecioMercado() {
        return maxPrecioMercado;
    }

    public void setMaxPrecioMercado(double maxPrecioMercado) {
        this.maxPrecioMercado = maxPrecioMercado;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public double getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(double presentacion) {
        this.presentacion = presentacion;
    }
    
    

    @Override
    public String toString(){
        String cadena="Producto: "+nombre+" Marca:"+marca+"\n";
        return cadena;
    }
}
