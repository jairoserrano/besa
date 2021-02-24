/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentb;

import BESA.Kernel.Agent.StateBESA;

/**
 *
 * @author fabianjose
 */
public class AgentBState extends StateBESA {

    private int cont;

    public AgentBState() {
        cont = 0;
    }

    public void upCont(){
        cont++;
    }
    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }
}
