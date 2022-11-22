/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;


import wpsMain.util.Aleatorio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dsvalencia
 */
public class Inventario {
    
    private String comercioPropietario;

    public Inventario(Inventario inventario) {
        this.comercioPropietario=inventario.comercioPropietario;
        this.registrosInventario=new HashMap(inventario.registrosInventario);
        
    }

    public String getComercioPropietario() {
        return comercioPropietario;
    }

    public void setComercioPropietario(String comercioPropietario) {
        this.comercioPropietario = comercioPropietario;
    }
    private HashMap <String, RegistroInventario> registrosInventario = new HashMap();
    
    public Inventario(){
        this.comercioPropietario="Desconocido";
    }

    public void agregarRegistroInventario(Producto producto, int unidad, double presentacion, double precioUnitario, int cantidadDisponible) {
        registrosInventario.put(producto.getNombre(), new RegistroInventario(producto, unidad, presentacion, precioUnitario, cantidadDisponible));
    }

    public void agregarInventario(ArrayList<RegistroInventario> registrosInventario) {
        for(RegistroInventario registroInventario:registrosInventario){
            if(!this.registrosInventario.containsKey(registroInventario.getProducto().getNombre())){
                this.registrosInventario.put(registroInventario.getProducto().getNombre(), registroInventario);
            }
        }
    }
    
    @Override
    public String toString(){
        String cadena="Inventario: \n\n";
        Set<String> llaves=registrosInventario.keySet();
        Iterator i=llaves.iterator();
        while(i.hasNext()){
            RegistroInventario registro=(RegistroInventario)registrosInventario.get(i.next());
            cadena+="\t"+registro.getProducto().getNombre()+"\t"+registro.getCantidadDisponible()+"\t$"+registro.getPrecioUnitario()+"\n";
        }
        return cadena;
    }

    public boolean contieneProducto(Producto producto) {
        Set<String> llaves=registrosInventario.keySet();
        Iterator i=llaves.iterator();
        while(i.hasNext()){
            RegistroInventario registro=(RegistroInventario)registrosInventario.get(i.next());
            if(registro!=null){
                if(registro.getProducto()==producto){
                    return true;
                }
            }
        }
        return false;
    }

    public Cotizacion generarCotizacion(Producto producto) {
        Set<String> llaves=registrosInventario.keySet();
        Iterator i=llaves.iterator();
        while(i.hasNext()){
            RegistroInventario registro=(RegistroInventario)registrosInventario.get((String)i.next());
            if(registro!=null){
                if(registro.getProducto()==producto){
                    if(registro.getCantidadDisponible()>0){
                        return new Cotizacion(registro.getProducto(), registro.getPrecioUnitario(), registro.getUnidad(), registro.getCantidadDisponible(), comercioPropietario);
                    }
                }
            }
        }
        return null;
    }

    public boolean descargarProducto(Producto producto) {
        Set<String> llaves=registrosInventario.keySet();
        Iterator i=llaves.iterator();
        while(i.hasNext()){
            RegistroInventario registro=(RegistroInventario)registrosInventario.get((String)i.next());
            if(registro!=null){
                if(registro.getProducto()==producto){
                    int cantidad=registro.getCantidadDisponible();
                    if(cantidad>0){
                        registro.setCantidadDisponible(--cantidad);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<Producto> getProductosEnPromocion() {
        if(registrosInventario!=null){
            if(registrosInventario.size()>0){
                ArrayList<Producto> productosEnPromocion=new ArrayList();
                ArrayList<RegistroInventario> registros=new ArrayList(registrosInventario.values());
                int cantidad=Aleatorio.getInstance().siguienteEnteroGeneracionClientes(0, registros.size());
                for(int i=0;i<cantidad;i++){
                    int indice=Aleatorio.getInstance().siguienteEnteroGeneracionClientes(0, registros.size()-1);
                    Producto producto=registros.get(indice).getProducto();
                    if(!productosEnPromocion.contains(producto)){
                        productosEnPromocion.add(producto);
                    }else{
                        i--;
                    }
                }
                return productosEnPromocion;
            }
        }
        return null;
    }
    
}

