/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.infraestructura.lugares.comercio.agente;

import BESA.Kernel.Agent.StructBESA;
import SIMALL.modelo.cc.agente.Cotizacion;
import SIMALL.modelo.cc.agente.Inventario;
import SIMALL.modelo.cc.agente.Producto;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.DatosEconomicos;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.Necesidad;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;
import SIMALL.modelo.cc.infraestructura.lugar.agente.guardas.AgregarClienteGuard;
import SIMALL.modelo.cc.infraestructura.lugar.agente.guardas.EliminarClienteGuard;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.util.DistribucionProbabilidadTriangular;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class ComercioState extends LugarState {
    
    private int consecutivoVenta=0;
    
    private Inventario inventario;
    private ArrayList<FacturaVenta> facturasVenta;
    private int impulsadores=0;

    public int getImpulsadores() {
        return impulsadores;
    }

    public void setImpulsadores(int impulsadores) {
        this.impulsadores = impulsadores;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public ComercioState(String alias, String nicho, DistribucionProbabilidadTriangular tiempoServicio, DistribucionProbabilidadTriangular valorProductos, DistribucionProbabilidadTriangular ventas, int x, int y, int unidades, Inventario inventario, int impulsadores) {
        super(alias, nicho, tiempoServicio, valorProductos, ventas, x, y, unidades);
        this.impulsadores=impulsadores;
        this.inventario=new Inventario(inventario);
        this.inventario.setComercioPropietario(alias);
        this.facturasVenta=new ArrayList();
        try{
            StructBESA comercioStruct = new StructBESA();
            comercioStruct.addBehavior("AgregarClienteGuard");
            comercioStruct.bindGuard("AgregarClienteGuard",AgregarClienteGuard.class);                        
            comercioStruct.addBehavior("EliminarClienteGuard");
            comercioStruct.bindGuard("EliminarClienteGuard",EliminarClienteGuard.class);                        
            ComercioAgent almacenAgent = new ComercioAgent(alias, this, comercioStruct, 0.91); 
            almacenAgent.start();
        }catch (Exception e) {
            LogAuditoria.getInstance().escribirError(this.getClass().getName(),alias,e);
        }

    }

    public Cotizacion generarCotizacion(Producto producto) {
        return inventario.generarCotizacion(producto);
    }

    public synchronized FacturaVenta comprar(Necesidad necesidadEnCurso, String nombreCliente, DatosEconomicos datosEconomicos) {
        FacturaVenta factura=null;
        double efectivo=0;
        double debito=0;
        double credito=0;
        double saldo=0;
        if(necesidadEnCurso!=null){
            Cotizacion cotizacion=inventario.generarCotizacion(necesidadEnCurso.getProducto());
            if(inventario.descargarProducto(necesidadEnCurso.getProducto())){
                if(datosEconomicos.getCapacidadEconomicaTotal()>=cotizacion.getPrecioUnitario()){
                    if(datosEconomicos.getDineroPresupuestadoParaGasto()>=cotizacion.getPrecioUnitario()){
                        if(datosEconomicos.getDineroDisponibleEfectivo()>=cotizacion.getPrecioUnitario()){
                            efectivo=cotizacion.getPrecioUnitario();
                            datosEconomicos.setDineroDisponibleEfectivo(datosEconomicos.getDineroDisponibleEfectivo()-efectivo);
                        }
                        else{
                            efectivo=datosEconomicos.getDineroDisponibleEfectivo();
                            saldo=cotizacion.getPrecioUnitario()-efectivo;
                            datosEconomicos.setDineroDisponibleEfectivo(0);
                            if(datosEconomicos.getDineroDisponibleCuenta()>=saldo){
                                debito=saldo;
                                datosEconomicos.setDineroDisponibleCuenta(datosEconomicos.getDineroDisponibleCuenta()-debito);
                            }
                            else{
                                debito=datosEconomicos.getDineroDisponibleCuenta();
                                saldo=saldo-debito;
                                datosEconomicos.setDineroDisponibleCuenta(0);
                                if(datosEconomicos.getDineroDisponibleCredito()>=saldo){
                                    credito=saldo;
                                    datosEconomicos.setDineroDisponibleCredito(datosEconomicos.getDineroDisponibleCredito()-credito);
                                }
                                else{
                                    System.err.println("No le alcanza el saldo para su compra?");
                                    System.exit(0);
                                }
                            }
                        }
                    }
                }
                consecutivoVenta++;
                factura=new FacturaVenta(necesidadEnCurso, consecutivoVenta, cotizacion.getPrecioUnitario(), cotizacion.getCantidad(), 1, new Date(), nombreCliente, cotizacion.getProucto(), efectivo, debito, credito);
                facturasVenta.add(factura);
            }
        }
        return factura;
    }

    public int getTotalCantidadVentasRealizadas() {
        if(facturasVenta!=null){
            return facturasVenta.size();
        }
        return 0;
    }
    
    public double getTotalValorVentasRealizadas(){
        double totalVentas=0;
        if(facturasVenta!=null){
            for(FacturaVenta factura:facturasVenta){
                totalVentas+=factura.getPrecio();
            }
        }
        return totalVentas;
    }
    
    
    
}
