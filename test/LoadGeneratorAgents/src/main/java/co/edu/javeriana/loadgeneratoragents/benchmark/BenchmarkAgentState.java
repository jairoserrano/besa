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
public class BenchmarkAgentState extends StateBESA implements Serializable {

    BenchmarkConfig config = new BenchmarkConfig();

    public BenchmarkAgentState() {
        super();
    }

    public synchronized void initState() {

    }

}
