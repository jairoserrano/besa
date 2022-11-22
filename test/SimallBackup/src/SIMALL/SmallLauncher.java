/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import rational.data.InfoData;
import wpsMain.agents.peasant.CentroComercialAgent;
import wpsMain.agents.peasant.CentroComercialState;
import wpsMain.agents.peasant.CrearClienteGuard;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import wpsMain.agents.peasant.LugarState;
import wpsMain.agents.peasant.ComercioState;
import wpsMain.agents.peasant.GestorSimuladorAgent;
import wpsMain.agents.peasant.GestorSmuladorState;
import wpsMain.agents.peasant.GenerarClientePeriodicGuard;
import wpsMain.agents.peasant.IniciarGeneracionGuard;
import SIMALL.modelo.log.LogAuditoria;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dsvalencia
 */
public class SmallLauncher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MapaEstructural mapa = MapaEstructural.getInstance();
        try {
            MapaEstructural mapaEstructural = MapaEstructural.getInstance();
            LogAuditoria.getInstance();
            System.out.println("Inicia Simulación");

            CentroComercialState centroComercialState = new CentroComercialState();
            StructBESA centroComercialStruct = new StructBESA();
            centroComercialStruct.addBehavior("CrearClienteGuard");
            centroComercialStruct.bindGuard("CrearClienteGuard", CrearClienteGuard.class);
            CentroComercialAgent centroComercialAgent = new CentroComercialAgent("CentroComercialAgent", centroComercialState, centroComercialStruct, 0.91);
            centroComercialAgent.start();

            GestorSmuladorState gestorSimuladorState = new GestorSmuladorState();
            StructBESA gestorSimuladorStruct = new StructBESA();
            gestorSimuladorStruct.addBehavior("IniciarGeneracionGuard");
            gestorSimuladorStruct.bindGuard("IniciarGeneracionGuard", IniciarGeneracionGuard.class);
            gestorSimuladorStruct.addBehavior("GenerarClientePeriodicGuard");
            gestorSimuladorStruct.bindGuard("GenerarClientePeriodicGuard", GenerarClientePeriodicGuard.class);
            GestorSimuladorAgent gestorSimuladorAgent = new GestorSimuladorAgent("GestorSimuladorAgent", gestorSimuladorState, gestorSimuladorStruct, 0.91);
            gestorSimuladorAgent.start();

            AgHandlerBESA agHandler = AdmBESA.getInstance().getHandlerByAlias("GestorSimuladorAgent");
            EventBESA eventBesa = new EventBESA(IniciarGeneracionGuard.class.getName(), new InfoData("Iniciar Generación"));
            agHandler.sendEvent(eventBesa);

            Thread.sleep(MapaEstructural.getInstance().getTiempoTotalSimulacion());
            MapaEstructural.getInstance().mostrarCompras();

            //---------------------------
            try {
                AdmBESA adm = AdmBESA.getInstance();
                adm.killAgent(adm.getHandlerByAlias("GestorSimuladorAgent").getAgId(), 0.91);
                adm.killAgent(adm.getHandlerByAlias("CentroComercialAgent").getAgId(), 0.91);
                Set<String> clientes = mapaEstructural.getClientes().keySet();
                Iterator i = clientes.iterator();
                while (i.hasNext()) {
                    adm.killAgent(adm.getHandlerByAlias((String) i.next()).getAgId(), 0.91);
                }
                ArrayList<LugarState> lugares = mapaEstructural.getGrafoLugares().getVertices();
                for (int j = 0; j < lugares.size(); j++) {
                    adm.killAgent(adm.getHandlerByAlias(lugares.get(j).getAlias()).getAgId(), 0.91);
                }

            } catch (ExceptionBESA e) {
                LogAuditoria.getInstance().escribirError("Main", "Deteniendo el Simulador", e);
            }
            //---------------------------
            
            
            int cantidadVentasTotales = 0;
            int valorVentasTotales = 0;
            Collection<LugarState> comercios = mapaEstructural.getAlmacenes().values();
            Iterator i = comercios.iterator();
            while (i.hasNext()) {
                ComercioState comercio = (ComercioState) i.next();
                valorVentasTotales += comercio.getTotalValorVentasRealizadas();
                cantidadVentasTotales += comercio.getTotalCantidadVentasRealizadas();
            }
            LogAuditoria.getInstance().crearLog("Resumen", "Resumen");
            LogAuditoria.getInstance().escribirMensaje("Resumen", "Resumen", "Clusterizado: " + mapaEstructural.isClusterizado() + ";" + cantidadVentasTotales + ";" + valorVentasTotales);
            System.out.println("Clusterizado: " + mapaEstructural.isClusterizado() + ";" + cantidadVentasTotales + ";" + valorVentasTotales);
            File archivo = new File("salida.txt");
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            try ( FileWriter fw = new FileWriter(archivo, true)) {
                fw.write("Clusterizado: " + mapaEstructural.isClusterizado() + ";" + cantidadVentasTotales + ";" + valorVentasTotales);
                fw.write("\r\n");
            }
        } catch (ExceptionBESA | IOException | InterruptedException e) {
            LogAuditoria.getInstance().escribirError("Main", "Main", e);
        }
    }

    private void detenerSimulador() {

    }
}
