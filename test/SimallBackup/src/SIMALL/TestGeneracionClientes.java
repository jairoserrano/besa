
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL;

import wpsMain.agents.peasant.ClienteBDIBelieves;
import wpsMain.agents.peasant.DatosComprador;
import wpsMain.agents.peasant.DatosDemograficos;
import wpsMain.agents.peasant.DatosEconomicos;
import wpsMain.agents.peasant.Interes;
import wpsMain.agents.peasant.Necesidad;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import java.util.ArrayList;

/**
 *
 * @author dsvalencia
 */
public class TestGeneracionClientes {
    public static void main(String[] args) {
        ArrayList<ClienteBDIBelieves> clientes=new ArrayList();
        
        for(int i=0;i<100000;i++){
            String nombre="Cliente"+i;
            DatosComprador datosCompradorAleatorios=MapaEstructural.getInstance().getDemografia().getMercadoObjetivo().generarDatosCompradorAleatorios(nombre);
            DatosDemograficos datosDemograficosAleatorios=MapaEstructural.getInstance().getDemografia().getMercadoObjetivo().generarDatosDemogaficosAleatorios(nombre);
            DatosEconomicos datosEcnomicosAleatorios=datosDemograficosAleatorios.generarDatosEconomicosAleatorios(datosDemograficosAleatorios);
            ArrayList<Interes> interesesAleatorios=MapaEstructural.getInstance().getArbolCategorias().getInteresesAsequiblesAleatorios(datosDemograficosAleatorios, datosEcnomicosAleatorios);
            clientes.add(new ClienteBDIBelieves(nombre, interesesAleatorios, datosCompradorAleatorios, datosDemograficosAleatorios, datosEcnomicosAleatorios)); 
        }
        System.out.println("Listo!");
        for(ClienteBDIBelieves cliente:clientes){
            System.out.println(cliente);
        }
        
    }
}
