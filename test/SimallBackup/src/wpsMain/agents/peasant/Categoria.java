/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import wpsMain.util.Aleatorio;
import java.util.ArrayList;


/**
 *
 * @author dsvalencia
 */
public class Categoria {
    private String id;
    private String nombre;
    private ArrayList<Categoria> subCategorias;
    private MercadoObjetivo mercadoObjetivo;

    public MercadoObjetivo getMercadoObjetivo() {
        return mercadoObjetivo;
    }

    public void setMercadoObjetivo(MercadoObjetivo mercadoObjetivo) {
        this.mercadoObjetivo = mercadoObjetivo;
    }
    private ArrayList<Producto> productos;

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    public Categoria(String id, String nombre, ArrayList<Categoria> subCategorias, ArrayList<Producto> productos, MercadoObjetivo mercadoObjetivo) {
        this.id = id;
        this.nombre = nombre;
        this.subCategorias = subCategorias;
        this.productos=productos;
        this.mercadoObjetivo=mercadoObjetivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Categoria> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(ArrayList<Categoria> subCategorias) {
        this.subCategorias = subCategorias;
    }
    
    @Override
    public String toString(){
        return toStringRecursivo(this,0);
    }
    
    private String toStringRecursivo(Categoria categoria, int nivel){
        String cadena="";
        for(int i=0;i<nivel;i++){
            cadena+="\t";
        }
        cadena+=categoria.getId()+" - "+categoria.getNombre()+"\n";
        if(!categoria.mercadoObjetivo.esMercadoObjetivoPorDefecto()){
            for(int i=0;i<nivel+1;i++){
                cadena+="\t";
            }
            cadena+=categoria.mercadoObjetivo+"\n";
        }
        for(Producto producto:categoria.productos){
            for(int i=0;i<nivel+1;i++){
                cadena+="\t";
            }
            cadena+=producto;
        }
        for(int i=0;i<categoria.subCategorias.size();i++){
            if(i==0){
                nivel++;
            }
            cadena+=toStringRecursivo(categoria.subCategorias.get(i),nivel);
        }
        return cadena;
    }
    
    public Categoria getSubCategoria(String nombre){
        for(Categoria subCategoria:subCategorias){
            if(subCategoria.getNombre().equals(nombre)){
                return subCategoria;
            }
        }
        return null;
    }

    

    public void getProductosAsequiblesRecursivo(DatosDemograficos datosDemograficos, DatosEconomicos datosEconomicos, ArrayList<Producto> productosAsequibles) {
        if(this.productos!=null){
            for(Producto producto:productos){
                String nicho="Ninguno";
                if(this.nombre.equalsIgnoreCase("Restaurante")||MapaEstructural.getInstance().getArbolCategorias().getCategoria("Restaurante").getSubCategoria(this.nombre)!=null){
                    nicho="Restaurante";
                }
                producto.setNicho(nicho);
            }
        }
        if(mercadoObjetivo.evaluarCompatibilidad(datosDemograficos)){
            for(Producto productoDeCategoria:productos){
                if(datosEconomicos.evaluarCapacidadPago(productoDeCategoria)){
                    boolean productoExistente=false;
                    for(Producto productoAsequible:productosAsequibles){
                        if(productoAsequible.getNombre().equalsIgnoreCase(productoDeCategoria.getNombre())&&productoAsequible.getMarca().equalsIgnoreCase(productoDeCategoria.getMarca())){
                            productoExistente=true;
                            break;
                        }
                    }
                    if(!productoExistente){
                        productosAsequibles.add(productoDeCategoria);
                    }
                }
            }
        }
        for(Categoria subCategoria:subCategorias){
            if(subCategoria.mercadoObjetivo.evaluarCompatibilidad(datosDemograficos)){
                subCategoria.getProductosAsequiblesRecursivo(datosDemograficos, datosEconomicos, productosAsequibles);
            }
        }
    }

    public ArrayList<Categoria> getCategoriasAsequibles(DatosDemograficos datosDemograficos, DatosEconomicos datosEconomicos) {
        ArrayList<Categoria> categoriasAsequibles=new ArrayList();
        if(mercadoObjetivo.evaluarCompatibilidad(datosDemograficos)){
            categoriasAsequibles.add(this);
        }
        for(Categoria subCategoria:subCategorias){
            categoriasAsequibles.addAll(subCategoria.getCategoriasAsequibles(datosDemograficos, datosEconomicos));
        }
        return categoriasAsequibles;
    }

    public ArrayList<Necesidad> getNecesidadesAsequiblesAleatorias(DatosDemograficos datosDemograficos, DatosEconomicos datosEconomicos, ComponenteEmocional componenteEmocional) {
        Aleatorio aleatorio=Aleatorio.getInstance();
        ArrayList<Necesidad> necesidadaesAsequiblesAleatorias=new ArrayList();
        ArrayList<Categoria> categoriasAsequibles=getCategoriasAsequibles(datosDemograficos, datosEconomicos);
        ArrayList<Producto> productosAsequibles=new ArrayList();
        for(int i=0;i<categoriasAsequibles.size();i++){
           categoriasAsequibles.get(i).getProductosAsequiblesRecursivo(datosDemograficos, datosEconomicos,productosAsequibles);
        }
        int cantidadNecesidadesAsequiblesAleatorias=aleatorio.siguienteEnteroGeneracionClientes((int)(productosAsequibles.size()*0.2), productosAsequibles.size());
        for(int i=0;i<cantidadNecesidadesAsequiblesAleatorias;i++){
            int indiceProductoAsequibleAleatorio=aleatorio.siguienteEnteroGeneracionClientes(0, productosAsequibles.size()-1);
            necesidadaesAsequiblesAleatorias.add(new Necesidad(productosAsequibles.get(indiceProductoAsequibleAleatorio),componenteEmocional,(float)Aleatorio.getInstance().siguienteDobleGeneracionClientes(0, 1)));
            productosAsequibles.remove(indiceProductoAsequibleAleatorio);            
        }
        return necesidadaesAsequiblesAleatorias;
    }

    public ArrayList<Interes> getInteresesAsequiblesAleatorios(DatosDemograficos datosDemograficos, DatosEconomicos datosEconomicos) {
        Aleatorio aleatorio=Aleatorio.getInstance();
        ArrayList<Interes> interesesAsequiblesAleatorios=new ArrayList();
        ArrayList<Categoria> categoriasAsequibles=getCategoriasAsequibles(datosDemograficos, datosEconomicos);
        int cantidadInteresesAsequiblesAleatorios=aleatorio.siguienteEnteroGeneracionClientes(0, categoriasAsequibles.size());
        for(int i=0;i<cantidadInteresesAsequiblesAleatorios;i++){
            int indiceCategoriaAsequibleAleatoria=aleatorio.siguienteEnteroGeneracionClientes(0, categoriasAsequibles.size()-1);
            interesesAsequiblesAleatorios.add(new Interes(categoriasAsequibles.get(indiceCategoriaAsequibleAleatoria),i));
            categoriasAsequibles.remove(indiceCategoriaAsequibleAleatoria);            
        }
        return interesesAsequiblesAleatorios;
    }

    public Producto getProducto(String nombreProducto) {
        if(productos!=null){
            for(Producto producto:productos){
                if(producto.getNombre().equalsIgnoreCase(nombreProducto)){
                    return producto;
                }
            }
        }
        if(subCategorias!=null){
            for(Categoria subCategoria:subCategorias){
                Producto productoSubCategoria=subCategoria.getProducto(nombreProducto);
                if(productoSubCategoria!=null){
                    return productoSubCategoria;
                }
            }
        }
        return null;
    }

    public Categoria getCategoria(String nombreCategoria) {
        if(this.nombre.equalsIgnoreCase(nombreCategoria)){
            return this;
        }
        if(subCategorias!=null){
            for(Categoria subCategoria:subCategorias){
                Categoria subCategoriaEncontrada=subCategoria.getCategoria(nombreCategoria);
                if(subCategoriaEncontrada!=null){
                    return subCategoriaEncontrada;
                }
            }
        }
        return null;
    }

    public ArrayList<Producto> getProductosAutogenerados(double porcentajeDiversidad) {
        ArrayList<Producto> productosAutogenerados=new ArrayList();
        if(this.productos!=null){
            if(this.productos.size()>0){
                int cantidadProductos=(int)Math.round(porcentajeDiversidad*this.productos.size());
                int[] indices= new int[cantidadProductos];
                for(int i=0;i<indices.length;i++){
                    indices[i]=-1;
                }
                for(int i=0;i<cantidadProductos;i++){
                    int indice=Aleatorio.getInstance().siguienteEntero(0, cantidadProductos-1);
                    boolean repetido=false;
                    for(int j=0;j<indices.length;j++){
                        if(indices[j]==indice){
                            repetido=true;
                            break;
                        }
                    }
                    if(repetido){
                        i--;
                    }
                    else{
                        productosAutogenerados.add(productos.get(indice));
                    }
                }
            }
            if(this.subCategorias!=null){
                for(Categoria subCategoria:subCategorias){
                    ArrayList<Producto> productosSubCategoria=subCategoria.getProductosAutogenerados(porcentajeDiversidad);
                    if(productosSubCategoria!=null){
                        for(Producto productoAutogeneradoSubCategoria:productosSubCategoria){
                            if(!productosAutogenerados.contains(productoAutogeneradoSubCategoria)){
                                productosAutogenerados.add(productoAutogeneradoSubCategoria);
                            }   
                        }
                    }
                }
            }
        }
        return productosAutogenerados;
    }
    
    
    
}
