/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.agente.config;

import SIMALL.modelo.cc.agente.ApetitoEndeudamiento;
import SIMALL.modelo.cc.agente.Demografia;
import SIMALL.modelo.cc.agente.NivelEconomico;
import SIMALL.modelo.cc.agente.PresupuestoGasto;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.DatosDemograficos;
import SIMALL.modelo.util.Punto;
import java.io.File;
import java.util.HashMap;
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
public class LectorDemografia {
    public LectorDemografia(){
    
    }
    
    public static Demografia leerDemografia() {
        Demografia demografia=new Demografia();
        try{
            File archivoXML = new File("simall.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();
            Node nodoDemografia=doc.getElementsByTagName("Demografia").item(0);
            if(nodoDemografia!=null){
                if(nodoDemografia.getAttributes()!=null){
                    NodeList nodeListMercadoObjetivo=((Element)nodoDemografia).getElementsByTagName("MercadoObjetivo");
                    NodeList nodeListNivelEconomico=((Element)nodoDemografia).getElementsByTagName("NivelEconomico");
                    HashMap<Integer,Punto> nivelEconomico=new HashMap();
                    HashMap<Integer,Punto> apetitoEndeudamiento=new HashMap();
                    HashMap<Integer,Punto> presupuestoGasto=new HashMap();
                    if(nodeListNivelEconomico!=null){
                        if(nodeListNivelEconomico.item(0)!=null){
                            NodeList rangos=nodeListNivelEconomico.item(0).getChildNodes();
                            for(int i=0;i<rangos.getLength();i++){
                                if(rangos.item(i).getAttributes()!=null){
                                    if(rangos.item(i).getNodeName().equals("Rango")){
                                        Element nodoRago=(Element)rangos.item(i);
                                        String nombreNivelSocioeconomico=nodoRago.getAttribute("nivelSocioeconomico");
                                        int nivelSocioeconomico=-1;
                                        double minimoDinero=Double.parseDouble(nodoRago.getAttribute("minimoDinero"));
                                        double maximoDinero=Double.parseDouble(nodoRago.getAttribute("maximoDinero"));
                                        double minimoEndeudamiento=Double.parseDouble(nodoRago.getAttribute("minimoEndeudamiento"));
                                        double maximoEndeudamiento=Double.parseDouble(nodoRago.getAttribute("maximoEndeudamiento"));
                                        double minimoPresupuestoGasto=Double.parseDouble(nodoRago.getAttribute("minimoPresupuestoGasto"));
                                        double maximoPresupuestoGasto=Double.parseDouble(nodoRago.getAttribute("maximoPresupuestoGasto"));
                                        if(nombreNivelSocioeconomico.equalsIgnoreCase("muy_bajo")){
                                            nivelSocioeconomico=DatosDemograficos.NIVEL_SOCIOECONOMICO_MUY_BAJO;
                                        }
                                        if(nombreNivelSocioeconomico.equalsIgnoreCase("bajo")){
                                            nivelSocioeconomico=DatosDemograficos.NIVEL_SOCIOECONOMICO_BAJO;
                                        }
                                        if(nombreNivelSocioeconomico.equalsIgnoreCase("bajo_medio")){
                                            nivelSocioeconomico=DatosDemograficos.NIVEL_SOCIOECONOMICO_BAJO_MEDIO;
                                        }
                                        if(nombreNivelSocioeconomico.equalsIgnoreCase("medio")){
                                            nivelSocioeconomico=DatosDemograficos.NIVEL_SOCIOECONOMICO_MEDIO;
                                        }
                                        if(nombreNivelSocioeconomico.equalsIgnoreCase("medio_alto")){
                                            nivelSocioeconomico=DatosDemograficos.NIVEL_SOCIOECONOMICO_MEDIO_ALTO;
                                        }
                                        if(nombreNivelSocioeconomico.equalsIgnoreCase("alto")){
                                            nivelSocioeconomico=DatosDemograficos.NIVEL_SOCIOECONOMICO_ALTO;
                                        }
                                        if(nombreNivelSocioeconomico.equalsIgnoreCase("muy_alto")){
                                            nivelSocioeconomico=DatosDemograficos.NIVEL_SOCIOECONOMICO_MUY_ALTO;
                                        }
                                        nivelEconomico.put(nivelSocioeconomico, new Punto(minimoDinero,maximoDinero));
                                        apetitoEndeudamiento.put(nivelSocioeconomico, new Punto(minimoEndeudamiento,maximoEndeudamiento));
                                        presupuestoGasto.put(nivelSocioeconomico, new Punto(minimoPresupuestoGasto,maximoPresupuestoGasto));
                                    }    
                                }
                            }
                        }
                    }
                    return new Demografia(LectorMercadoObjetivo.leerMercadoObjetivo(nodeListMercadoObjetivo), new NivelEconomico(nivelEconomico), new ApetitoEndeudamiento(apetitoEndeudamiento), new PresupuestoGasto(presupuestoGasto)); 
                }
            }
        }
        catch(Exception e){
            System.out.println("Se presentó un error en la lectura de la demografia: "+e.getMessage());
            System.exit(0);
        }
        return demografia;
    
    }
}
