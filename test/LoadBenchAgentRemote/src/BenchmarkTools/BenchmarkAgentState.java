/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

import BESA.Kernel.Agent.StateBESA;
import ContainersLauncher.BenchmarkConfig;
import java.io.Serializable;

/**
 *
 * @author jairo
 */
public final class BenchmarkAgentState extends StateBESA implements Serializable {

    BenchmarkConfig config;
    int counter;

    public BenchmarkAgentState() {
        super();
        config = BenchmarkConfig.getConfig();
        this.setCounter(
                config.getNumberOfAgentsPerContainer()
                * config.getNumberOfContainers()
        );
    }

    public synchronized void initState() {

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
