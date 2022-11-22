/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import wpsMain.agents.peasant.Producto;
import wpsMain.agents.peasant.Necesidad;
import java.util.Date;

/**
 *
 * @author dsvalencia
 */
public class FacturaVenta {
    
    private int consecutivoVenta;
    private double precio;
    private double efectivo;
    private double debito;
    private double credito;
    private int cantidad;
    private int unidad;
    private Date fecha;
    private String cliente;
    private Producto producto;
    private Necesidad necesidad;

    public FacturaVenta(Necesidad necesidad, int consecutivoVenta, double precio, int cantidad, int unidad, Date fecha, String cliente, Producto producto, double efectivo, double debito, double credito) {
        this.necesidad=necesidad;
        this.consecutivoVenta = consecutivoVenta;
        this.precio = precio;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.fecha = fecha;
        this.cliente = cliente;
        this.producto = producto;
        this.efectivo=efectivo;
        this.debito=debito;
        this.credito=credito;
    }

    public Necesidad getNecesidad() {
        return necesidad;
    }

    public void setNecesidad(Necesidad necesidad) {
        this.necesidad = necesidad;
    }

    public double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(double efectivo) {
        this.efectivo = efectivo;
    }

    public double getDebito() {
        return debito;
    }

    public void setDebito(double debito) {
        this.debito = debito;
    }

    public double getCredito() {
        return credito;
    }

    public void setCredito(double credito) {
        this.credito = credito;
    }

    public int getConsecutivoVenta() {
        return consecutivoVenta;
    }

    public void setConsecutivoVenta(int consecutivoVenta) {
        this.consecutivoVenta = consecutivoVenta;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
}
