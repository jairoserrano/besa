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

    private int agents;

    public BenchmarkAgentMessage(int agents) {
        this.agents = agents;
    }

    public int getNumberOfAgents() {
        return agents;
    }

}
