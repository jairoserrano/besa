/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;

import BESA.BDI.AgentStructuralModel.Agent.AgentBDI;
import BESA.BDI.AgentStructuralModel.GoalBDI;
import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import SIMALL.modelo.cc.infraestructura.MapaEstructural;
import SIMALL.modelo.log.LogAuditoria;
import java.util.ArrayList;
import java.util.List;
import rational.RationalRole;
import rational.RationalState;
import rational.data.InfoData;
import rational.guards.InformationFlowGuard;
import rational.mapping.Believes;
import rational.mapping.Plan;

/**
 *
 * @author Daniel
 */
public class ClienteBDIAgent extends AgentBDI{
    
    public ClienteBDIAgent(String alias) throws KernelAgentExceptionBESA, ExceptionBESA {
        super(alias, crearBelieves(alias), crearMetas(), 0.6, crearStruct(new StructBESA()));
    }

    public void impulsoBDI() {
        try{
            AgHandlerBESA agHandler = AdmBESA.getInstance().getHandlerByAlias(this.getAlias());
            EventBESA eventBesa = new EventBESA(InformationFlowGuard.class.getName(),null);
            agHandler.sendEvent(eventBesa);
        }catch(Exception e){
            LogAuditoria.getInstance().escribirError(this.getClass().getName(), this.getAlias(), e);
        }
    }
    
    public Believes getBelieves(){
        return ((RationalState)this.getState()).getBelieves();
    }

    @Override
    public void setupAgentBDI() {
        
    }

    @Override
    public void shutdownAgentBDI() {
    }
    
    public static void sendEvent(String alias,String guardName,InfoData data){
        try {
            EventBESA event = new EventBESA(guardName, data);
            AdmBESA.getInstance().getHandlerByAlias(alias).sendEvent(event);
        } catch (Exception ex) {
            ReportBESA.error("Error con el handler del agente " + alias);
        }
    }
    
    private static StructBESA crearStruct(StructBESA structBESA) throws ExceptionBESA {
        structBESA.addBehavior("IniciarLogroDeMetasGuard");
        structBESA.bindGuard("IniciarLogroDeMetasGuard", IniciarLogroDeMetasGuard.class);
        structBESA.addBehavior("RecibirMensajesPromocionalesGuard");
        structBESA.bindGuard("RecibirMensajesPromocionalesGuard", RecibirMensajesPromocionalesGuard.class);
        return structBESA;
    }
    
