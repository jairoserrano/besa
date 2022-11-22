/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.agente;

import SIMALL.modelo.cc.cliente.agenteBDI.creencias.DatosDemograficos;
import SIMALL.modelo.util.Punto;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dsvalencia
 */
public class NivelEconomico {
    private HashMap<Integer,Punto> nivelesEconomicos;

    public NivelEconomico(HashMap<Integer,Punto> nivelEconomico) {
        this.nivelesEconomicos = nivelEconomico;
    }
    
    public Punto obtenerRangoEconomico(int nivelEconomico){
        return (Punto)this.nivelesEconomicos.get(nivelEconomico);
    }
    
    @Override
    public String toString(){
        String cadena="Nivel Economico:\n\n";
        Set<Integer> niveles=nivelesEconomicos.keySet();
        Iterator i=niveles.iterator();
        while(i.hasNext()){
            int nivel=(Integer)i.next();
            cadena+="\t";
            switch(nivel){
                case DatosDemograficos.NIVEL_SOCIOECONOMICO_MUY_BAJO:   cadena+="MUY_BAJO: ";
                                                                        break;
                case DatosDemograficos.NIVEL_SOCIOECONOMICO_BAJO:       cadena+="BAJO: ";
                                                                        break;
                case DatosDemograficos.NIVEL_SOCIOECONOMICO_BAJO_MEDIO: cadena+="BAJO_MEDIO: ";
                                                                        break;
                case DatosDemograficos.NIVEL_SOCIOECONOMICO_MEDIO:      cadena+="MEDIO: ";
                                                                        break;
                case DatosDemograficos.NIVEL_SOCIOECONOMICO_MEDIO_ALTO: cadena+="MEDIO_ALTO: ";
                                                                        break;
                case DatosDemograficos.NIVEL_SOCIOECONOMICO_ALTO:       cadena+="ALTO: ";
                                                                        break;
                case DatosDemograficos.NIVEL_SOCIOECONOMICO_MUY_ALTO:   cadena+="MUY_ALTO: ";
                                                                        break;
                default:    System.err.println("Error: Se configuró un nivel económico desconocido: "+nivel);
                            System.exit(0);
            }
            cadena+="$"+nivelesEconomicos.get(nivel).getX()+", $"+nivelesEconomicos.get(nivel).getY()+"\n";
        }
        return cadena;
    }
}
