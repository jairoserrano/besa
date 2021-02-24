/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Extern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author SAR
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registerBAP", propOrder = {
    "arg0", "arg1", "arg2"
})
public class RegisterBAP {

    protected String arg0;
    protected String arg1;
    protected int arg2;

    public String getArg0() {
        return arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }
}
