/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WorkAgent;

import WorkAgent.*;
import BESA.Kernel.Agent.StateBESA;
import java.io.Serializable;

/**
 *
 * @author jairo
 */
public class WorkAgentState extends StateBESA implements Serializable {
    
    int workCalls;

    public WorkAgentState() {
        super();
    }

    public synchronized void initState() {
        this.workCalls = 0;
    }
    

}
