/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Remote.Balancer;

import BESA.Kernel.Agent.StateBESA;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author bitas
 */
public class Ag_Balancer_State extends StateBESA implements Serializable {

    //Tiempo que va a tardar la guarda periodica para ejecutarse
    private long periodicTime;
    
    //Maxima carga que deberia soportar el contenedor
    private long max;
    
    //Carga actual del contenedor
    private long load;
    
    //Carga de cada uno de los agentes del contenedor
    private Map<String, Long> agentsLoads;
    
    //Comunicaciones de los agentes
    private Map<String, Long> agentsComunications;
    
    //Hilos asociados a cada uno de los agentes
    private Map<String, ArrayList<Long>> threadsIDs;
    
    //Lista donde estaran los contenedores prometedores
    private List<String> contenedores;
    

    
    public Ag_Balancer_State(long max, long time) {
        super();
        this.max = max;
        this.periodicTime = time;

    }

    public synchronized void initState() {
        
        this.load = 0;
        this.agentsLoads = new HashMap<>();
        this.agentsComunications = new HashMap<>();
        this.threadsIDs = new HashMap<>();
        this.contenedores = new ArrayList<>();
    }

    public long getLoad() {
        return load;
    }

    public void setLoad(long load) {
        this.load = load;
    }

    public Map<String, Long> getAgentsLoads() {
        return agentsLoads;
    }

    public void setAgentsLoads(Map<String, Long> agentsLoads) {
        this.agentsLoads = agentsLoads;
    }

    public Map<String, Long> getAgentsComunications() {
        return agentsComunications;
    }

    public void setAgentsComunications(Map<String, Long> agentsComunications) {
        this.agentsComunications = agentsComunications;
    }

    public Map<String, ArrayList<Long>> getThreadsIDs() {
        return threadsIDs;
    }

    public void setThreadsIDs(Map<String, ArrayList<Long>> threadsIDs) {
        this.threadsIDs = threadsIDs;
    }

    public List<String> getContenedores() {
        return contenedores;
    }

    public void setContenedores(List<String> contenedores) {
        this.contenedores = contenedores;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getPeriodicTime() {
        return periodicTime;
    }

    public void setPeriodicTime(long periodicTime) {
        this.periodicTime = periodicTime;
    }
    
    
    
}