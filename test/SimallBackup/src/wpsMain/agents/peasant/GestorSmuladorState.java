/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.Kernel.Agent.StateBESA;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;

/**
 *
 * @author Daniel
 */
public class GestorSmuladorState extends StateBESA {

    private int cantidadClientes=0;
    
    public GestorSmuladorState(){
    }

    public String crearCliente() {
        return "Cliente"+cantidadClientes++;
    }

    public boolean topeClientes() {
        return cantidadClientes<MapaEstructural.getInstance().getCantidadClientes();
    }
    
}
