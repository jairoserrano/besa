/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import wpsMain.agents.peasant.Producto;
import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class MensajePromocional {
    
    String nombreComercio;
    
    ArrayList<Producto> productosEnPromocion;

    public String getNombreComercio() {
        return nombreComercio;
    }

    public void setNombreComercio(String nombreComercio) {
        this.nombreComercio = nombreComercio;
    }

    public ArrayList<Producto> getProductosEnPromocion() {
        return productosEnPromocion;
    }

    public void setProductosEnPromocion(ArrayList<Producto> productosEnPromocion) {
        this.productosEnPromocion = productosEnPromocion;
    }

    public MensajePromocional(String nombreComercio, ArrayList<Producto> productosEnPromocion) {
        this.nombreComercio = nombreComercio;
        this.productosEnPromocion = productosEnPromocion;
    }

    
    
}
