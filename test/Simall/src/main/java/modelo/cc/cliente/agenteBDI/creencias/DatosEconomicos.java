/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.creencias;

import SIMALL.modelo.cc.agente.Producto;
import java.text.DecimalFormat;

/**
 *
 * @author dsvalencia
 */
public class DatosEconomicos {
    private double dineroPresupuestadoParaGasto;
    
    private double dineroDisponibleEfectivo;
    private double dineroDisponibleCredito;
    private double dineroDisponibleCuenta;
    private double porcentajeApetitoEndeudamiento; //0 mínimo a 1 máximo

    public DatosEconomicos(double dineroPresupuestadoParaGasto, double dineroDisponibleEfectivo, double dineroDisponibleCredito, double dineroDisponibleCuenta, double porcentajeApetitoEndeudamiento) {
        this.dineroPresupuestadoParaGasto = dineroPresupuestadoParaGasto;
        this.dineroDisponibleEfectivo = dineroDisponibleEfectivo;
        this.dineroDisponibleCredito = dineroDisponibleCredito;
        this.dineroDisponibleCuenta = dineroDisponibleCuenta;
        this.porcentajeApetitoEndeudamiento = porcentajeApetitoEndeudamiento;
    }

    DatosEconomicos(DatosEconomicos datosEconomicos) {
        this.dineroPresupuestadoParaGasto = datosEconomicos.dineroPresupuestadoParaGasto;
        this.dineroDisponibleEfectivo = datosEconomicos.dineroDisponibleEfectivo;
        this.dineroDisponibleCredito = datosEconomicos.dineroDisponibleCredito;
        this.dineroDisponibleCuenta = datosEconomicos.dineroDisponibleCuenta;
        this.porcentajeApetitoEndeudamiento = datosEconomicos.porcentajeApetitoEndeudamiento;
    }

    public double getDineroPresupuestadoParaGasto() {
        return dineroPresupuestadoParaGasto;
    }

    public void setDineroPresupuestadoParaGasto(double dineroPresupuestadoParaGasto) {
        this.dineroPresupuestadoParaGasto = dineroPresupuestadoParaGasto;
    }
    
    public double getDineroDisponibleEfectivo() {
        return dineroDisponibleEfectivo;
    }

    public void setDineroDisponibleEfectivo(double dineroDisponibleEfectivo) {
        this.dineroDisponibleEfectivo = dineroDisponibleEfectivo;
    }

    public double getDineroDisponibleCredito() {
        return dineroDisponibleCredito;
    }

    public void setDineroDisponibleCredito(double dineroDisponibleCredito) {
        this.dineroDisponibleCredito = dineroDisponibleCredito;
    }

    public double getDineroDisponibleCuenta() {
        return dineroDisponibleCuenta;
    }

    public void setDineroDisponibleCuenta(double dineroDisponibleCuenta) {
        this.dineroDisponibleCuenta = dineroDisponibleCuenta;
    }

    public double getPorcentajeApetitoEndeudamiento() {
        return porcentajeApetitoEndeudamiento;
    }

    public void setPorcentajeApetitoEndeudamiento(double porcentajeApetitoEndeudamiento) {
        this.porcentajeApetitoEndeudamiento = porcentajeApetitoEndeudamiento;
    }
    
    @Override
    public String toString(){
        String cadena="Datos Financieros:\n";
        cadena+="   Presupuesto:$"+ new DecimalFormat().format(dineroPresupuestadoParaGasto)+"\n";
        cadena+="   Efectivo:$"+ new DecimalFormat().format(dineroDisponibleEfectivo)+"\n";
        cadena+="   Crédito:$"+ new DecimalFormat().format(dineroDisponibleCredito)+"\n";
        cadena+="   Cuenta:$"+ new DecimalFormat().format(dineroDisponibleCuenta)+"\n";
        cadena+="   Apetito Endeudamiento: "+porcentajeApetitoEndeudamiento+"\n";
        return cadena;
    }
    
    public double getCapacidadEconomicaTotal(){
        return dineroDisponibleEfectivo+dineroDisponibleCredito+dineroDisponibleCuenta;
    }

    public boolean evaluarCapacidadPago(Producto producto) {
        return producto.getMaxPrecioMercado()<=this.dineroPresupuestadoParaGasto;
    }
}
