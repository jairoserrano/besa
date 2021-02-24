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
@XmlRootElement(name = "getBAPsLocation", namespace = "http://Extern.BESA/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBAPsLocation", namespace = "http://Extern.BESA/")
public class GetBAPsLocation {

    @XmlElement(name = "arg0", namespace = "")
    private String arg0;
    @XmlElement(name = "arg1", namespace = "")
    private int arg1;

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
