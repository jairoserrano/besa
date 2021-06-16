/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.ia.fuzzy.pares;

import SIMALL.modelo.ia.fuzzy.LogicaDifusa;
import SIMALL.modelo.ia.fuzzy.TerminoLinguistico;
import SIMALL.modelo.ia.fuzzy.VariableDeSalida;
import SIMALL.modelo.ia.fuzzy.VariableDeEntrada;
import SIMALL.modelo.ia.fuzzy.Condicion;
import SIMALL.modelo.util.Punto;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author dsvalencia
 */

public class EstructuraFuzzy {
    
    public static final int PROPORCION_POSITIVA=0;
    public static final int PROPORCION_NEGATIVA=1;
    
    private LogicaDifusa logicaDifusaPositivoPositivo;
    private LogicaDifusa logicaDifusaPositivoNegativo;
    private LogicaDifusa logicaDifusaNegativoPositivo;
    private LogicaDifusa logicaDifusaNegativoNegativo;
    
    ArrayList<Condicion> condicionesPositivoPositivo=new ArrayList();
    ArrayList<Condicion> condicionesPositivoNegativo=new ArrayList();
    ArrayList<Condicion> condicionesNegativoPositivo=new ArrayList();
    ArrayList<Condicion> condicionesNegativoNegativo=new ArrayList();
    
    
    public EstructuraFuzzy(){
        ArrayList<VariableDeEntrada> variablesDeEntrada=new ArrayList();
        ArrayList<TerminoLinguistico> tlV1=new ArrayList();
        TerminoLinguistico bajoV1=new TerminoLinguistico("bajo",new ArrayList(Arrays.asList(new Punto(0,1),new Punto(0.2,1),new Punto(0.4,0))));
        TerminoLinguistico medioV1=new TerminoLinguistico("medio",new ArrayList(Arrays.asList(new Punto(0.2,0),new Punto(0.4,1),new Punto(0.6,1),new Punto(0.8,0))));
        TerminoLinguistico altoV1=new TerminoLinguistico("alto",new ArrayList(Arrays.asList(new Punto(0.6,0),new Punto(0.8,1),new Punto(1,1))));
        tlV1.add(bajoV1);
        tlV1.add(medioV1);
        tlV1.add(altoV1);
        VariableDeEntrada v1=new VariableDeEntrada("v1",tlV1);
        variablesDeEntrada.add(v1);

        ArrayList<TerminoLinguistico> tlV2=new ArrayList();
        TerminoLinguistico bajoV2=new TerminoLinguistico("bajo",new ArrayList(Arrays.asList(new Punto(0,1),new Punto(0.2,1),new Punto(0.4,0))));
        TerminoLinguistico medioV2=new TerminoLinguistico("medio",new ArrayList(Arrays.asList(new Punto(0.2,0),new Punto(0.4,1),new Punto(0.6,1),new Punto(0.8,0))));
        TerminoLinguistico altoV2=new TerminoLinguistico("alto",new ArrayList(Arrays.asList(new Punto(0.6,0),new Punto(0.8,1),new Punto(1,1))));
        tlV2.add(bajoV2);
        tlV2.add(medioV2);
        tlV2.add(altoV2);
        VariableDeEntrada v2=new VariableDeEntrada("v2",tlV2);
        variablesDeEntrada.add(v2);

        ArrayList<VariableDeSalida> variablesDeSalida=new ArrayList();
        ArrayList<TerminoLinguistico> tlV3=new ArrayList();
        TerminoLinguistico bajoV3=new TerminoLinguistico("bajo",new ArrayList(Arrays.asList(new Punto(0,1),new Punto(0.2,1),new Punto(0.4,0))));
        TerminoLinguistico medioV3=new TerminoLinguistico("medio",new ArrayList(Arrays.asList(new Punto(0.2,0),new Punto(0.4,1),new Punto(0.6,1),new Punto(0.8,0))));
        TerminoLinguistico altoV3=new TerminoLinguistico("alto",new ArrayList(Arrays.asList(new Punto(0.6,0),new Punto(0.8,1),new Punto(1,1))));
        tlV3.add(bajoV3);
        tlV3.add(medioV3);
        tlV3.add(altoV3);
        VariableDeSalida v3=new VariableDeSalida("v3",tlV3,VariableDeSalida.METODO_COG,0);
        variablesDeSalida.add(v3);

        ArrayList<Condicion> condiciones=new ArrayList();

        condicionesPositivoPositivo.add(new Condicion(v1,bajoV1,v2,bajoV2,v3,bajoV3));
        condicionesPositivoPositivo.add(new Condicion(v1,bajoV1,v2,medioV2,v3,bajoV3));
        condicionesPositivoPositivo.add(new Condicion(v1,bajoV1,v2,altoV2,v3,medioV3));
        condicionesPositivoPositivo.add(new Condicion(v1,medioV1,v2,bajoV2,v3,bajoV3));
        condicionesPositivoPositivo.add(new Condicion(v1,medioV1,v2,medioV2,v3,medioV3));
        condicionesPositivoPositivo.add(new Condicion(v1,medioV1,v2,altoV2,v3,altoV3));
        condicionesPositivoPositivo.add(new Condicion(v1,altoV1,v2,bajoV2,v3,medioV3));
        condicionesPositivoPositivo.add(new Condicion(v1,altoV1,v2,medioV2,v3,altoV3));
        condicionesPositivoPositivo.add(new Condicion(v1,altoV1,v2,altoV2,v3,altoV3));
        
        condicionesPositivoNegativo.add(new Condicion(v1,bajoV1,v2,bajoV2,v3,medioV3));
        condicionesPositivoNegativo.add(new Condicion(v1,bajoV1,v2,medioV2,v3,bajoV3));
        condicionesPositivoNegativo.add(new Condicion(v1,bajoV1,v2,altoV2,v3,bajoV3));
        condicionesPositivoNegativo.add(new Condicion(v1,medioV1,v2,bajoV2,v3,altoV3));
        condicionesPositivoNegativo.add(new Condicion(v1,medioV1,v2,medioV2,v3,medioV3));
        condicionesPositivoNegativo.add(new Condicion(v1,medioV1,v2,altoV2,v3,bajoV3));
        condicionesPositivoNegativo.add(new Condicion(v1,altoV1,v2,bajoV2,v3,altoV3));
        condicionesPositivoNegativo.add(new Condicion(v1,altoV1,v2,medioV2,v3,altoV3));
        condicionesPositivoNegativo.add(new Condicion(v1,altoV1,v2,altoV2,v3,medioV3));

        condicionesNegativoPositivo.add(new Condicion(v1,bajoV1,v2,bajoV2,v3,medioV3));
        condicionesNegativoPositivo.add(new Condicion(v1,bajoV1,v2,medioV2,v3,altoV3));
        condicionesNegativoPositivo.add(new Condicion(v1,bajoV1,v2,altoV2,v3,altoV3));
        condicionesNegativoPositivo.add(new Condicion(v1,medioV1,v2,bajoV2,v3,bajoV3));
        condicionesNegativoPositivo.add(new Condicion(v1,medioV1,v2,medioV2,v3,medioV3));
        condicionesNegativoPositivo.add(new Condicion(v1,medioV1,v2,altoV2,v3,altoV3));
        condicionesNegativoPositivo.add(new Condicion(v1,altoV1,v2,bajoV2,v3,bajoV3));
        condicionesNegativoPositivo.add(new Condicion(v1,altoV1,v2,medioV2,v3,bajoV3));
        condicionesNegativoPositivo.add(new Condicion(v1,altoV1,v2,altoV2,v3,medioV3));
        
        condicionesNegativoNegativo.add(new Condicion(v1,bajoV1,v2,bajoV2,v3,altoV3));
        condicionesNegativoNegativo.add(new Condicion(v1,bajoV1,v2,medioV2,v3,altoV3));
        condicionesNegativoNegativo.add(new Condicion(v1,bajoV1,v2,altoV2,v3,medioV3));
        condicionesNegativoNegativo.add(new Condicion(v1,medioV1,v2,bajoV2,v3,altoV3));
        condicionesNegativoNegativo.add(new Condicion(v1,medioV1,v2,medioV2,v3,medioV3));
        condicionesNegativoNegativo.add(new Condicion(v1,medioV1,v2,altoV2,v3,bajoV3));
        condicionesNegativoNegativo.add(new Condicion(v1,altoV1,v2,bajoV2,v3,medioV3));
        condicionesNegativoNegativo.add(new Condicion(v1,altoV1,v2,medioV2,v3,bajoV3));
        condicionesNegativoNegativo.add(new Condicion(v1,altoV1,v2,altoV2,v3,bajoV3));

        BloqueDeReglas matrizDeReglasPositivoPositivo=new BloqueDeReglas(BloqueDeReglas.MIN,BloqueDeReglas.MIN,BloqueDeReglas.MAX,condicionesPositivoPositivo);
        BloqueDeReglas matrizDeReglasPositivoNegativo=new BloqueDeReglas(BloqueDeReglas.MIN,BloqueDeReglas.MIN,BloqueDeReglas.MAX,condicionesPositivoNegativo);
        BloqueDeReglas matrizDeReglasNegativoPositivo=new BloqueDeReglas(BloqueDeReglas.MIN,BloqueDeReglas.MIN,BloqueDeReglas.MAX,condicionesNegativoPositivo);
        BloqueDeReglas matrizDeReglasNegativoNegativo=new BloqueDeReglas(BloqueDeReglas.MIN,BloqueDeReglas.MIN,BloqueDeReglas.MAX,condicionesNegativoNegativo);
        
        logicaDifusaPositivoPositivo=new LogicaDifusa("PositivoPositivo",variablesDeEntrada,variablesDeSalida,matrizDeReglasPositivoPositivo);
        logicaDifusaPositivoNegativo=new LogicaDifusa("PositivoNegativo",variablesDeEntrada,variablesDeSalida,matrizDeReglasPositivoNegativo);
        logicaDifusaNegativoPositivo=new LogicaDifusa("NegativoPositivo",variablesDeEntrada,variablesDeSalida,matrizDeReglasNegativoPositivo);
        logicaDifusaNegativoNegativo=new LogicaDifusa("NegativoNegativo",variablesDeEntrada,variablesDeSalida,matrizDeReglasNegativoNegativo);
    }
    
