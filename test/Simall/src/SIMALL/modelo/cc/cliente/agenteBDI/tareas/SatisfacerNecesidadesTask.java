/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.tareas;

import SIMALL.modelo.cc.agente.Cotizacion;
import SIMALL.modelo.log.LogAuditoria;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.ClienteBDIBelieves;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.Necesidad;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.obligacion.SatisfacerNecesidadesGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad.ComprarGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad.SalirGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.CotizarNecesidadGoalBDI;
import SIMALL.modelo.cc.infraestructura.lugares.comercio.agente.FacturaVenta;
import java.util.ArrayList;
import rational.mapping.Believes;
import rational.mapping.Task;

/**
 *
 * @author Daniel
 */
public class SatisfacerNecesidadesTask extends Task {
    
    private boolean ejecutada;
    public SatisfacerNecesidadesTask() {
        this.ejecutada=false;
    }
    @Override
    public void executeTask(Believes blvs) {
        ClienteBDIBelieves clienteBDIBelieves=((ClienteBDIBelieves)blvs);
        int estadoMeta=clienteBDIBelieves.getEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName());
        String nombreEstado="DESCONOCIDO";
        String nombreMeta="DESCONOCIDO";
        ArrayList<String> nombreYEstado=clienteBDIBelieves.getNombreYEstadoDeMeta(SatisfacerNecesidadesGoalBDI.class.getName(),estadoMeta);
        if(nombreYEstado!=null){
            if(nombreYEstado.size()==2){
                nombreMeta=nombreYEstado.get(0);
                nombreEstado=nombreYEstado.get(1);
            }
        }
        Necesidad necesidadSeleccionada=null;
        Necesidad necesidadEnCurso=clienteBDIBelieves.getNecesidadEnCurso();
        if(necesidadEnCurso!=null){
            System.out.println("Entra a Tarea "+clienteBDIBelieves.getAlias()+" "+nombreMeta+" "+nombreEstado+" "+necesidadEnCurso.getProducto().getNombre());
        }
        else{
            System.out.println("Entra a Tarea "+clienteBDIBelieves.getAlias()+" "+nombreMeta+" "+nombreEstado+" SIN NECESIDAD ASIGNADA");
        }
        
        ArrayList<Necesidad> necesidadesSinSatisfacer=clienteBDIBelieves.getNecesidadesSinSatisfacer();
        
