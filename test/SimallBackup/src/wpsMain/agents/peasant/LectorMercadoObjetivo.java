/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import wpsMain.agents.peasant.MercadoObjetivo;
import wpsMain.agents.peasant.RegistroAtributoMercado;
import wpsMain.util.Punto;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author dsvalencia
 */
public class LectorMercadoObjetivo {
    
    public static MercadoObjetivo leerMercadoObjetivo(NodeList nodeListMercadoObjetivo){
        MercadoObjetivo mercadoObjetivo=new MercadoObjetivo();
        if(nodeListMercadoObjetivo!=null){
            if(nodeListMercadoObjetivo.item(0)!=null){
                NodeList registrosAtributosMercado=nodeListMercadoObjetivo.item(0).getChildNodes();
                if(registrosAtributosMercado!=null){
                    for(int j=0;j<registrosAtributosMercado.getLength();j++){
                        if(registrosAtributosMercado.item(j).getAttributes()!=null){
                            if(registrosAtributosMercado.item(j).getNodeName().equals("RangoEdad")){
                                Element nodoRagoEdad=(Element)registrosAtributosMercado.item(j);
                                double porcentajeMercado=Double.parseDouble(nodoRagoEdad.getAttribute("porcentajeMercado"));
                                int desde=Integer.parseInt(nodoRagoEdad.getAttribute("desde"));
                                int hasta=Integer.parseInt(nodoRagoEdad.getAttribute("hasta"));
                                if(desde<hasta){
                                    if(!mercadoObjetivo.agregarRegistroAtributoMercado(new RegistroAtributoMercado(porcentajeMercado,RegistroAtributoMercado.TIPO_EDAD,new Punto(desde,hasta)))){
                                        System.err.println("Se presenta un error al agregar registro atributo mecrado Edad desde="+desde+" - hasta "+hasta);
                                        System.exit(0);
                                    }
                                }
                                else{
                                    if(!mercadoObjetivo.agregarRegistroAtributoMercado(new RegistroAtributoMercado(porcentajeMercado,RegistroAtributoMercado.TIPO_EDAD,new Punto(hasta,desde)))){
                                        System.err.println("Se presenta un error al agregar registro atributo mecrado Edad desde="+desde+" - hasta "+hasta);
                                        System.exit(0);
                                    }
                                }
                            }
                            else{
                                String nombreAtributo=registrosAtributosMercado.item(j).getNodeName();
                                Element nodoDiferenteAEdad=(Element)registrosAtributosMercado.item(j);
                                double porcentajeMercado=Double.parseDouble(nodoDiferenteAEdad.getAttribute("porcentajeMercado"));
                                String valor=nodoDiferenteAEdad.getAttribute("valor");
                                int tipoAtributo=-1;
                                if(nombreAtributo.equals("Genero")){tipoAtributo=RegistroAtributoMercado.TIPO_GENERO;}
                                if(nombreAtributo.equals("NivelSocioEconomico")){tipoAtributo=RegistroAtributoMercado.TIPO_NIVEL_SOCIOECONOMICO;}
                                if(nombreAtributo.equals("Ocupacion")){tipoAtributo=RegistroAtributoMercado.TIPO_OCUPACION;}
                                if(nombreAtributo.equals("Escolaridad")){tipoAtributo=RegistroAtributoMercado.TIPO_ESCOLARIDAD;}
                                if(!mercadoObjetivo.agregarRegistroAtributoMercado(new RegistroAtributoMercado(porcentajeMercado,tipoAtributo,valor))){
                                    System.err.println("Se presenta un error al agregar registro atributo mecrado tipo: "+nombreAtributo+" valor "+valor);
                                    System.exit(0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return mercadoObjetivo;
    }
    
}
