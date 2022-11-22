/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.metas.obligacion;

import BESA.BDI.AgentStructuralModel.GoalBDI;
import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.BDI.AgentStructuralModel.StateBDI;
import BESA.Kernel.Agent.Event.KernellAgentEventExceptionBESA;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.ClienteBDIBelieves;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.Necesidad;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad.ComprarGoalBDI;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.CotizarNecesidadGoalBDI;
import java.util.ArrayList;
import rational.RationalRole;
import rational.mapping.Believes;

/**
 *
 * @author Daniel
 */
public class SatisfacerNecesidadesGoalBDI extends GoalBDI{
    
    public static final int ESTADO_SIN_INICIAR=0;
    public static final int ESTADO_FINALIZADA=1;
    
    public static final int ESTADO_EJECUCION_ASIGNANDO_NECESIDAD=2;
    public static final int ESTADO_ESPERA_REALIZAR_COTIZACIONES=3;
    public static final int ESTADO_EJECUCION_EVALUAR_COTIZACIONES=4;
    public static final int ESTADO_ESPERA_REALIZAR_COMPRA=5;
    public static final int ESTADO_EJECUCION_EVALUAR_COMPRA_REALIZADA=6;
    
    public SatisfacerNecesidadesGoalBDI(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    @Override
    public double evaluateViability(Believes blvs) throws KernellAgentEventExceptionBESA {
        ClienteBDIBelieves clienteBDIBelieves=(ClienteBDIBelieves)blvs;
        int estadoMeta=clienteBDIBelieves.getEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName());
        switch(estadoMeta){
            case SatisfacerNecesidadesGoalBDI.ESTADO_SIN_INICIAR:
                clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(), SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD);
                return 1;
            case SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD:
            case SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_EVALUAR_COTIZACIONES:
            case SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_EVALUAR_COMPRA_REALIZADA:
                return 1;
            case SatisfacerNecesidadesGoalBDI.ESTADO_ESPERA_REALIZAR_COTIZACIONES: 
                if(clienteBDIBelieves.getEstadoMeta(CotizarNecesidadGoalBDI.class.getName())==CotizarNecesidadGoalBDI.ESTADO_FINALIZADA){
                    clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(), SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_EVALUAR_COTIZACIONES);
                    return 1;
                }
                return 0;
            case SatisfacerNecesidadesGoalBDI.ESTADO_ESPERA_REALIZAR_COMPRA: 
                if(clienteBDIBelieves.getEstadoMeta(ComprarGoalBDI.class.getName())==ComprarGoalBDI.ESTADO_FINALIZADA){
                    clienteBDIBelieves.setEstadoMeta(SatisfacerNecesidadesGoalBDI.class.getName(), SatisfacerNecesidadesGoalBDI.ESTADO_EJECUCION_EVALUAR_COMPRA_REALIZADA);
                    return 1;
                }
                return 0;
        }
        return 0;
    }

    @Override
    public double detectGoal(Believes blvs) throws KernellAgentEventExceptionBESA {
        Necesidad necesidadEnCurso=((ClienteBDIBelieves)blvs).getNecesidadEnCurso();
        if(necesidadEnCurso!=null){
            //System.out.println("Necesidad a Satisfacer: "+necesidadEnCurso);
            return 1;
        }
        ArrayList necesidadesSinSatisfacer=((ClienteBDIBelieves)blvs).getNecesidadesSinSatisfacer();
        if(necesidadesSinSatisfacer!=null){
            if(necesidadesSinSatisfacer.size()>0){
                //System.out.println("Cantidad de Necesidades a Satisfacer: "+necesidadesSinSatisfacer.size());
                return 1;
            }
        }
        return 0;
    }

    @Override
    public double evaluatePlausibility(Believes blvs) throws KernellAgentEventExceptionBESA {
        return 1;
    }

    @Override
    public double evaluateContribution(StateBDI sbdi) throws KernellAgentEventExceptionBESA {
        ArrayList necesidadesSinSatisfacer=((ClienteBDIBelieves)sbdi.getBelieves()).getNecesidadesSinSatisfacer();
        if(necesidadesSinSatisfacer!=null){
            if(necesidadesSinSatisfacer.size()>=0){
                return 1;
            }
        }
        return 0;
    }

    @Override
    public boolean predictResultUnlegality(StateBDI sbdi) throws KernellAgentEventExceptionBESA {
        return true;
    }

    @Override
    public boolean goalSucceeded(Believes blvs) throws KernellAgentEventExceptionBESA {
        /*ArrayList necesidadesSinSatisfacer=((ClienteBDIBelieves)blvs).getNecesidadesSinSatisfacer();
        if(necesidadesSinSatisfacer!=null){
            if(necesidadesSinSatisfacer.size()>=0){
                return false;
            }
        }*/
        return false;
    }
    
}
