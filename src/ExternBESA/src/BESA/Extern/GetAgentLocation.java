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
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAgentLocation", propOrder = {
    "arg0"
})
public class GetAgentLocation {

    protected String arg0;

    public String getArg0() {
        return arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }
}
