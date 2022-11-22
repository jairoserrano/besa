/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import wpsMain.agents.peasant.Categoria;
import wpsMain.agents.peasant.ConfigComercio;
import wpsMain.agents.peasant.Inventario;
import wpsMain.agents.peasant.MercadoObjetivo;
import wpsMain.agents.peasant.Producto;
import wpsMain.agents.peasant.RegistroInventario;
import SIMALL.modelo.cc.infraestructura.ConfLugar;
import wpsMain.util.Aleatorio;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author dsvalencia
 */
public class LectorComercios {

    private static ArrayList<RegistroInventario> autogenerarInventario(Categoria arbolCategorias, ArrayList<String> categoriasRegistradas, double porcentajeAbastecimiento, double porcentajeDiversidad, int nivelCosto, int minItems, int avgItems, int maxItems) {
        ArrayList<RegistroInventario> registrosInventarioAutogenerado=new ArrayList();
        for(String categoriaRegistrada:categoriasRegistradas){
            Categoria categoriaRegistradaEncontrada=arbolCategorias.getCategoria(categoriaRegistrada);
            if(categoriaRegistradaEncontrada!=null){
                ArrayList<Producto> productosAutogenerados=categoriaRegistradaEncontrada.getProductosAutogenerados(porcentajeDiversidad);
                if(productosAutogenerados!=null){
                    for(Producto productoAutogenerado:productosAutogenerados){
                        double precioUnitario=-1;
                        double min=productoAutogenerado.getMinPrecioMercado();
                        double prom=productoAutogenerado.getAvgPrecioMercado();
                        double max=productoAutogenerado.getMaxPrecioMercado();
                        switch(nivelCosto){
                            case ConfigComercio.NIVEL_COSTO_MUY_BAJO:   precioUnitario=Aleatorio.getInstance().siguienteDobleGeneracionClientes(min, min+((prom-min)/4));
                                                                        break;
                            case ConfigComercio.NIVEL_COSTO_BAJO:       precioUnitario=Aleatorio.getInstance().siguienteDobleGeneracionClientes(min+((prom-min)/4), min+(prom-min)/2);
                                                                        break;
                            case ConfigComercio.NIVEL_COSTO_BAJO_MEDIO: precioUnitario=Aleatorio.getInstance().siguienteDobleGeneracionClientes(min+(prom-min)/2, min+((prom-min)/4)*3);
                                                                        break;
                            case ConfigComercio.NIVEL_COSTO_MEDIO:      precioUnitario=Aleatorio.getInstance().siguienteDobleGeneracionClientes(min+((prom-min)/4)*3, prom+((max-prom)/4));
                                                                        break;
                            case ConfigComercio.NIVEL_COSTO_MEDIO_ALTO: precioUnitario=Aleatorio.getInstance().siguienteDobleGeneracionClientes(prom+((max-prom)/4),prom+((max-prom)/2));
                                                                        break;
                            case ConfigComercio.NIVEL_COSTO_ALTO:       precioUnitario=Aleatorio.getInstance().siguienteDobleGeneracionClientes(prom+((max-prom)/2),prom+(((max-prom)/4)*3));
                                                                        break;
                            case ConfigComercio.NIVEL_COSTO_MUY_ALTO:   precioUnitario=Aleatorio.getInstance().siguienteDobleGeneracionClientes(prom+(((max-prom)/4)*3),max);
                                                                        break;
                            default:    precioUnitario=Aleatorio.getInstance().siguienteDobleGeneracionClientes(min,max);
                                        break;
                        }
                        int cantidadDisponible=-1;
                        if(porcentajeAbastecimiento<0.25){
                            cantidadDisponible=Aleatorio.getInstance().siguienteEnteroGeneracionClientes(minItems,minItems+((avgItems-minItems)/2));
                        }
                        if(porcentajeAbastecimiento>=0.25&&porcentajeAbastecimiento<0.5){
                            cantidadDisponible=Aleatorio.getInstance().siguienteEnteroGeneracionClientes(minItems+((avgItems-minItems)/2),avgItems);
                        }
                        if(porcentajeAbastecimiento>=0.5&&porcentajeAbastecimiento<0.75){
                            cantidadDisponible=Aleatorio.getInstance().siguienteEnteroGeneracionClientes(avgItems,avgItems+((maxItems-avgItems)/2));
                        }
                        if(porcentajeAbastecimiento>=0.75&&porcentajeAbastecimiento<=1){
                            cantidadDisponible=Aleatorio.getInstance().siguienteEnteroGeneracionClientes(avgItems+((maxItems-avgItems)/2),maxItems);
                        }
                        registrosInventarioAutogenerado.add(new RegistroInventario(productoAutogenerado, productoAutogenerado.getUnidad(), productoAutogenerado.getPresentacion(), precioUnitario, cantidadDisponible));
                    }
                }
            }
        }
        return registrosInventarioAutogenerado;
    }
    
