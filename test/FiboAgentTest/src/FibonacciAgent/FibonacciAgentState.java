/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FibonacciAgent;

import BESA.Kernel.Agent.StateBESA;
import java.io.Serializable;

/**
 *
 * @author jairo
 */
public class FibonacciAgentState extends StateBESA implements Serializable {
    
    int fiboCalls;

    public FibonacciAgentState() {
        super();
    }

    public synchronized void initState() {
        this.fiboCalls = 0;
    }
    
    public synchronized int getFibocalls() {
        return this.fiboCalls;
    }

    public synchronized void incFibocalls(){
        this.fiboCalls = this.fiboCalls + 1;
    }

}
