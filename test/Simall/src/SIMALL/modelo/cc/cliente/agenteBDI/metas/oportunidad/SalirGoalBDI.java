/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.metas.oportunidad;

import BESA.BDI.AgentStructuralModel.GoalBDI;
import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.BDI.AgentStructuralModel.StateBDI;
import BESA.Kernel.Agent.Event.KernellAgentEventExceptionBESA;
import SIMALL.modelo.cc.cliente.agenteBDI.creencias.ClienteBDIBelieves;
import SIMALL.modelo.cc.cliente.agenteBDI.metas.requerimiento.TrasladarseALugarGoalBDI;
import rational.RationalRole;
import rational.mapping.Believes;

/**
 *
 * @author Daniel
 */
public class SalirGoalBDI extends GoalBDI{

    public static final int ESTADO_SIN_INICIAR=0;
    public static final int ESTADO_FINALIZADA=1;
    
    public static final int ESTADO_EJECUCION_SALIR=2;
    public static final int ESTADO_ESPERA_TRASLADO_CLIENTE=3;
    

    public SalirGoalBDI(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    @Override
    public double evaluateViability(Believes blvs) throws KernellAgentEventExceptionBESA {
        ClienteBDIBelieves clienteBDIBelieves=(ClienteBDIBelieves)blvs;
        int estado=clienteBDIBelieves.getEstadoMeta(SalirGoalBDI.class.getName());
        switch(estado){
            case ESTADO_EJECUCION_SALIR:
                return 1;
            case ESTADO_ESPERA_TRASLADO_CLIENTE:
                if(clienteBDIBelieves.getEstadoMeta(TrasladarseALugarGoalBDI.class.getName())==TrasladarseALugarGoalBDI.ESTADO_FINALIZADA){
                    clienteBDIBelieves.setEstadoMeta(SalirGoalBDI.class.getName(),SalirGoalBDI.ESTADO_EJECUCION_SALIR);
                    return 1;
                }
        }
        return 0;
    }

    @Override
    public double detectGoal(Believes blvs) throws KernellAgentEventExceptionBESA {
        return 1;
    }

    @Override
    public double evaluatePlausibility(Believes blvs) throws KernellAgentEventExceptionBESA {
        return 1;
    }

    @Override
    public double evaluateContribution(StateBDI sbdi) throws KernellAgentEventExceptionBESA {
        return 1;
    }

    @Override
    public boolean predictResultUnlegality(StateBDI sbdi) throws KernellAgentEventExceptionBESA {
        return true;
    }

    @Override
    public boolean goalSucceeded(Believes blvs) throws KernellAgentEventExceptionBESA {
        /*int estado=((ClienteBDIBelieves)blvs).getEstadoMeta(ComprarGoalBDI.class.getName());
        switch(estado){
            case ESTADO_FINALIZADA: return true; 
        }*/
        return false;
    }
    
}
