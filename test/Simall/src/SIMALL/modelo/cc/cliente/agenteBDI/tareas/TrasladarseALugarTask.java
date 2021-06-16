/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.tareas;

import SIMALL.modelo.cc.cliente.agenteBDI.creencias.ManejadorCliente;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.ClienteBDIBelieves;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.TrasladarseALugarGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.supervivencia.IrAlSanitarioGoalBDI;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import java.util.ArrayList;
import rational.mapping.Believes;
import rational.mapping.Task;

/**
 *
 * @author Daniel
 */
public class TrasladarseALugarTask extends Task {
    
    private boolean ejecutada;
    public TrasladarseALugarTask() {
        this.ejecutada=false;
    }
    @Override
    public void executeTask(Believes blvs) {
        ClienteBDIBelieves clienteBDIBelieves=(ClienteBDIBelieves)blvs;
        int estadoMeta=clienteBDIBelieves.getEstadoMeta(TrasladarseALugarGoalBDI.class.getName());
        String nombreEstado="DESCONOCIDO";
        String nombreMeta="DESCONOCIDO";
        ArrayList<String> nombreYEstado=clienteBDIBelieves.getNombreYEstadoDeMeta(TrasladarseALugarGoalBDI.class.getName(),estadoMeta);
        if(nombreYEstado!=null){
            if(nombreYEstado.size()==2){
                nombreMeta=nombreYEstado.get(0);
                nombreEstado=nombreYEstado.get(1);
            }
        }
        System.out.print("Entra a Tarea "+clienteBDIBelieves.getAlias()+" "+nombreMeta+" "+nombreEstado);
        if(estadoMeta==TrasladarseALugarGoalBDI.ESTADO_EJECUCION_TRASLADO_EN_CURSO){
            System.out.print(" "+MapaEstructural.getInstance().getLugarActualDeCliente(clienteBDIBelieves.getAlias()).getAlias()+" "+clienteBDIBelieves.getManejadorCliente().getAliasLugarObjetivo());
        }
        System.out.println("");
        ManejadorCliente manejadorCliente=clienteBDIBelieves.getManejadorCliente();
        int estado=clienteBDIBelieves.getEstadoMeta(TrasladarseALugarGoalBDI.class.getName());
        switch(estado){
            case TrasladarseALugarGoalBDI.ESTADO_EJECUCION_TRASLADO_EN_CURSO:
                if(manejadorCliente.tieneRutaDefinida()){
                    if(!manejadorCliente.haLLegadoALugarObjetivo()){
                        manejadorCliente.avanzarHaciaLugarObjetivo();
                        LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Avanza a "+MapaEstructural.getInstance().getLugarActualDeCliente(clienteBDIBelieves.getAlias()).getAlias()+" hacia "+clienteBDIBelieves.getManejadorCliente().getAliasLugarObjetivo());
                        this.ejecutada=true;
                        return;
                    }
                    else{
                        manejadorCliente.reiniciar();
                        clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(), TrasladarseALugarGoalBDI.ESTADO_FINALIZADA);
                        LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Ha llegado a lugar de destino "+MapaEstructural.getInstance().getLugarActualDeCliente(clienteBDIBelieves.getAlias()));
                        this.ejecutada=true;
                        return;
                    }
                }
                else{
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Se intenta trasladar a lugar, pero no tiene definido un destino.");
                }
                clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(), TrasladarseALugarGoalBDI.ESTADO_FINALIZADA);
                this.ejecutada=true;
                return;
        }
        this.ejecutada=true;
    }
    @Override
    public void interruptTask(Believes blvs) {
        System.out.println("Tarea Interrumpida "+((ClienteBDIBelieves)blvs).getAlias()+" TrasladarseALugarTask");
    }
    @Override
    public void cancelTask(Believes blvs) {
        System.out.println("Tarea Cancelada "+((ClienteBDIBelieves)blvs).getAlias()+" TrasladarseALugarTask");
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
