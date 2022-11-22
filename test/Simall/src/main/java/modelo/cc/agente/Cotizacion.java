/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.agente;

/**
 *
 * @author dsvalencia
 */
public class Cotizacion {
    private Producto proucto;
    private double precioUnitario;
    private int unidad;
    private int cantidad;
    private String nombreComercio;

    public Cotizacion(Producto proucto, double precioUnitario, int unidad, int cantidad, String nombreComercio) {
        this.proucto = proucto;
        this.precioUnitario = precioUnitario;
        this.unidad = unidad;
        this.cantidad = cantidad;
        this.nombreComercio = nombreComercio;
    }

    public Producto getProucto() {
        return proucto;
    }

    public void setProucto(Producto proucto) {
        this.proucto = proucto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombreComercio() {
        return nombreComercio;
    }

    public void setNombreComercio(String nombreComercio) {
        this.nombreComercio = nombreComercio;
    }
}
