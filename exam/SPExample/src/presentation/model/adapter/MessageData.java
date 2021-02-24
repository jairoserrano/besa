/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.model.adapter;

import BESA.Kernel.Agent.Event.DataBESA;



/**
 *
 * @author fabianjose
 */
public class MessageData extends DataBESA {

    private String message;

    public MessageData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
