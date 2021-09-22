/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WorkerAgent;

import BESA.Kernel.Agent.StateBESA;
import java.io.Serializable;

/**
 *
 * @author jairo
 */
public class WorkerAgentState extends StateBESA implements Serializable {
    
    private String ClientName;

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String ClientName) {
        this.ClientName = ClientName;
    }

    public WorkerAgentState(String ClientName) {
        super();
        setClientName(ClientName);
    }

}
