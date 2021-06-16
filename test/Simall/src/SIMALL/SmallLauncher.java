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
import SIMALL.lookandfeel.Lienzo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import rational.data.InfoData;
import SIMALL.modelo.cc.agente.CentroComercialAgent;
import SIMALL.modelo.cc.agente.CentroComercialState;
import SIMALL.modelo.cc.agente.guardas.CrearClienteGuard;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.cc.infraestructura.lugar.agente.LugarState;
import SIMALL.modelo.cc.infraestructura.lugares.comercio.agente.ComercioState;
import SIMALL.modelo.gestorSimulador.agente.GestorSimuladorAgent;
import SIMALL.modelo.gestorSimulador.agente.GestorSmuladorState;
import SIMALL.modelo.gestorSimulador.agente.guardas.GenerarClientePeriodicGuard;
import SIMALL.modelo.gestorSimulador.agente.guardas.IniciarGeneracionGuard;
import SIMALL.modelo.log.LogAuditoria;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dsvalencia
 */
public class SmallLauncher extends Application {

    @Override
    public void start(Stage primaryStage) {
        MapaEstructural mapa = MapaEstructural.getInstance();
        iniciarSimulador();
        Lienzo lienzo = new Lienzo();
        Scene scene = new Scene(lienzo, mapa.getOffSet() * 2, mapa.getOffSet() * 2, Color.WHITE);
        lienzo.configurar();
        primaryStage.setTitle("Simall");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void iniciarSimulador() {
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
            detenerSimulador();
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
            try (FileWriter fw = new FileWriter(archivo, true)) {
                fw.write("Clusterizado: " + mapaEstructural.isClusterizado() + ";" + cantidadVentasTotales + ";" + valorVentasTotales);
                fw.write("\r\n");
            }
        } catch (ExceptionBESA | IOException | InterruptedException e) {
            LogAuditoria.getInstance().escribirError("Main", "Main", e);
        }
    }

    private void detenerSimulador() {
        try {
            MapaEstructural mapaEstructural = MapaEstructural.getInstance();
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
    }
}
