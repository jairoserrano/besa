/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.tareas;

import SIMALL.modelo.cc.agente.Cotizacion;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.ClienteBDIBelieves;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.DatosComprador;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad.ComprarGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad.SalirGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.TrasladarseALugarGoalBDI;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.cc.infraestructura.lugares.comercio.agente.ComercioState;
import SIMALL.modelo.cc.infraestructura.lugares.entrada.agente.EntradaState;
import SIMALL.modelo.log.LogAuditoria;
import java.util.ArrayList;
import rational.mapping.Believes;
import rational.mapping.Task;

/**
 *
 * @author Daniel
 */
public class SalirTask extends Task {

    private boolean ejecutada;
    public SalirTask() {
        this.ejecutada=false;
    }
    @Override
    public void executeTask(Believes blvs) {
        ClienteBDIBelieves clienteBDIBelieves=(ClienteBDIBelieves)blvs;
        int estadoMeta=clienteBDIBelieves.getEstadoMeta(SalirGoalBDI.class.getName());
        String nombreEstado="DESCONOCIDO";
        String nombreMeta="DESCONOCIDO";
        ArrayList<String> nombreYEstado=clienteBDIBelieves.getNombreYEstadoDeMeta(SalirGoalBDI.class.getName(),estadoMeta);
        if(nombreYEstado!=null){
            if(nombreYEstado.size()==2){
                nombreMeta=nombreYEstado.get(0);
                nombreEstado=nombreYEstado.get(1);
            }
        }
        System.out.println("Entra a Tarea "+clienteBDIBelieves.getAlias()+" "+nombreMeta+" "+nombreEstado);
        DatosComprador datosComprador=clienteBDIBelieves.getDatosComprador();
        int estado=clienteBDIBelieves.getEstadoMeta(SalirGoalBDI.class.getName());
        switch(estado){
            case SalirGoalBDI.ESTADO_EJECUCION_SALIR:
                if(MapaEstructural.getInstance().getLugarActualDeCliente(clienteBDIBelieves.getAlias()).getClass().getName().equals(EntradaState.class.getName())){
                    clienteBDIBelieves.setEstadoMeta(SalirGoalBDI.class.getName(),SalirGoalBDI.ESTADO_FINALIZADA);
                    clienteBDIBelieves.salir();
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Sale en forma exitosa del centro comercial.");
                    this.ejecutada=true;
                    return;
                }
                else{
                    EntradaState entrada=MapaEstructural.getInstance().getEntradaMasCercana(clienteBDIBelieves.getAlias());
                    clienteBDIBelieves.getManejadorCliente().reiniciar();
                    clienteBDIBelieves.getManejadorCliente().definirRutaHaciaLugarEspecífico(entrada);
                    clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(), TrasladarseALugarGoalBDI.ESTADO_EJECUCION_TRASLADO_EN_CURSO);
                    clienteBDIBelieves.setEstadoMeta(SalirGoalBDI.class.getName(), SalirGoalBDI.ESTADO_ESPERA_TRASLADO_CLIENTE);
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Inicia tralsado hacia "+entrada.getAlias()+" para salir del centro comercial.");
                    this.ejecutada=true;
                    return;
                }
        }
        this.ejecutada=true;
    }
    @Override
    public void interruptTask(Believes blvs) {
        System.out.println("Tarea Interrumpida "+((ClienteBDIBelieves)blvs).getAlias()+" ComprarTask");
    }
    @Override
    public void cancelTask(Believes blvs) {
        System.out.println("Tarea Cancelada "+((ClienteBDIBelieves)blvs).getAlias()+" ComprarTask");
    }
    public boolean isExecuted() {
        if(this.ejecutada && this.taskState == STATE.WAITING_FOR_EXECUTION){
            this.ejecutada=false;
        }
        return ejecutada;
    }
    @Override
    public boolean checkFinish(Believes blvs) {
        return  isExecuted();
    }

    
    
}
