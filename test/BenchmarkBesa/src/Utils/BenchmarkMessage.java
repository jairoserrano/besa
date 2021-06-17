/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author jairo
 */
public class BenchmarkMessage extends DataBESA {

    private String content;
    private String agentRef;

    public BenchmarkMessage(String content, String agentDest) {
        this.content = content;
        this.agentRef = agentDest;
    }

    public String getAgentRef() {
        return agentRef;
    }

    public void setAgentRef(String agentDest) {
        this.agentRef = agentDest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
