/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WorkAgent;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author jairo
 */
public class WorkAgentMessage extends DataBESA {

    private final String KindOfWork;

    public WorkAgentMessage(String KindOfWork) {
        this.KindOfWork = KindOfWork;
    }

    public String getKindOfWork() {
        return this.KindOfWork;
    }

}
