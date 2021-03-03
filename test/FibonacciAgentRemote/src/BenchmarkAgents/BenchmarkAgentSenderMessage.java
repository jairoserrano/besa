/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkAgents;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentSenderMessage extends DataBESA {

    private int NumberOfContainers = 0;
    private int NumberOfAgentsPerContainer = 0;

    public BenchmarkAgentSenderMessage(int nc, int na) {
        this.NumberOfContainers = nc;
        this.NumberOfAgentsPerContainer = na;
    }

    public int getNumberOfAgentsPerContainer() {
        return NumberOfAgentsPerContainer;
    }

    public void setNumberOfAgentsPerContainer(int NumberOfAgentsPerContainer) {
        this.NumberOfAgentsPerContainer = NumberOfAgentsPerContainer;
    }

    public int getNumberOfContainers() {
        return NumberOfContainers;
    }

    public void setNumberOfContainers(int NumberOfContainers) {
        this.NumberOfContainers = NumberOfContainers;
    }

}