    private static List<GoalBDI> crearMetas(){
        List<GoalBDI> listaMetasCliente=new ArrayList();

        SatisfacerNecesidadesTask satisfacerNecesidadesTask=new SatisfacerNecesidadesTask();
        Plan satisfacerNecesidadesPlan=new Plan();
        satisfacerNecesidadesPlan.addTask(satisfacerNecesidadesTask);
        RationalRole satisfacerNecesidadesRole = new RationalRole("satisfacerNecesidadesRole", satisfacerNecesidadesPlan);
        SatisfacerNecesidadesGoalBDI satisfacerNecesidadesGoalBDI=new SatisfacerNecesidadesGoalBDI(0, satisfacerNecesidadesRole, "SatisfacerNecesidadesGoalBDI", GoalBDITypes.DUTY);
        listaMetasCliente.add(satisfacerNecesidadesGoalBDI);
        
        ComprarTask comprarTask=new ComprarTask();
        Plan comprarPlan=new Plan();
        comprarPlan.addTask(comprarTask);
        RationalRole comprarRole = new RationalRole("comprarRole", comprarPlan);
        ComprarGoalBDI comprarGoalBDI=new ComprarGoalBDI(1, comprarRole, "ComprarGoalBDI", GoalBDITypes.OPORTUNITY);
        listaMetasCliente.add(comprarGoalBDI);
        
        TrasladarseALugarTask trasladarseALugarTask=new TrasladarseALugarTask();
        Plan trasladarseALugarPlan=new Plan();
        trasladarseALugarPlan.addTask(trasladarseALugarTask);
        RationalRole trasladarseALugarRole = new RationalRole("trasladarseALugarRole", trasladarseALugarPlan);
        TrasladarseALugarGoalBDI trasladarseALugarGoalBDI=new TrasladarseALugarGoalBDI(2, trasladarseALugarRole, "TrasladarseALugarGoalBDI", GoalBDITypes.REQUIREMENT);
        listaMetasCliente.add(trasladarseALugarGoalBDI);
                
        CotizarNecesidadTask cotizarNecesidadTask=new CotizarNecesidadTask();
        Plan cotizarNecesidadPlan=new Plan();
        cotizarNecesidadPlan.addTask(cotizarNecesidadTask);
        RationalRole cotizarNecesidadRole = new RationalRole("cotizarNecesidadRole", cotizarNecesidadPlan);
        CotizarNecesidadGoalBDI cotizarNecesidadGoalBDI=new CotizarNecesidadGoalBDI(3, cotizarNecesidadRole, "CotizarNecesidadGoalBDI", GoalBDITypes.REQUIREMENT);
        listaMetasCliente.add(cotizarNecesidadGoalBDI);
        
        IrAlSanitarioTask irAlSanitarioTask=new IrAlSanitarioTask();
        Plan irAlSanitarioPlan=new Plan();
        irAlSanitarioPlan.addTask(irAlSanitarioTask);
        RationalRole irAlSanitarioRole = new RationalRole("irAlSanitarioRole", irAlSanitarioPlan);
        IrAlSanitarioGoalBDI irAlSanitarioGoalBDI=new IrAlSanitarioGoalBDI(4, irAlSanitarioRole, "IrAlSanitarioGoalBDI", GoalBDITypes.SURVIVAL);
        listaMetasCliente.add(irAlSanitarioGoalBDI);

        AlimentarseTask alimentarseTask=new AlimentarseTask();
        Plan alimentarsePlan=new Plan();
        alimentarsePlan.addTask(alimentarseTask);
        RationalRole alimentarseRole = new RationalRole("alimentarseRole", alimentarsePlan);
        AlimentarseGoalBDI alimentarseGoalBDI=new AlimentarseGoalBDI(5, alimentarseRole, "AlimentarseGoalBDI", GoalBDITypes.SURVIVAL);
        listaMetasCliente.add(alimentarseGoalBDI);
        
        SalirTask salirTask=new SalirTask();
        Plan salirPlan=new Plan();
        salirPlan.addTask(salirTask);
        RationalRole salirRole = new RationalRole("salirRole", salirPlan);
        SalirGoalBDI salirGoalBDI=new SalirGoalBDI(6, salirRole, "SalirGoalBDI", GoalBDITypes.OPORTUNITY);
        listaMetasCliente.add(salirGoalBDI);/*
        
        
        DeambularTask deambularTask=new DeambularTask();
        Plan deambularPlan=new Plan();
        deambularPlan.addTask(deambularTask);
        RationalRole deambularRole = new RationalRole("deambularRole", deambularPlan);
        DeambularGoalBDI deambularGoalBDI=new DeambularGoalBDI(4, deambularRole, "DeambularGoalBDI", GoalBDITypes.NEED);
        listaMetasCliente.add(deambularGoalBDI);
        
        RetirarDineroTask retirarDineroTask=new RetirarDineroTask();
        Plan retirarDineroPlan=new Plan();
        retirarDineroPlan.addTask(retirarDineroTask);
        RationalRole retirarDineroRole = new RationalRole("retirarDineroRole", retirarDineroPlan);
        RetirarDineroGoalBDI retirarDineroGoalBDI=new RetirarDineroGoalBDI(5, retirarDineroRole, "RetirarDineroGoalBDI", GoalBDITypes.REQUIREMENT);
        listaMetasCliente.add(retirarDineroGoalBDI);
        
        SolicitarInformacionTask solicitarInformacionTask=new SolicitarInformacionTask();
        Plan solicitarInformacionPlan=new Plan();
        solicitarInformacionPlan.addTask(solicitarInformacionTask);
        RationalRole solicitarInformacionRole = new RationalRole("solicitarInformacionRole", solicitarInformacionPlan);
        SolicitarInformacionGoalBDI solicitarInformacionGoalBDI=new SolicitarInformacionGoalBDI(6, solicitarInformacionRole, "SolicitarInformacionGoalBDI", GoalBDITypes.REQUIREMENT);
        listaMetasCliente.add(solicitarInformacionGoalBDI);
        
        
        */
        return listaMetasCliente;
    }
    
    private static ClienteBDIBelieves crearBelieves(String alias){
        DatosComprador datosCompradorAleatorios=MapaEstructural.getInstance().getDemografia().getMercadoObjetivo().generarDatosCompradorAleatorios(alias);
        DatosDemograficos datosDemograficosAleatorios=MapaEstructural.getInstance().getDemografia().getMercadoObjetivo().generarDatosDemogaficosAleatorios(alias);
        DatosEconomicos datosEconomicosAleatorios=datosDemograficosAleatorios.generarDatosEconomicosAleatorios(datosDemograficosAleatorios);
        ArrayList<Interes> interesesAleatorios=MapaEstructural.getInstance().getArbolCategorias().getInteresesAsequiblesAleatorios(datosDemograficosAleatorios, datosEconomicosAleatorios);
        return new ClienteBDIBelieves(alias, interesesAleatorios, datosCompradorAleatorios, datosDemograficosAleatorios, datosEconomicosAleatorios);        
    }
}
