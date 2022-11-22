/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.plantillas.agenteBDI;

import SIMALL.plantillas.agenteBDI.metas.PlantillaBDIGoalConstants;
import BESA.BDI.AgentStructuralModel.Agent.Builder.BDIAgentBuilder;
import BESA.BDI.AgentStructuralModel.BDIMachineParams;
import BESA.BDI.AgentStructuralModel.GoalBDI;
import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import SIMALL.modelo.cc.cliente.agenteBDI.guardas.IniciarLogroDeMetasGuard;
import SIMALL.plantillas.agente.guardas.PlantillaGuard;
import java.util.ArrayList;
import java.util.List;
import SIMALL.plantillas.agenteBDI.metas.supervivencia.PlantillaSupervivenciaGoalBDI;
import SIMALL.plantillas.agenteBDI.tareas.PlantillaTask;
import rational.RationalRole;
import rational.mapping.Plan;

/**
 *
 * @author Daniel
 */
public class PlantillaBDIAgentBuilder extends BDIAgentBuilder{
    
    private final double defaultThreshold;
    
    public PlantillaBDIAgentBuilder(String alias, double password, double defaultThreshold) {
        super(alias, password);
        this.defaultThreshold=defaultThreshold;
    }

    @Override
    public void buildDBIMachine() {
        BDIMachineParams machineParams = new BDIMachineParams();
        machineParams.setDutyThreshold(PlantillaBDIGoalConstants.DUTYTHRESHOLD);
        machineParams.setNeedThreshold(PlantillaBDIGoalConstants.NEEDTHRESHOLD);
        machineParams.setOportunityThreshold(PlantillaBDIGoalConstants.OPORTUNITYTHRESHOLD);
        machineParams.setRequirementThreshold(PlantillaBDIGoalConstants.REQUIREMENTTHRESHOLD);
        machineParams.setSurvivalThreshold(PlantillaBDIGoalConstants.SURVIVALTHRESHOLD);
        this.setParamsMachine(machineParams);
    }

    @Override
    public void buildAgentBelieves() {
        PlantillaBDIBelieves clienteBDIBelieves=new PlantillaBDIBelieves(); 
        this.setBelieves(clienteBDIBelieves);
    }

    @Override
    public void buildAgent() throws KernellAgentExceptionBESA, ExceptionBESA {
        PlantillaBDIAgent plantillaBDIAgent=new PlantillaBDIAgent(this.getAlias(),new PlantillaBDIBelieves(),plantillaCrearMetas(), defaultThreshold, setupStruct(new StructBESA()));
        setAgentBDI(plantillaBDIAgent);
    }
    
    private static StructBESA setupStruct(StructBESA structBESA) throws ExceptionBESA {
        structBESA.addBehavior("PlantillaGuard");
        structBESA.bindGuard("PlantillaGuard", PlantillaGuard.class);
        return structBESA;
    }
    
    private List<GoalBDI> plantillaCrearMetas(){
        List<GoalBDI> plantillaListaMetas=new ArrayList();

        PlantillaTask plantillaTask=new PlantillaTask();
        Plan plantillaPlan=new Plan();
        plantillaPlan.addTask(plantillaTask);
        RationalRole plantillaRole = new RationalRole("plantillaRole", plantillaPlan);
        PlantillaSupervivenciaGoalBDI plantillaGoalBDI=new PlantillaSupervivenciaGoalBDI(0, plantillaRole, "plantillaGoalBDI", GoalBDITypes.SURVIVAL);
        plantillaListaMetas.add(plantillaGoalBDI);

        return plantillaListaMetas;
    }
}
