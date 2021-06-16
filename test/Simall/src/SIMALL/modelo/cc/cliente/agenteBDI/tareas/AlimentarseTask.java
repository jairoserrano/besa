/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.tareas;

import SIMALL.modelo.cc.cliente.agenteBDI.creencias.ClienteBDIBelieves;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad.SalirGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.supervivencia.AlimentarseGoalBDI;
import SIMALL.modelo.log.LogAuditoria;
import java.util.ArrayList;
import rational.mapping.Believes;
import rational.mapping.Task;

/**
 *
 * @author Daniel
 */
public class AlimentarseTask extends Task {
    
    private boolean ejecutada;
    public AlimentarseTask() {
        this.ejecutada=false;
    }
    @Override
    public void executeTask(Believes blvs) {
        ClienteBDIBelieves clienteBDIBelieves=((ClienteBDIBelieves)blvs);
        int estadoMeta=clienteBDIBelieves.getEstadoMeta(AlimentarseGoalBDI.class.getName());
        String nombreEstado="DESCONOCIDO";
        String nombreMeta="DESCONOCIDO";
        ArrayList<String> nombreYEstado=clienteBDIBelieves.getNombreYEstadoDeMeta(AlimentarseGoalBDI.class.getName(),estadoMeta);
        if(nombreYEstado!=null){
            if(nombreYEstado.size()==2){
                nombreMeta=nombreYEstado.get(0);
                nombreEstado=nombreYEstado.get(1);
            }
        }
        System.out.println("Entra a Tarea "+clienteBDIBelieves.getAlias()+" "+nombreMeta+" "+nombreEstado);
        int estado=clienteBDIBelieves.getEstadoMeta(AlimentarseGoalBDI.class.getName());
        switch(estado){
            case AlimentarseGoalBDI.ESTADO_EJECUCION_PRIORIZA_ALIMENTACION: 
                if(clienteBDIBelieves.priorizarAlimentacion()){
                    clienteBDIBelieves.setEstadoMeta(AlimentarseGoalBDI.class.getName(),AlimentarseGoalBDI.ESTADO_ESPERA_ALIMENTARSE);
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Prioriza la necesidad de Alimentacion: "+clienteBDIBelieves.getNecesidadAlimentacionPriorizada().getProducto().getNombre());
                    System.out.println("Prioriza la necesidad de Alimentacion: "+clienteBDIBelieves.getNecesidadAlimentacionPriorizada().getProducto().getNombre());
                    this.ejecutada=true;
                    return;
                }
                else{
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Al no encontrar satisfacer su hambre, sale del centro comercial.");
                    clienteBDIBelieves.setEstadoMeta(AlimentarseGoalBDI.class.getName(),AlimentarseGoalBDI.ESTADO_FINALIZADA);                    
                    clienteBDIBelieves.setEstadoMeta(SalirGoalBDI.class.getName(),SalirGoalBDI.ESTADO_EJECUCION_SALIR);
                    this.ejecutada=true;
                    return;
                }
        }
        this.ejecutada=true;
        return;
    }
    @Override
    public void interruptTask(Believes blvs) {
        System.out.println("Tarea Interrumpida "+((ClienteBDIBelieves)blvs).getAlias()+" SatisfacerNecesidadesTask");
    }
    @Override
    public void cancelTask(Believes blvs) {
        System.out.println("Tarea Cancelada "+((ClienteBDIBelieves)blvs).getAlias()+" SatisfacerNecesidadesTask");
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