    private double evaluarPareja(double variable1, int proporcionalidad1, double variable2, double proporcionalidad2){
        if(variable1>=0&&variable1<=1&&variable2>=0&&variable2<=1){
            if(proporcionalidad1==PROPORCION_POSITIVA&&proporcionalidad2==PROPORCION_POSITIVA){
                logicaDifusaPositivoPositivo.setVariableDeEntrada("v1", variable1);
                logicaDifusaPositivoPositivo.setVariableDeEntrada("v2", variable2);
                logicaDifusaPositivoPositivo.evaluar();
                return logicaDifusaPositivoPositivo.getVariableDeSalida("v3");
            }
            if(proporcionalidad1==PROPORCION_POSITIVA&&proporcionalidad2==PROPORCION_NEGATIVA){
                logicaDifusaPositivoNegativo.setVariableDeEntrada("v1", variable1);
                logicaDifusaPositivoNegativo.setVariableDeEntrada("v2", variable2);
                logicaDifusaPositivoNegativo.evaluar();
                return logicaDifusaPositivoNegativo.getVariableDeSalida("v3");
            }
            if(proporcionalidad1==PROPORCION_NEGATIVA&&proporcionalidad2==PROPORCION_POSITIVA){
                logicaDifusaNegativoPositivo.setVariableDeEntrada("v1", variable1);
                logicaDifusaNegativoPositivo.setVariableDeEntrada("v2", variable2);
                logicaDifusaNegativoPositivo.evaluar();
                return logicaDifusaNegativoPositivo.getVariableDeSalida("v3");
            }
            if(proporcionalidad1==PROPORCION_NEGATIVA&&proporcionalidad2==PROPORCION_NEGATIVA){
                logicaDifusaNegativoNegativo.setVariableDeEntrada("v1", variable1);
                logicaDifusaNegativoNegativo.setVariableDeEntrada("v2", variable2);
                logicaDifusaNegativoNegativo.evaluar();
                return logicaDifusaNegativoNegativo.getVariableDeSalida("v3");
            }
        }
        return -1;
    }

