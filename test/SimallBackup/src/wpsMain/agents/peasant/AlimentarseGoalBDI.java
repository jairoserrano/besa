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
import wpsMain.agents.peasant.Necesidad;
import wpsMain.agents.peasant.Const;
import wpsMain.agents.peasant.EmotionAxis;
import wpsMain.agents.peasant.SalirGoalBDI;
import rational.RationalRole;
import rational.mapping.Believes;

/**
 *
 * @author Daniel
 */
public class AlimentarseGoalBDI extends GoalBDI{
    
    public static final int ESTADO_SIN_INICIAR=0;
    public static final int ESTADO_FINALIZADA=1;
    
    public static final int ESTADO_EJECUCION_PRIORIZA_ALIMENTACION=2;
    public static final int ESTADO_ESPERA_ALIMENTARSE=3;
    

    public AlimentarseGoalBDI(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    @Override
    public double evaluateViability(Believes blvs) throws KernellAgentEventExceptionBESA {
        ClienteBDIBelieves clienteBDIBelieves=(ClienteBDIBelieves)blvs;
        int estado=clienteBDIBelieves.getEstadoMeta(AlimentarseGoalBDI.class.getName());
        switch(estado){
            case ESTADO_EJECUCION_PRIORIZA_ALIMENTACION:
                return 1;
        }
        return 0;
    }

    @Override
    public double detectGoal(Believes blvs) throws KernellAgentEventExceptionBESA {
        EmotionAxis ejeHambre=((ClienteBDIBelieves)blvs).getComponenteEmocional().getEmotionAxis(Const.Semantica.Sensaciones.SinHambre, Const.Semantica.Sensaciones.Hambre);
        float nivelHambre=ejeHambre.getCurrentValue();
        if(nivelHambre==0f){
            if(((ClienteBDIBelieves)blvs).getEstadoMeta(AlimentarseGoalBDI.class.getName())!=AlimentarseGoalBDI.ESTADO_EJECUCION_PRIORIZA_ALIMENTACION&&((ClienteBDIBelieves)blvs).getEstadoMeta(AlimentarseGoalBDI.class.getName())!=AlimentarseGoalBDI.ESTADO_ESPERA_ALIMENTARSE){
                ((ClienteBDIBelieves)blvs).setEstadoMeta(AlimentarseGoalBDI.class.getName(),AlimentarseGoalBDI.ESTADO_EJECUCION_PRIORIZA_ALIMENTACION);
            }
            if(((ClienteBDIBelieves)blvs).getEstadoMeta(AlimentarseGoalBDI.class.getName())==AlimentarseGoalBDI.ESTADO_ESPERA_ALIMENTARSE){
                Necesidad necesidadSinSatisfacerMaximaPrioridad=((ClienteBDIBelieves)blvs).getNecesidadSinSatisfacerMaximaPrioridad();
                if(((ClienteBDIBelieves)blvs).getNecesidadAlimentacionPriorizada()!=((ClienteBDIBelieves)blvs).getNecesidadEnCurso()){
                    if(((ClienteBDIBelieves)blvs).getNecesidadAlimentacionPriorizada()!=necesidadSinSatisfacerMaximaPrioridad){
                        ((ClienteBDIBelieves)blvs).setEstadoMeta(AlimentarseGoalBDI.class.getName(),AlimentarseGoalBDI.ESTADO_EJECUCION_PRIORIZA_ALIMENTACION);
                    }
                }
            }
            if(((ClienteBDIBelieves)blvs).getEstadoMeta(SalirGoalBDI.class.getName())!=SalirGoalBDI.ESTADO_FINALIZADA&&((ClienteBDIBelieves)blvs).getEstadoMeta(SalirGoalBDI.class.getName())!=SalirGoalBDI.ESTADO_SIN_INICIAR){
                return 0;
            }
            return 1;
        }
        else{
            if(((ClienteBDIBelieves)blvs).getEstadoMeta(AlimentarseGoalBDI.class.getName())!=AlimentarseGoalBDI.ESTADO_FINALIZADA&&((ClienteBDIBelieves)blvs).getEstadoMeta(AlimentarseGoalBDI.class.getName())!=AlimentarseGoalBDI.ESTADO_SIN_INICIAR){
                ((ClienteBDIBelieves)blvs).setEstadoMeta(AlimentarseGoalBDI.class.getName(),AlimentarseGoalBDI.ESTADO_FINALIZADA);
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
        return 0.8;
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