    public LectorComercios(){}
    
    public static ArrayList<ConfigComercio> leerComercios(int tipoComercio, Categoria arbolCategorias) {
        ArrayList<ConfigComercio> comercios=new ArrayList();
        try{
            File archivoXML = new File("simall.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();
            NodeList nodosConfigComercios=doc.getElementsByTagName("Comercios").item(0).getChildNodes();
            for(int i=0;i<nodosConfigComercios.getLength();i++){
                Node nodoConfigComercio=nodosConfigComercios.item(i);
                if(nodoConfigComercio.getAttributes()!=null){
                    String nombreConfigComercio=((Element)nodoConfigComercio).getAttribute("nombre");
                    String nichoConfigComercio=((Element)nodoConfigComercio).getAttribute("nicho");
                    int impulsadores=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("impulsadores"));
                    int tipoConfigComercio=-1;
                    String stringTipoConfigComercio=((Element)nodoConfigComercio).getAttribute("tipo");                    
                    if(stringTipoConfigComercio.equalsIgnoreCase("NORMAL")){tipoConfigComercio=ConfigComercio.TIPO_NORMAL;}
                    if(stringTipoConfigComercio.equalsIgnoreCase("ANCLA")){tipoConfigComercio=ConfigComercio.TIPO_ANCLA;}
                    if(tipoConfigComercio==tipoComercio){    
                        int unidades=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("unidades"));
                        int vitrinas=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("vitrinas"));
                        if(vitrinas>=unidades){
                            System.err.println("Error de configuración: "+nombreConfigComercio+" tiene igual o más vitrinas que unidades.");
                            System.exit(0);
                        }
                        int locales=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("locales"));
                        int minItems=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("minItems"));
                        int avgItems=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("avgItems"));
                        int maxItems=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("maxItems"));
                        int minValor=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("minValor"));
                        int avgValor=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("avgValor"));
                        int maxValor=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("maxValor"));
                        int minTiempo=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("minTiempo"));
                        int avgTiempo=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("avgTiempo"));
                        int maxTiempo=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("maxTiempo"));
                        int minVentas=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("minVentas"));
                        int avgVentas=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("avgVentas"));
                        int maxVentas=Integer.parseInt(((Element)nodoConfigComercio).getAttribute("maxVentas"));
                        boolean inventarioAutogenerado=Boolean.parseBoolean(((Element)nodoConfigComercio).getAttribute("inventarioAutogenerado"));
                        double porcentajeAbastecimiento=Double.parseDouble(((Element)nodoConfigComercio).getAttribute("porcentajeAbastecimiento"));
                        double porcentajeDiversidad=Double.parseDouble(((Element)nodoConfigComercio).getAttribute("porcentajeDiversidad"));
                        String sNivelCosto=((Element)nodoConfigComercio).getAttribute("nivelCosto");
                        int nivelCosto=-1;
                        if(sNivelCosto.equalsIgnoreCase("muy_bajo")){
                            nivelCosto=ConfigComercio.NIVEL_COSTO_MUY_BAJO;
                        }
                        if(sNivelCosto.equalsIgnoreCase("bajo")){
                            nivelCosto=ConfigComercio.NIVEL_COSTO_BAJO;
                        }
                        if(sNivelCosto.equalsIgnoreCase("bajo_medio")){
                            nivelCosto=ConfigComercio.NIVEL_COSTO_BAJO_MEDIO;
                        }
                        if(sNivelCosto.equalsIgnoreCase("medio")){
                            nivelCosto=ConfigComercio.NIVEL_COSTO_MEDIO;
                        }
                        if(sNivelCosto.equalsIgnoreCase("medio_alto")){
                            nivelCosto=ConfigComercio.NIVEL_COSTO_MEDIO_ALTO;
                        }
                        if(sNivelCosto.equalsIgnoreCase("alto")){
                            nivelCosto=ConfigComercio.NIVEL_COSTO_ALTO;
                        }
                        if(sNivelCosto.equalsIgnoreCase("muy_alto")){
                            nivelCosto=ConfigComercio.NIVEL_COSTO_MUY_ALTO;
                        }
                        NodeList nodeListMercadoObjetivoConfigComercio=((Element)nodoConfigComercio).getElementsByTagName("MercadoObjetivo");
                        MercadoObjetivo mercadoObjetivoConfigComercio=mercadoObjetivoConfigComercio=LectorMercadoObjetivo.leerMercadoObjetivo(nodeListMercadoObjetivoConfigComercio);
                        Inventario inventario=new Inventario();
                        NodeList nodeListInventario=((Element)nodoConfigComercio).getElementsByTagName("Inventario");
                        if(nodeListInventario!=null){
                            for(int j=0;j<nodeListInventario.getLength();j++){
                                if(nodeListInventario.item(j).getAttributes()!=null){
                                    NodeList nodeListRegistroInventario=nodeListInventario.item(j).getChildNodes();
                                    if(nodeListRegistroInventario!=null){
                                        for(int k=0;k<nodeListRegistroInventario.getLength();k++){
                                            Node nodoRegistroInventario=nodeListRegistroInventario.item(k);
                                            if(nodoRegistroInventario!=null){
                                                if(nodoRegistroInventario.getAttributes()!=null){
                                                    String nombreProducto=((Element)nodoRegistroInventario).getAttribute("producto");
                                                    double precioUnitario=Double.parseDouble(((Element)nodoRegistroInventario).getAttribute("precioUnitario"));
                                                    double presentacion=Double.parseDouble(((Element)nodoRegistroInventario).getAttribute("presentacion"));
                                                    int cantidadDisponible=Integer.parseInt(((Element)nodoRegistroInventario).getAttribute("cantidadDisponible"));
                                                    String stringUnidad=((Element)nodoRegistroInventario).getAttribute("unidad");
                                                    int unidad=-1;
                                                    if(stringUnidad.equalsIgnoreCase("UND")){unidad=RegistroInventario.TIPO_UND;}
                                                    if(stringUnidad.equalsIgnoreCase("G")){unidad=RegistroInventario.TIPO_G;}
                                                    if(stringUnidad.equalsIgnoreCase("ML")){unidad=RegistroInventario.TIPO_ML;}
                                                    if(stringUnidad.equalsIgnoreCase("MG")){unidad=RegistroInventario.TIPO_MG;}
                                                    if(stringUnidad.equalsIgnoreCase("LB")){unidad=RegistroInventario.TIPO_LB;}
                                                    if(stringUnidad.equalsIgnoreCase("LT")){unidad=RegistroInventario.TIPO_LT;}
                                                    Producto producto=arbolCategorias.getProducto(nombreProducto);
                                                    if(producto==null){
                                                        System.err.println("Error, no se encuentra el producto "+nombreProducto+" en el catálogo de categorías.");
                                                        System.exit(0);
                                                    }else{
                                                        producto.setNicho(nichoConfigComercio);
                                                    }
                                                    inventario.agregarRegistroInventario(producto, unidad, presentacion, precioUnitario, cantidadDisponible);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        ArrayList<String> categoriasRegistradas=new ArrayList();
                        NodeList nodeListCategoriasRegistradas=((Element)nodoConfigComercio).getElementsByTagName("CategoriasRegistradas");
                        if(nodeListCategoriasRegistradas!=null){
                            for(int j=0;j<nodeListCategoriasRegistradas.getLength();j++){
                                if(nodeListCategoriasRegistradas.item(j).getAttributes()!=null){
                                    NodeList nodeListRegistroCategoria=nodeListCategoriasRegistradas.item(j).getChildNodes();
                                    if(nodeListRegistroCategoria!=null){
                                        for(int k=0;k<nodeListRegistroCategoria.getLength();k++){
                                            Node nodoRegistroCategoria=nodeListRegistroCategoria.item(k);
                                            if(nodoRegistroCategoria!=null){
                                                if(nodoRegistroCategoria.getAttributes()!=null){
                                                    categoriasRegistradas.add(((Element)nodoRegistroCategoria).getAttribute("nombre"));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(inventarioAutogenerado){
                            inventario.agregarInventario(autogenerarInventario(arbolCategorias, categoriasRegistradas, porcentajeAbastecimiento, porcentajeDiversidad, nivelCosto, minItems, avgItems, maxItems));
                        }
                        comercios.add(new ConfigComercio(tipoConfigComercio,nombreConfigComercio,nichoConfigComercio,categoriasRegistradas,mercadoObjetivoConfigComercio,inventario,unidades,vitrinas,locales,minItems, avgItems, maxItems,minValor,avgValor,maxValor,minTiempo,avgTiempo,maxTiempo,minVentas,avgVentas,maxVentas,ConfLugar.X_DEFECTO,ConfLugar.Y_DEFECTO, inventarioAutogenerado, porcentajeAbastecimiento, porcentajeDiversidad, nivelCosto, impulsadores));
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Se presentó un error en la lectura de los comercios: "+e.getMessage());
            for(int i=0;i<e.getStackTrace().length;i++){
                System.err.println(e.getStackTrace()[i]);
            }
            System.exit(0);
        }
        return comercios;
    }
}
