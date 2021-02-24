/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Extern.jaxws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author SAR
 */
@XmlRootElement(name = "unRegisterAgent", namespace = "http://Extern.BESA/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unRegisterAgent", namespace = "http://Extern.BESA/")
public class UnRegisterAgent {

    @XmlElement(name = "arg0", namespace = "")
    private String arg0;

    public String getArg0() {
        return arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }
}
