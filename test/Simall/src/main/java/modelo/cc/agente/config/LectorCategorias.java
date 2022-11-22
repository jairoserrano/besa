/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.agente.config;

import SIMALL.modelo.cc.agente.Categoria;
import SIMALL.modelo.cc.agente.MercadoObjetivo;
import SIMALL.modelo.cc.agente.Producto;
import SIMALL.modelo.cc.agente.RegistroInventario;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author dsvalencia
 */
public class LectorCategorias {
    
    public LectorCategorias(){}
    
    public static Categoria leerCategorias() {
        Categoria categoria=new Categoria("0","INICIAL",new ArrayList(),new ArrayList(),new MercadoObjetivo());
        try{
            File archivoXML = new File("simall.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();
            categoria=crearCategoriaRecursivamente(doc.getElementsByTagName("Categoria").item(0),new MercadoObjetivo());            
        }
        catch(Exception e){
            System.out.println("Se presentó un error en la lectura de las categorías: "+e.getMessage());
            System.exit(0);
        }
        return categoria;
    
    }

    private static Categoria crearCategoriaRecursivamente(Node nodoCategoria, MercadoObjetivo mercadoObjetivoCategoriaPadre) {
        ArrayList<Categoria> subCategorias=new ArrayList();
        ArrayList<Producto> productos=new ArrayList();
        MercadoObjetivo mercadoObjetivo=new MercadoObjetivo();
        if(nodoCategoria!=null){
            mercadoObjetivo=LectorMercadoObjetivo.leerMercadoObjetivo(((Element)nodoCategoria).getElementsByTagName("MercadoObjetivo"));
            if(mercadoObjetivo==null&&mercadoObjetivoCategoriaPadre!=null){
                mercadoObjetivo=mercadoObjetivoCategoriaPadre;
            }
            if(nodoCategoria.getFirstChild()!=null){
                Node subNodoCategoria=nodoCategoria.getFirstChild().getNextSibling();
                while(subNodoCategoria!=null&&subNodoCategoria.getAttributes()==null){
                    subNodoCategoria=subNodoCategoria.getNextSibling();
                }
                int i=0;
                while(subNodoCategoria!=null){
                    if(subNodoCategoria.getNodeName().equals("Categoria")){
                        subCategorias.add(crearCategoriaRecursivamente(subNodoCategoria,mercadoObjetivo));
                    }
                    if(subNodoCategoria.getNodeName().equals("Producto")){
                        String nombreProducto=((Element)subNodoCategoria).getAttribute("nombre");
                        String marca=((Element)subNodoCategoria).getAttribute("marca");
                        double minPrecioMercado=Double.parseDouble(((Element)subNodoCategoria).getAttribute("minPrecioMercado"));
                        double avgPrecioMercadoMercado=Double.parseDouble(((Element)subNodoCategoria).getAttribute("avgPrecioMercado"));
                        double maxPrecioMercado=Double.parseDouble(((Element)subNodoCategoria).getAttribute("maxPrecioMercado"));
                        double presentacion=Double.parseDouble(((Element)subNodoCategoria).getAttribute("presentacion"));
                        String stringUnidad=((Element)subNodoCategoria).getAttribute("unidad");
                        int unidad=-1;
                        if(stringUnidad.equalsIgnoreCase("UND")){unidad=RegistroInventario.TIPO_UND;}
                        if(stringUnidad.equalsIgnoreCase("G")){unidad=RegistroInventario.TIPO_G;}
                        if(stringUnidad.equalsIgnoreCase("ML")){unidad=RegistroInventario.TIPO_ML;}
                        if(stringUnidad.equalsIgnoreCase("MG")){unidad=RegistroInventario.TIPO_MG;}
                        if(stringUnidad.equalsIgnoreCase("LB")){unidad=RegistroInventario.TIPO_LB;}
                        if(stringUnidad.equalsIgnoreCase("LT")){unidad=RegistroInventario.TIPO_LT;}
                        productos.add(new Producto(nombreProducto, marca, minPrecioMercado, avgPrecioMercadoMercado, maxPrecioMercado, presentacion, unidad, null, null));
                    }
                    subNodoCategoria=subNodoCategoria.getNextSibling();
                    while(subNodoCategoria!=null&&subNodoCategoria.getAttributes()==null){
                        subNodoCategoria=subNodoCategoria.getNextSibling();
                    }
                }
            }
            for(Producto producto:productos){
                producto.setCategoria(((Element)nodoCategoria).getAttribute("nombre"));
            }
            return new Categoria(((Element)nodoCategoria).getAttribute("id"),((Element)nodoCategoria).getAttribute("nombre"),subCategorias,productos,mercadoObjetivo);
         }
        return null;
    }
}
