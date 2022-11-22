/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import wpsMain.agents.peasant.ClienteBDIBelieves;
import wpsMain.agents.peasant.TrasladarseALugarGoalBDI;
import wpsMain.agents.peasant.IrAlSanitarioGoalBDI;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.cc.infraestructura.lugares.sanitario.agente.SanitarioState;
import SIMALL.modelo.log.LogAuditoria;
import java.util.ArrayList;
import rational.mapping.Believes;
import rational.mapping.Task;

/**
 *
 * @author Daniel
 */
public class IrAlSanitarioTask extends Task {

    private boolean ejecutada;
    public IrAlSanitarioTask() {
        this.ejecutada=false;
    }
    @Override
    public void executeTask(Believes blvs) {
        ClienteBDIBelieves clienteBDIBelieves=((ClienteBDIBelieves)blvs);
        int estadoMeta=clienteBDIBelieves.getEstadoMeta(IrAlSanitarioGoalBDI.class.getName());
        String nombreEstado="DESCONOCIDO";
        String nombreMeta="DESCONOCIDO";
        ArrayList<String> nombreYEstado=clienteBDIBelieves.getNombreYEstadoDeMeta(IrAlSanitarioGoalBDI.class.getName(),estadoMeta);
        if(nombreYEstado!=null){
            if(nombreYEstado.size()==2){
                nombreMeta=nombreYEstado.get(0);
                nombreEstado=nombreYEstado.get(1);
            }
        }
        System.out.println("Entra a Tarea "+clienteBDIBelieves.getAlias()+" "+nombreMeta+" "+nombreEstado);
        int estado=clienteBDIBelieves.getEstadoMeta(IrAlSanitarioGoalBDI.class.getName());
        switch(estado){
            case IrAlSanitarioGoalBDI.ESTADO_EJECUCION_USAR_SANITARIO: 
                clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(),TrasladarseALugarGoalBDI.ESTADO_SIN_INICIAR);
                LugarState lugarActual=MapaEstructural.getInstance().getLugarActualDeCliente(clienteBDIBelieves.getAlias());
                if(lugarActual.getClass().getName().equals(SanitarioState.class.getName())){
                    clienteBDIBelieves.usarSanitario();
                    clienteBDIBelieves.setEstadoMeta(IrAlSanitarioGoalBDI.class.getName(),IrAlSanitarioGoalBDI.ESTADO_FINALIZADA);
                    LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Usa el sanitario :)");
                    this.ejecutada=true;
                    return;
                }
                SanitarioState sanitarioMasCercano=MapaEstructural.getInstance().getSanitarioMasCercano(clienteBDIBelieves.getAlias());
                clienteBDIBelieves.getManejadorCliente().reiniciar();
                clienteBDIBelieves.getManejadorCliente().definirRutaHaciaLugarEspecífico(sanitarioMasCercano);
                clienteBDIBelieves.setEstadoMeta(TrasladarseALugarGoalBDI.class.getName(), TrasladarseALugarGoalBDI.ESTADO_EJECUCION_TRASLADO_EN_CURSO);
                clienteBDIBelieves.setEstadoMeta(IrAlSanitarioGoalBDI.class.getName(), IrAlSanitarioGoalBDI.ESTADO_ESPERA_TRASLADO_CLIENTE);
                LogAuditoria.getInstance().escribirEvento(ClienteBDIBelieves.class.getName(), clienteBDIBelieves.getAlias(), "Se traslada con urgencia al sanitario más cercano: "+sanitarioMasCercano.getAlias());
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
