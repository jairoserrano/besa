/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

import BESA.Kernel.Agent.StateBESA;
import BESA.Log.ReportBESA;
import ContainersLauncher.BenchmarkConfig;
import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jairo
 */
public final class BenchmarkAgentState extends StateBESA implements Serializable {

    BenchmarkConfig config;
    int counter;
    private final ArrayBlockingQueue<String> agentsReady;
    private final ArrayBlockingQueue<String> TaskList;

    public BenchmarkAgentState() {
        super();
        config = BenchmarkConfig.getConfig();
        this.setCounter(config.getNumberOfTasks());
        agentsReady = new ArrayBlockingQueue<>(config.getNumberOfAgents());
        TaskList = new ArrayBlockingQueue<>(config.getNumberOfTasks());
    }

    public synchronized void initState() {

    }

    public synchronized int Tasks() {
        return TaskList.size();
    }

    public synchronized String getTask() {

        String Task = "";
        try {
            Task = TaskList.take();
        } catch (InterruptedException ex) {
            Logger.getLogger(BenchmarkAgentState.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Task;

    }

    public synchronized void addTask(String Task) {
        try {
            TaskList.put(Task);
        } catch (InterruptedException ex) {
            Logger.getLogger(BenchmarkAgentState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized String getAgentReady() {

        String AgentReady = "";
        try {
            AgentReady = agentsReady.take();
        } catch (InterruptedException ex) {
            Logger.getLogger(BenchmarkAgentState.class.getName()).log(Level.SEVERE, null, ex);
        }
        //ReportBESA.debug("Agente " + AgentReady);
        return AgentReady;

    }

    public synchronized void setAgentsReady(String newagent) {
        ReportBESA.debug("Queriendo registrar agente " + newagent);
        if (!agentsReady.contains(newagent)) {
            try {
                ReportBESA.debug("Registrado agente " + newagent);
                agentsReady.put(newagent);
            } catch (InterruptedException ex) {
                Logger.getLogger(BenchmarkAgentState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            ReportBESA.debug("No se registró agente " + newagent);
        }
            
    }

    /**
     * Initializes counter state.
     *
     * @param n number of total agents
     */
    public synchronized void setCounter(int n) {
        counter = n;
    }

    /**
     * Initializes counter state.
     *
     * @return int Counter
     */
    public synchronized int getCounter() {
        return counter;
    }

    /**
     * Increments the counter.
     */
    public synchronized void incrementCounter() {
        this.counter++;
    }

    /**
     * Decrements the counter.
     */
    public synchronized void decrementCounter() {
        this.counter--;
    }

}
