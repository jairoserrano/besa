/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.lookandfeel;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author dsvalencia
 */
class Union extends Line{
    public Union(int xi, int yi, int xf, int yf){
        super(xi,yi,xf,yf);
        this.setStroke(Color.DARKGRAY);
        this.setStrokeWidth(0.5);
    }
}