        int estado=clienteBDIBelieves.getEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName());
        switch(estado){
            case SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD: 
                if(necesidadEnCurso==null){
                    if(necesidadesSinSatisfacer!=null){
                        if(necesidadesSinSatisfacer.size()>0){
                            double maximaIntensidad=-1;
                            for(Necesidad necesidadSinSatisfacer:necesidadesSinSatisfacer){
                                double intensidad=necesidadSinSatisfacer.getIntensidad();
                                if(maximaIntensidad==-1){
                                    maximaIntensidad=intensidad;
                                    necesidadSeleccionada=necesidadSinSatisfacer;
                                }
                                else{
                                    if(intensidad>maximaIntensidad){
                                        maximaIntensidad=intensidad;
                                        necesidadSeleccionada=necesidadSinSatisfacer;
                                    }
                                }
                            }
                            if(necesidadSeleccionada!=null){
                                clienteBDIBelieves.setNecesidadEnCurso(necesidadSeleccionada);
                                necesidadesSinSatisfacer.remove(necesidadSeleccionada);
                                clienteBDIBelieves.getManejadorCliente().reiniciar();
                                clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(),SatisfacerNecesidadesGoalBDI.ESTADO_ESPERA_REALIZAR_COTIZACIONES);
                                clienteBDIBelieves.setEstadoMeta(CotizarNecesidadGoalBDI.class.getName(), CotizarNecesidadGoalBDI.ESTADO_EJECUCION_COTIZANDO_NECESIDAD);
                                LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Empieza a Cotizar "+necesidadSeleccionada.getProducto().getNombre()+", quedan "+necesidadesSinSatisfacer.size()+" necesidades.");
                                this.ejecutada=true;
                                return;
                            }
                            else{
                                LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Se presentó un error seleccionando meta.");
                                System.err.println("Se presentó un erro al seleccionar la meta.");
                                System.exit(0);
                                this.ejecutada=true;
                                return;
                            }
                        }
                        else{
                            clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(),SatisfacerNecesidadesGoalBDI.ESTADO_FINALIZADA);
                            LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Ha terminado de satisfacer todas sus necesidades.");
                            clienteBDIBelieves.setEstadoMeta(SalirGoalBDI.class.getName(),SalirGoalBDI.ESTADO_EJECUCION_SALIR);
                            this.ejecutada=true;
                            return;
                        }
                    }
                    clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(),SatisfacerNecesidadesGoalBDI.ESTADO_FINALIZADA);
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "No tiene necesidades para satisfacer.");
                    clienteBDIBelieves.setEstadoMeta(SalirGoalBDI.class.getName(),SalirGoalBDI.ESTADO_EJECUCION_SALIR);
                    this.ejecutada=true;
                    return;
                }
                else{
                    necesidadesSinSatisfacer.remove(necesidadSeleccionada);
                    clienteBDIBelieves.getManejadorCliente().reiniciar();
                    clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(),SatisfacerNecesidadesGoalBDI.ESTADO_ESPERA_REALIZAR_COTIZACIONES);
                    clienteBDIBelieves.setEstadoMeta(CotizarNecesidadGoalBDI.class.getName(), CotizarNecesidadGoalBDI.ESTADO_EJECUCION_COTIZANDO_NECESIDAD);
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Empieza a Cotizar "+necesidadSeleccionada.getProducto().getNombre()+", quedan "+necesidadesSinSatisfacer.size()+" necesidades.");
                    this.ejecutada=true;
                    return;
                }
            case SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_EVALUAR_COTIZACIONES:
                clienteBDIBelieves.setEstadoMeta(CotizarNecesidadGoalBDI.class.getName(), CotizarNecesidadGoalBDI.ESTADO_SIN_INICIAR);
                ArrayList<Cotizacion> cotizacionesDeNecesidad=clienteBDIBelieves.getDatosComprador().getCotizaciones().get(necesidadEnCurso);
                if(cotizacionesDeNecesidad!=null){
                    if(cotizacionesDeNecesidad.size()>0){
                        clienteBDIBelieves.setEstadoMeta(ComprarGoalBDI.class.getName(), ComprarGoalBDI.ESTADO_EJECUCION_REALIZAR_COMPRA);
                        clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(), SatisfacerNecesidadesGoalBDI.ESTADO_ESPERA_REALIZAR_COMPRA);
                        LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Luego de revisar las cotizaciones, decide iniciar el proceso de compra del producto "+necesidadEnCurso.getProducto().getNombre()+".");
                        this.ejecutada=true;
                        return;
                    }
                }
                clienteBDIBelieves.getNecesidadesInsatisfizas().add(necesidadEnCurso);
                clienteBDIBelieves.setNecesidadEnCurso(null);
                clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(), SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD);
                LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Luego de revisar los comercios no se encontró disponibilidad del producto "+necesidadEnCurso.getProducto().getNombre()+".");
                this.ejecutada=true;
                return;

            case SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_EVALUAR_COMPRA_REALIZADA:
                clienteBDIBelieves.setEstadoMeta(CotizarNecesidadGoalBDI.class.getName(), CotizarNecesidadGoalBDI.ESTADO_SIN_INICIAR);
                ArrayList<FacturaVenta> facturasVenta=clienteBDIBelieves.getDatosComprador().getFacturasVenta();
                if(facturasVenta!=null){
                    for(int i=0;i<facturasVenta.size();i++){
                        if(facturasVenta.get(i).getNecesidad()==necesidadEnCurso){
                            if(necesidadEnCurso.getProducto().getNicho().equalsIgnoreCase("Restaurante")){
                                clienteBDIBelieves.alimentarse();
                            }
                            clienteBDIBelieves.getNecesidadesSatisfechas().add(necesidadEnCurso);
                            clienteBDIBelieves.setNecesidadEnCurso(null);
                            clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(), SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD);
                            LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Se realiza la compra efectiva del producto "+necesidadEnCurso.getProducto().getNombre()+".");
                            this.ejecutada=true;
                            return;
                        }
                    }
                }
                LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "No se logró realizar la compra del producto.");
                clienteBDIBelieves.getNecesidadesInsatisfizas().add(necesidadEnCurso);
                clienteBDIBelieves.setNecesidadEnCurso(null);
                clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(), SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD);
                this.ejecutada=true;
                return;
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
