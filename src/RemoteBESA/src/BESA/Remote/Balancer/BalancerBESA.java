/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Remote.Balancer;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.BehaviorBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Util.PeriodicDataBESA;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bitas
 */
public class BalancerBESA {

    /**
     * Agente balanceador
     */
    private Ag_Balancer balancer;
    /**
     * Tiempo de la guarda periodica
     */
    private long periodicTime;
    /**
     * Carga maxima del contenedor
     */    
    private long max_load;          

    public BalancerBESA() {

        StructBESA balanceador = new StructBESA();

        this.periodicTime = 10000;
        this.max_load = 100000000000L;

        try {
            balanceador.addBehavior("ComportamientoBalanceador");
            balanceador.addBehavior("ComportamientoSolicitudes");

            balanceador.bindGuard("ComportamientoBalanceador", Balance_Guard.class);
            balanceador.bindGuard("ComportamientoBalanceador", Update_Guard.class);
            balanceador.bindGuard("ComportamientoSolicitudes", Answers_Guard.class);
            balanceador.bindGuard("ComportamientoSolicitudes", Requests_Guard.class);

        } catch (ExceptionBESA ex) {
            Logger.getLogger(BalancerBESA.class.getName()).log(Level.SEVERE, null, ex);
        }

        double agentPasswd = 2500;
        Ag_Balancer_State estado = new Ag_Balancer_State(max_load, periodicTime);

        String alias = "balanceador" + AdmBESA.getInstance().getAdmHandler().getAlias();
        try {
            balancer = new Ag_Balancer(alias, estado, balanceador, agentPasswd);
        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(BalancerBESA.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public BalancerBESA(long periodicTime, long max_load) {

        StructBESA balanceador = new StructBESA();

        this.periodicTime = periodicTime;
        this.max_load = max_load;

        try {
            balanceador.addBehavior("ComportamientoBalanceador");
            balanceador.addBehavior("ComportamientoSolicitudes");

            balanceador.bindGuard("ComportamientoBalanceador", Balance_Guard.class);
            balanceador.bindGuard("ComportamientoBalanceador", Update_Guard.class);
            balanceador.bindGuard("ComportamientoSolicitudes", Answers_Guard.class);
            balanceador.bindGuard("ComportamientoSolicitudes", Requests_Guard.class);

        } catch (ExceptionBESA ex) {
            Logger.getLogger(BalancerBESA.class.getName()).log(Level.SEVERE, null, ex);
        }

        double agentPasswd2 = 0.91;
        Ag_Balancer_State estado = new Ag_Balancer_State(max_load, periodicTime);

        try {
            String alias = "balanceador" + AdmBESA.getInstance().getAdmHandler().getAlias();
            balancer = new Ag_Balancer(alias, estado, balanceador, agentPasswd2);

        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(BalancerBESA.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Este metodo da inicio al balanceador de carga.
     *
     *
     */
    public void initBalancer() {

        balancer.start();
        this.funcInit();

    }

    /**
     * Este metodo obtiene el nombre de los agentes en el contenedor y los
     * asocia con sus respectivos hilos.
     *
     *
     */
    private void funcInit() {

        AdmBESA localAdmin = AdmBESA.getInstance();
        AgHandlerBESA agent;
        Ag_Balancer_State estado = (Ag_Balancer_State) this.balancer.getState();
        Enumeration agentsIds = localAdmin.getIdList();
        ArrayList<Long> ids = null;
        HashMap<String, ArrayList<Long>> mapIds = new HashMap<String, ArrayList<Long>>();

        //Se revisan todos los agentes para saber que hilos tienen asociados
        while (agentsIds.hasMoreElements()) {
            try {
                agent = localAdmin.getHandlerByAid((String) agentsIds.nextElement());
                //Si el agente no es el balanceador y pertenece al contenedor se incluye 
                if (!agent.getAgId().equalsIgnoreCase(this.balancer.getAid())
                        && (localAdmin.getHandlerByAlias(agent.getAlias()).getAg() != null)) {
                    System.out.println(agent.getAlias());
                    ids = new ArrayList<>();
                    for (BehaviorBESA behavior : agent.getAg().getBehaviors()) {
                        //Se agregan los hilos de sus comportamientos a la lista
                        ids.add(behavior.getThread().getId());
                    }
                    //Se relacionan los id's de los hilos con el alias del agente
                    mapIds.put(agent.getAlias(), ids);
                }
            } catch (ExceptionBESA ex) {
                Logger.getLogger(BalancerBESA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        estado.setThreadsIDs(mapIds);
        //Crea el mensaje de inicio para la periodic guard.
        PeriodicDataBESA periodicData = new PeriodicDataBESA(this.periodicTime,
                PeriodicGuardBESA.START_PERIODIC_CALL);
        //Crea el evento event y se envia a la Guarda_Balanceo.
        EventBESA startPeriodicEv = new EventBESA(Balance_Guard.class.getName(),
                periodicData);
        try {
            agent = localAdmin.getHandlerByAlias(balancer.getAlias());
            agent.sendEvent(startPeriodicEv);
        } catch (ExceptionBESA e) {
            e.printStackTrace();
        }
    }

}
