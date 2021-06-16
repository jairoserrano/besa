/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.lookandfeel;

import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;

/**
 *
 * @author dsvalencia
 */
public class Edificio extends Box{
    
    private LugarState lugar;
    
    public LugarState getLugar(){
        return lugar;
    }
    
    public Edificio(LugarState lugar){
        super(40,40,10);
        this.lugar=lugar;
        PhongMaterial material=new PhongMaterial();
        material.setSpecularColor(Color.BLUE);
        material.setDiffuseColor(Color.DARKBLUE);
        this.setMaterial(material);
    }
    
    public Edificio(LugarState lugar, int tipo, int ancho){
        super(ancho,ancho,10);
        this.lugar=lugar;
        PhongMaterial material=new PhongMaterial();
        
        switch(tipo){
            case LugarState.COMERCIO:material.setSpecularColor(MapaEstructural.getInstance().getColorComercioPorNichoClaro(lugar.getNicho()));
                                    material.setDiffuseColor(MapaEstructural.getInstance().getColorComercioPorNichoOscuro(lugar.getNicho())); 
                                    this.setDepth(ancho/2);
                                    break;
            case LugarState.ENTRADA:material.setSpecularColor(Color.RED);
                                    material.setDiffuseColor(Color.DARKRED); 
                                    this.setDepth(2);
                                    break;
            case LugarState.MURO:   material.setSpecularColor(Color.GRAY.darker());
                                    material.setDiffuseColor(Color.GRAY.darker().darker()); 
                                    this.setDepth(ancho/2);
                                    break;
            case LugarState.PASILLO:material.setSpecularColor(Color.BLUE.brighter());
                                    material.setDiffuseColor(Color.BLUE); 
                                    this.setDepth(2);
                                    break;
            case LugarState.PUNTO_DE_INFORMACION:material.setSpecularColor(Color.MAGENTA);
                                    material.setDiffuseColor(Color.DARKMAGENTA); 
                                    this.setDepth(ancho/2);
                                    break;
            case LugarState.SANITARIO:material.setSpecularColor(Color.CYAN);
                                    material.setDiffuseColor(Color.DARKCYAN); 
                                    this.setDepth(ancho/2);
                                    break;
            default:    System.err.println("No Pinta LugarState."+tipo);
                        break;
        }
        this.setMaterial(material);   
    }
}
