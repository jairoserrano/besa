/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenta;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author SAR
 */
public class SayHelloData extends DataBESA {

    private String msg;

    public SayHelloData(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getStringFromDataBesa() {
        return this.msg;
    }
}
