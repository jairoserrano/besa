/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Extern.Data;

import BESA.Kernel.Agent.Event.DataBESA;
import java.io.Serializable;

/**
 *
 * @author kss
 */
public class MessageBAP implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = "MessageBAP".hashCode();
    private String command;
    private DataBESA data;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public DataBESA getData() {
        return data;
    }

    public void setData(DataBESA data) {
        this.data = data;
    }
}
