/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.tareas;

import SIMALL.modelo.cc.agente.Cotizacion;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.ClienteBDIBelieves;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.DatosComprador;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.Necesidad;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.CotizarNecesidadGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.TrasladarseALugarGoalBDI;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.cc.infraestructura.lugares.comercio.agente.ComercioState;
import java.util.ArrayList;
import rational.mapping.Believes;
import rational.mapping.Task;

/**
 *
 * @author Daniel
 */
public class CotizarNecesidadTask extends Task {
    
    private boolean ejecutada;
    private ComercioState comercioACotizar=null;
    
    public CotizarNecesidadTask() {
        this.ejecutada=false;
    }
    @Override
    public void executeTask(Believes blvs) {
        ClienteBDIBelieves clienteBDIBelieves=(ClienteBDIBelieves)blvs;
        int estadoMeta=clienteBDIBelieves.getEstadoMeta(CotizarNecesidadGoalBDI.class.getName());
        String nombreEstado="DESCONOCIDO";
        String nombreMeta="DESCONOCIDO";
        ArrayList<String> nombreYEstado=clienteBDIBelieves.getNombreYEstadoDeMeta(CotizarNecesidadGoalBDI.class.getName(),estadoMeta);
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
        int estado=clienteBDIBelieves.getEstadoMeta(CotizarNecesidadGoalBDI.class.getName());
        switch(estado){
            case CotizarNecesidadGoalBDI.ESTADO_EJECUCION_COTIZANDO_NECESIDAD:
                clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(),TrasladarseALugarGoalBDI.ESTADO_SIN_INICIAR);
                if(necesidadEnCurso!=null){
                    if(comercioACotizar==null){
                        comercioACotizar=datosComprador.siguienteComercioACotizar(clienteBDIBelieves.getAlias(), necesidadEnCurso);
                    }
                    if(comercioACotizar!=null){
                        if(comercioACotizar==MapaEstructural.getInstance().getLugarActualDeCliente(clienteBDIBelieves.getAlias())){
                            clienteBDIBelieves.getManejadorCliente().reiniciar();
                            clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(), TrasladarseALugarGoalBDI.ESTADO_SIN_INICIAR);
                            Cotizacion cotizacion=comercioACotizar.generarCotizacion(clienteBDIBelieves.getNecesidadEnCurso().getProducto());
                            if(cotizacion!=null){
                                datosComprador.registrarCotizacion(necesidadEnCurso, cotizacion);
                                LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Registra cotización del producto "+necesidadEnCurso.getProducto().getNombre()+" en "+comercioACotizar.getAlias());
                            }
                            else{
                                LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "El comercio "+comercioACotizar.getAlias()+" no pudo generar una cotizació del producto "+necesidadEnCurso.getProducto().getNombre());
                            }
                            comercioACotizar=null;
                            this.ejecutada=true;
                            return;
                        }
                        else{
                            clienteBDIBelieves.getManejadorCliente().reiniciar();
                            clienteBDIBelieves.getManejadorCliente().definirRutaHaciaLugarEspecífico(comercioACotizar);
                            clienteBDIBelieves.setEstadoMeta(CotizarNecesidadGoalBDI.class.getName(), CotizarNecesidadGoalBDI.ESTADO_ESPERA_TRASLADO_CLIENTE);
                            clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(), TrasladarseALugarGoalBDI.ESTADO_EJECUCION_TRASLADO_EN_CURSO);
                            LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Inicia desplazamiento para cotizar el producto "+necesidadEnCurso.getProducto().getNombre()+" en "+comercioACotizar.getAlias());
                            this.ejecutada=true;
                            return;
                        }
                    }
                    else{
                        LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "No existen más comercios para cotizar.");
                    }
                }
                else{
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Intenta cotizar, sin embargo no hay una necesidad en curso.");
                }
                clienteBDIBelieves.setEstadoMeta(CotizarNecesidadGoalBDI.class.getName(), CotizarNecesidadGoalBDI.ESTADO_FINALIZADA);
                this.ejecutada=true;
                return;
            /*case CotizarNecesidadGoalBDI.ESTADO_EJECUCION_OBTENIENDO_COTIZACION:
                clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(),TrasladarseALugarGoalBDI.ESTADO_SIN_INICIAR);
                Cotizacion cotizacion=((ComercioState)MapaEstructural.getInstance().getClientes().get(clienteBDIBelieves.getAlias())).generarCotizacion(clienteBDIBelieves.getNecesidadEnCurso().getProducto());
                if(cotizacion!=null){
                    datosComprador.registrarCotizacion(necesidadEnCurso, cotizacion);
                }
                clienteBDIBelieves.setEstadoMeta(CotizarNecesidadGoalBDI.class.getName(), CotizarNecesidadGoalBDI.ESTADO_FINALIZADA);
                this.ejecutada=true;
                return;*/
        }
        this.ejecutada=true;
    }
    @Override
    public void interruptTask(Believes blvs) {
        System.out.println("Tarea Interrumpida "+((ClienteBDIBelieves)blvs).getAlias()+" CotizarNecesidadTask");
    }
    @Override
    public void cancelTask(Believes blvs) {
        System.out.println("Tarea Cancelada "+((ClienteBDIBelieves)blvs).getAlias()+" CotizarNecesidadTask");
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
