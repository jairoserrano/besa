/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.lookandfeel;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;

/**
 *
 * @author dsvalencia
 */
public class LabelEdificio extends Label{
    private LugarState lugar;
        
    public LugarState getLugar(){
        return lugar;
    }
    
    public LabelEdificio(LugarState lugar){
        this.lugar=lugar;
        configurarLabel();
    }

    
    private void configurarLabel() {
        this.setText(lugar.getAlias());
        this.setBorder(new Border(new BorderStroke(Color.DARKGREY,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setFont(Font.font("Verdana", 10));
    }
}
