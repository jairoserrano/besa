/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.BDI.AgentStructuralModel.GoalBDI;
import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.BDI.AgentStructuralModel.StateBDI;
import BESA.Kernel.Agent.Event.KernellAgentEventExceptionBESA;
import wpsMain.agents.peasant.ClienteBDIBelieves;
import wpsMain.agents.peasant.SatisfacerNecesidadesGoalBDI;
import rational.RationalRole;
import rational.mapping.Believes;

/**
 *
 * @author Daniel
 */
public class CotizarNecesidadGoalBDI extends GoalBDI{
    
    public static final int ESTADO_SIN_INICIAR=0;
    public static final int ESTADO_FINALIZADA=1;
    
    public static final int ESTADO_EJECUCION_COTIZANDO_NECESIDAD=2;
    public static final int ESTADO_ESPERA_TRASLADO_CLIENTE=3;
    public static final int ESTADO_EJECUCION_OBTENIENDO_COTIZACION=5;
        

    public CotizarNecesidadGoalBDI(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    @Override
    public double evaluateViability(Believes blvs) throws KernellAgentEventExceptionBESA {
        ClienteBDIBelieves clienteBDIBelieves=(ClienteBDIBelieves)blvs;
        int estado=clienteBDIBelieves.getEstadoMeta(CotizarNecesidadGoalBDI.class.getName());
        switch(estado){
            case ESTADO_EJECUCION_COTIZANDO_NECESIDAD:
            case ESTADO_EJECUCION_OBTENIENDO_COTIZACION:
                return 1;
            case ESTADO_ESPERA_TRASLADO_CLIENTE:
                if(clienteBDIBelieves.getEstadoMeta(TrasladarseALugarGoalBDI.class.getName())==TrasladarseALugarGoalBDI.ESTADO_FINALIZADA){
                    clienteBDIBelieves.setEstadoMeta(CotizarNecesidadGoalBDI.class.getName(),CotizarNecesidadGoalBDI.ESTADO_EJECUCION_COTIZANDO_NECESIDAD);
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
        /*int estado=((ClienteBDIBelieves)blvs).getEstadoMeta(CotizarNecesidadGoalBDI.class.getName());
        switch(estado){
            case ESTADO_FINALIZADA: return true; 
        }*/
        return false;
    }
    
}
