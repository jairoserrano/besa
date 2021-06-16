/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.agente.config;

import SIMALL.modelo.ia.fuzzy.pares.EstructuraFuzzy;
import SIMALL.modelo.ia.fuzzy.pares.NodoFuzzy;
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
public class LectorNodosFuzzy {
    
    public LectorNodosFuzzy(){}
    
    public static NodoFuzzy leerNodosFuzzy() {
        NodoFuzzy nodoFuzzy=new NodoFuzzy();
        try{
            File archivoXML = new File("simall.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();
            nodoFuzzy=crearNodosFuzzyRecursivamente(doc.getElementsByTagName("NodoFuzzy").item(0));
        }
        catch(Exception e){
            System.out.println("Se presentó un error en la lectura de los nodos fuzzy: "+e.getMessage());
            System.exit(0);
        }
        return nodoFuzzy;
    
    }

    private static NodoFuzzy crearNodosFuzzyRecursivamente(Node nodoFuzzy) {
        ArrayList<NodoFuzzy> subNodosFuzzy=new ArrayList();
        if(nodoFuzzy!=null&&nodoFuzzy.getFirstChild()!=null){
            Node nodoSubNodoFuzzy=nodoFuzzy.getFirstChild().getNextSibling();
            while(nodoSubNodoFuzzy!=null&&nodoSubNodoFuzzy.getAttributes()==null){
                nodoSubNodoFuzzy=nodoSubNodoFuzzy.getNextSibling();
            }
            int i=0;
            while(nodoSubNodoFuzzy!=null){
                subNodosFuzzy.add(crearNodosFuzzyRecursivamente(nodoSubNodoFuzzy));
                nodoSubNodoFuzzy=nodoSubNodoFuzzy.getNextSibling();
                while(nodoSubNodoFuzzy!=null&&nodoSubNodoFuzzy.getAttributes()==null){
                    nodoSubNodoFuzzy=nodoSubNodoFuzzy.getNextSibling();
                }
            }
        }
        
        String stringTipoProporcion=((Element)nodoFuzzy).getAttribute("tipoProporcion");
        int tipoProporcion=-1;
        if(stringTipoProporcion.equals("+")){
            tipoProporcion=EstructuraFuzzy.PROPORCION_POSITIVA;
        }
        if(stringTipoProporcion.equals("-")){
            tipoProporcion=EstructuraFuzzy.PROPORCION_NEGATIVA;
        }
        
        
        if(subNodosFuzzy.isEmpty()){
            return new NodoFuzzy(tipoProporcion,((Element)nodoFuzzy).getAttribute("nombre"),((Element)nodoFuzzy).getAttribute("objeto"),((Element)nodoFuzzy).getAttribute("metodo"));
        }
        return new NodoFuzzy(tipoProporcion,((Element)nodoFuzzy).getAttribute("nombre"),subNodosFuzzy);
    }
    
}
