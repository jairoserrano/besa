/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.creencias;

import SIMALL.modelo.cc.agente.Producto;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.Const;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.Const.Semantica;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.emociones.EmotionAxis;

/**
 *
 * @author dsvalencia
 */
public class Necesidad {
    private Producto producto; 
    
    private ComponenteEmocional componenteEmocional;
    private EmotionAxis ejeEmocional;
    
    public Necesidad(Producto producto, ComponenteEmocional componenteEmocional, float intensidad) {
        this.producto = producto;
        this.componenteEmocional=componenteEmocional;
        ejeEmocional=new EmotionAxis(Const.Semantica.Emociones.Felicidad+producto.getNombre(), Const.Semantica.Emociones.Tristeza+producto.getNombre(), intensidad, 0.0f, 0.01f);
        componenteEmocional.addEmotionAxis(ejeEmocional);
    }
    
    public void setIntensidad(float intensidad){
        ejeEmocional=new EmotionAxis(Semantica.Emociones.Felicidad+producto.getNombre(), Semantica.Emociones.Tristeza+producto.getNombre(), intensidad, 0.0f, 0.01f);
        componenteEmocional.addEmotionAxis(ejeEmocional);
    }

    public Necesidad(Necesidad necesidad) {
        if(necesidad!=null){
            this.producto=null;
            if(necesidad.producto!=null){
                this.producto = new Producto(necesidad.producto);
            }
            this.componenteEmocional = necesidad.componenteEmocional;
        }        
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getIntensidad() {
        return ejeEmocional.getCurrentValue();
    }

    @Override
    public String toString(){
        String cadena="Necesidades: \n";
        cadena+=producto;
        return cadena+=" Intensidad: "+getIntensidad();
    }
}
