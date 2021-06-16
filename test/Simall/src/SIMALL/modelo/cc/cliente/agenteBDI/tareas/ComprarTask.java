/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.tareas;

import SIMALL.modelo.cc.agente.Cotizacion;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.ClienteBDIBelieves;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.DatosComprador;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.Necesidad;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad.ComprarGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.TrasladarseALugarGoalBDI;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.cc.infraestructura.lugares.comercio.agente.ComercioState;
import SIMALL.modelo.log.LogAuditoria;
import java.util.ArrayList;
import rational.mapping.Believes;
import rational.mapping.Task;

/**
 *
 * @author Daniel
 */
public class ComprarTask extends Task {
    
    private boolean ejecutada;
    public ComprarTask() {
        this.ejecutada=false;
    }
    @Override
    public void executeTask(Believes blvs) {
        ClienteBDIBelieves clienteBDIBelieves=(ClienteBDIBelieves)blvs;
        int estadoMeta=clienteBDIBelieves.getEstadoMeta(ComprarGoalBDI.class.getName());
        String nombreEstado="DESCONOCIDO";
        String nombreMeta="DESCONOCIDO";
        ArrayList<String> nombreYEstado=clienteBDIBelieves.getNombreYEstadoDeMeta(ComprarGoalBDI.class.getName(),estadoMeta);
        if(nombreYEstado!=null){
            if(nombreYEstado.size()==2){
                nombreMeta=nombreYEstado.get(0);
                nombreEstado=nombreYEstado.get(1);
            }
        }
        Necesidad necesidadEnCurso=clienteBDIBelieves.getNecesidadEnCurso();
        if(necesidadEnCurso!=null){
            System.out.println("Entra a Tarea "+clienteBDIBelieves.getAlias()+" "+nombreMeta+" "+nombreEstado+" "+necesidadEnCurso.getProducto().getNombre());
        }
        else{
            System.out.println("Entra a Tarea "+clienteBDIBelieves.getAlias()+" "+nombreMeta+" "+nombreEstado+" NECESIDAD EN CURSO SIN ASIGNAR");
        }
        DatosComprador datosComprador=clienteBDIBelieves.getDatosComprador();
        int estado=clienteBDIBelieves.getEstadoMeta(ComprarGoalBDI.class.getName());
        switch(estado){
            case ComprarGoalBDI.ESTADO_EJECUCION_REALIZAR_COMPRA:
                clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(),TrasladarseALugarGoalBDI.ESTADO_SIN_INICIAR);
                if(necesidadEnCurso!=null){
                    Cotizacion cotizacion=datosComprador.getMejorCotizacion(necesidadEnCurso);
                    if(cotizacion!=null){
                        ComercioState comercioSeleccionado=(ComercioState)MapaEstructural.getInstance().getLugarPorNombre(cotizacion.getNombreComercio());
                        if(comercioSeleccionado==MapaEstructural.getInstance().getLugarActualDeCliente(clienteBDIBelieves.getAlias())){
                            clienteBDIBelieves.getManejadorCliente().reiniciar();
                            LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Intentar realizar la compra de "+cotizacion.getProucto().getNombre()+" en "+comercioSeleccionado.getAlias());
                            datosComprador.realizarCompra(necesidadEnCurso, comercioSeleccionado, clienteBDIBelieves.getAlias(), clienteBDIBelieves.getDatosEconomicos());
                            clienteBDIBelieves.setEstadoMeta(ComprarGoalBDI.class.getName(), ComprarGoalBDI.ESTADO_FINALIZADA);
                            this.ejecutada=true;
                            return;
                        }
                        else{
                            clienteBDIBelieves.getManejadorCliente().reiniciar();
                            clienteBDIBelieves.getManejadorCliente().definirRutaHaciaLugarEspecífico(comercioSeleccionado);
                            clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(), TrasladarseALugarGoalBDI.ESTADO_EJECUCION_TRASLADO_EN_CURSO);
                            clienteBDIBelieves.setEstadoMeta(ComprarGoalBDI.class.getName(), ComprarGoalBDI.ESTADO_ESPERA_TRASLADO_CLIENTE);
                            LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Inicia tralsado al comercio "+comercioSeleccionado.getAlias()+" para realizar la compra.");
                            this.ejecutada=true;
                            return;
                        }
                    }
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Intenta comprar, pero no tiene definida la mejor cotización.");
                }
                else{
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Intenta comprar, pero no tiene definida una necesidad en curso.");
                }
                clienteBDIBelieves.setEstadoMeta(ComprarGoalBDI.class.getName(), ComprarGoalBDI.ESTADO_FINALIZADA);
                this.ejecutada=true;
                return;
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
