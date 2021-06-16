/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.util;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author dsvalencia
 */
public class Geometria {
    
    private Geometria(){
    }
    
    public static final Geometria geometria=new Geometria();
   
    public static Geometria getInstance(){
        return geometria;
    }

    
    public void rectannguloBordeRedondo(Graphics2D g, int x, int y, int ancho, int largo, double redondez, Color colorLinea, Color colorRelleno){
        g.setColor(colorLinea);
        g.drawRoundRect(x-(ancho/2),y-(largo/2),ancho,largo,(int)(ancho*redondez),(int)(largo*redondez));
        if(colorRelleno!=null){
            g.setColor(colorRelleno);
            g.fillRoundRect(x-(ancho/2),y-(largo/2),ancho,largo,(int)(ancho*redondez),(int)(largo*redondez));
        }
    }
    
    public void circulo(Graphics2D g, int x, int y, int radio, Color colorLinea, Color colorRelleno){
        g.setColor(colorLinea);
        g.drawOval(x-(radio),y-(radio),radio*2,radio*2);
        if(colorRelleno!=null){
            g.setColor(colorRelleno);
            g.fillOval(x-(radio),y-(radio),radio*2,radio*2);
        }
    }
    
}
