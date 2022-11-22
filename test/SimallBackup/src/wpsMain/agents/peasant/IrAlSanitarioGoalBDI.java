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
import wpsMain.agents.peasant.Const;
import wpsMain.agents.peasant.EmotionAxis;
import wpsMain.agents.peasant.TrasladarseALugarGoalBDI;
import rational.RationalRole;
import rational.mapping.Believes;

/**
 *
 * @author Daniel
 */
public class IrAlSanitarioGoalBDI extends GoalBDI{
    
    public static final int ESTADO_SIN_INICIAR=0;
    public static final int ESTADO_FINALIZADA=1;
    
    public static final int ESTADO_ESPERA_TRASLADO_CLIENTE=2;
    public static final int ESTADO_EJECUCION_USAR_SANITARIO=3;

    public IrAlSanitarioGoalBDI(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    @Override
    public double evaluateViability(Believes blvs) throws KernellAgentEventExceptionBESA {
        ClienteBDIBelieves clienteBDIBelieves=(ClienteBDIBelieves)blvs;
        int estado=clienteBDIBelieves.getEstadoMeta(IrAlSanitarioGoalBDI.class.getName());
        switch(estado){
            case ESTADO_FINALIZADA:                
            case ESTADO_SIN_INICIAR:
                return 0;                
            case ESTADO_EJECUCION_USAR_SANITARIO:
                return 1;
            case ESTADO_ESPERA_TRASLADO_CLIENTE:
                if(clienteBDIBelieves.getEstadoMeta(TrasladarseALugarGoalBDI.class.getName())==TrasladarseALugarGoalBDI.ESTADO_FINALIZADA){
                    clienteBDIBelieves.setEstadoMeta(IrAlSanitarioGoalBDI.class.getName(),IrAlSanitarioGoalBDI.ESTADO_EJECUCION_USAR_SANITARIO);
                    return 1;
                }
        }
        return 0;
    }

    @Override
    public double detectGoal(Believes blvs) throws KernellAgentEventExceptionBESA {
        EmotionAxis ejeSanitario=((ClienteBDIBelieves)blvs).getComponenteEmocional().getEmotionAxis(Const.Semantica.Sensaciones.NoNecesitaSanitario, Const.Semantica.Sensaciones.NecesitaSanitario);
        float nivelSanitario=ejeSanitario.getCurrentValue();
        if(nivelSanitario==0f){
            if(((ClienteBDIBelieves)blvs).getEstadoMeta(IrAlSanitarioGoalBDI.class.getName())!=IrAlSanitarioGoalBDI.ESTADO_EJECUCION_USAR_SANITARIO&&((ClienteBDIBelieves)blvs).getEstadoMeta(IrAlSanitarioGoalBDI.class.getName())!=IrAlSanitarioGoalBDI.ESTADO_ESPERA_TRASLADO_CLIENTE){
                ((ClienteBDIBelieves)blvs).setEstadoMeta(IrAlSanitarioGoalBDI.class.getName(),IrAlSanitarioGoalBDI.ESTADO_EJECUCION_USAR_SANITARIO);
            }
            return 1;
        }
        return 0;
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
        /*EmotionAxis ejeSanitario=((ClienteBDIBelieves)blvs).getComponenteEmocional().getEmotionAxis(Const.Semantica.Sensaciones.NoNecesitaSanitario, Const.Semantica.Sensaciones.NecesitaSanitario);
        float nivelSanitario=ejeSanitario.getCurrentValue();
        if(nivelSanitario>-1){
            return true;
        }*/
        return false;
    }
    
}
