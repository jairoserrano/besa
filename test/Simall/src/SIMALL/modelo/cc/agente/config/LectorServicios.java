/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.agente.config;

import SIMALL.modelo.cc.infraestructura.ConfEntrada;
import SIMALL.modelo.cc.infraestructura.ConfLugar;
import SIMALL.modelo.cc.infraestructura.ConfPuntoInformacion;
import SIMALL.modelo.cc.infraestructura.ConfSanitario;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author dsvalencia
 */
public class LectorServicios {
    
    public static ArrayList<ConfSanitario> leerSanitarios(){
        ArrayList<ConfSanitario> confSanitarios=new ArrayList();
        try{
            File archivoXML = new File("simall.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();

            NodeList listaSanitarios = ((Element)doc.getElementsByTagName("Servicios").item(0)).getElementsByTagName("Sanitario");
            for(int i=0;i<listaSanitarios.getLength();i++){
                Element nodoSanitario = (Element)listaSanitarios.item(i);
                String nombreSanitario=nodoSanitario.getAttribute("nombre");
                int unidades=Integer.parseInt(nodoSanitario.getAttribute("unidades"));
                int locales=Integer.parseInt(nodoSanitario.getAttribute("locales"));
                int minTiempo=Integer.parseInt(nodoSanitario.getAttribute("minTiempo"));
                int avgTiempo=Integer.parseInt(nodoSanitario.getAttribute("avgTiempo"));
                int maxTiempo=Integer.parseInt(nodoSanitario.getAttribute("maxTiempo"));
                confSanitarios.add(new ConfSanitario(nombreSanitario,"Sanitario",unidades,locales,minTiempo,avgTiempo,maxTiempo, ConfLugar.X_DEFECTO, ConfLugar.Y_DEFECTO));
            }
        }
        catch(Exception e){
            System.out.println("Se presentó un error en la lectura de los servicios sanitarios: "+e.getMessage());
            System.exit(0);
        }
        return confSanitarios;
    }
    
    public static ArrayList<ConfPuntoInformacion> leerPuntosDeInformacion(){
        ArrayList<ConfPuntoInformacion> confPuntosInformacion=new ArrayList();
        try{
            File archivoXML = new File("simall.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();

            NodeList listaPuntosInformacion = ((Element)doc.getElementsByTagName("Servicios").item(0)).getElementsByTagName("PuntoInformacion");
            for(int i=0;i<listaPuntosInformacion.getLength();i++){
                Element nodoPuntoInformacion = (Element)listaPuntosInformacion.item(i);
                String nombrePuntoInformacion=nodoPuntoInformacion.getAttribute("nombre");
                int unidades=Integer.parseInt(nodoPuntoInformacion.getAttribute("unidades"));
                int locales=Integer.parseInt(nodoPuntoInformacion.getAttribute("locales"));
                int minTiempo=Integer.parseInt(nodoPuntoInformacion.getAttribute("minTiempo"));
                int avgTiempo=Integer.parseInt(nodoPuntoInformacion.getAttribute("avgTiempo"));
                int maxTiempo=Integer.parseInt(nodoPuntoInformacion.getAttribute("maxTiempo"));
                confPuntosInformacion.add(new ConfPuntoInformacion(nombrePuntoInformacion,"PuntoInformacion",unidades,locales,minTiempo,avgTiempo,maxTiempo, ConfLugar.X_DEFECTO, ConfLugar.Y_DEFECTO));
            }
        }
        catch(Exception e){
            System.out.println("Se presentó un error en la lectura de los servicios puntos de información: "+e.getMessage());
            System.exit(0);
        }
        return confPuntosInformacion;
    }
    
    public static ArrayList<ConfEntrada> leerEntradas(){
        ArrayList<ConfEntrada> confEntradas=new ArrayList();
        try{
            File archivoXML = new File("simall.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();

            NodeList listaEntradas = ((Element)doc.getElementsByTagName("Servicios").item(0)).getElementsByTagName("Entrada");
            for(int i=0;i<listaEntradas.getLength();i++){
                Element nodoEntrada = (Element)listaEntradas.item(i);
                String nombreEntrada=nodoEntrada.getAttribute("nombre");
                int unidades=Integer.parseInt(nodoEntrada.getAttribute("unidades"));
                int locales=Integer.parseInt(nodoEntrada.getAttribute("locales"));
                confEntradas.add(new ConfEntrada(nombreEntrada,"Entrada",unidades,locales, ConfLugar.X_DEFECTO, ConfLugar.Y_DEFECTO));
            }
        }
        catch(Exception e){
            System.out.println("Se presentó un error en la lectura de los servicios puntos de información: "+e.getMessage());
            System.exit(0);
        }
        return confEntradas;
    }
    
}
