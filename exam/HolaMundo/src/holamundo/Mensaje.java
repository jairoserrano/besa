package holamundo;

import BESA.Kernel.Agent.Event.DataBESA;

public class Mensaje extends DataBESA {
    
    private String contenido;

    public Mensaje(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
}
