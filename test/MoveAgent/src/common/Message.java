/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author fabianjose
 */
public class Message extends DataBESA {

    private String message;
    
    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }    
}