    public double evaluar(NodoFuzzy nodoFuzzy) {
        if(nodoFuzzy.getNodosFuzzyHijos().isEmpty()){
            if(nodoFuzzy.getValor()!=-1){
                return nodoFuzzy.getValor();
            }
            else{
                System.err.println("El nodoFuzzy "+nodoFuzzy.getNombre()+" no tiene asignado correctamente los valores.");
                System.exit(0);
            }
            return nodoFuzzy.getValor();
        }else{
            if(nodoFuzzy.getNodosFuzzyHijos().size()==2){
                NodoFuzzy nodo1=nodoFuzzy.getNodosFuzzyHijos().get(0);
                NodoFuzzy nodo2=nodoFuzzy.getNodosFuzzyHijos().get(1);
                if(!nodo1.getNodosFuzzyHijos().isEmpty()){
                    for(NodoFuzzy nodosFuzzyHijo1:nodo1.getNodosFuzzyHijos()){
                        nodo1.setValor(evaluar(nodosFuzzyHijo1));
                    }
                }
                if(!nodo2.getNodosFuzzyHijos().isEmpty()){
                    for(NodoFuzzy nodosFuzzyHijo2:nodo2.getNodosFuzzyHijos()){
                        nodo2.setValor(evaluar(nodosFuzzyHijo2));
                    }
                }
                if(nodo1.getValor()!=-1&&nodo2.getValor()!=-1){
                    if(nodo1.getTipoProporcion()!=-1&&nodo1.getTipoProporcion()!=-1){
                        nodoFuzzy.setValor(evaluarPareja(nodo1.getValor(), nodo1.getTipoProporcion(), nodo2.getValor(), nodo2.getTipoProporcion()));
                    }
                    else{
                        System.err.println("El nodoFuzzy "+nodoFuzzy.getNombre()+" no tiene asignado correctamente los tipos de proporción.");
                        System.exit(0);
                    }
                }else{
                    System.err.println("El nodoFuzzy "+nodoFuzzy.getNombre()+" no tiene asignado correctamente los valores.");
                    System.exit(0);
                }
            }else{
                System.err.println("El nodoFuzzy "+nodoFuzzy.getNombre()+" no tiene 2 hijos. En esta versión, es requerido que cada NodoFuzzy tenga 2 hijos.");
                System.exit(0);
            }
        }
        return nodoFuzzy.getValor();
    }
}
