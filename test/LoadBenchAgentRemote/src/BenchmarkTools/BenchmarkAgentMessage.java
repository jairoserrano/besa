/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author jairo
 */
public class BenchmarkAgentMessage extends DataBESA {

    private int NumberOfContainers = 0;
    private int NumberOfAgentsPerContainer = 0;

    public BenchmarkAgentMessage(int nc, int na) {
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
