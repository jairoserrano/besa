/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.creencias;

import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.EmotionAxis;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.EmotionalActor;


/**
 *
 * @author dsvalencia
 */
public class ComponenteEmocional extends EmotionalActor{

    ComponenteEmocional(ComponenteEmocional componenteEmocional) {
        
    }

    public ComponenteEmocional() {
    
    }
    
    public void agregarEjeEmocional(String poloPositivo, String poloNegativo, float valorActual, float lineaBase, float factorAtenuacion){
        EmotionAxis ejeEmocional=new EmotionAxis(poloPositivo, poloNegativo, valorActual, lineaBase, factorAtenuacion);
        this.addEmotionAxis(ejeEmocional);
    }
    
    public void configurarInfluenciaDeEvento(String poloPositivo, String poloNegativo, String evento, float factorInfluencia){
        EmotionAxis ejeEmocional=this.getEmotionAxis(poloPositivo, poloNegativo);
        if(ejeEmocional!=null){
            ejeEmocional.setEventInfluence(evento, factorInfluencia);
        }
    }
    
    public void configurarDeseoDeEvento(String evento, String valoracion){
        this.setEventDesirability(evento, valoracion);
    }
    
    public void configurarRelacionConObjeto(String objeto, String valoracion){
        this.setObjectRelationship(objeto, valoracion);
    }
    
    public void configurarRelacionConPersona(String persona, String valoracion){
        this.setPersonRelationship(persona, valoracion);
    }
    
    

    private void configurarDiccionario() {
        
    }

    
    
    

    
}
