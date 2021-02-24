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
@XmlType(name = "getBAPsLocation", propOrder = {
    "arg0", "arg1"
})
public class GetBAPsLocation {

    protected String arg0;
    protected int arg1;

    public String getArg0() {
        return arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    public int getArg1() {
        return arg1;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }
}
