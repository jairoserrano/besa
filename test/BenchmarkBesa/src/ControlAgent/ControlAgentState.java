/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlAgent;

import BESA.Kernel.Agent.StateBESA;
import ServerAgent.WorkerAgent;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jairo
 */
public class ControlAgentState extends StateBESA implements Serializable {
    
    private ArrayList<String> CurrentAgents;
    
    public ControlAgentState() {
        super();
        
        // TODO: INCLUIR LISTADO DE AGENTES
    }


}
