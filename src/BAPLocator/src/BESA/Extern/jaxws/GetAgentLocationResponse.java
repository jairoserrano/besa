/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BESA.Extern.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlRootElement(name = "getAgentLocationResponse", namespace = "http://Extern.BESA/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAgentLocationResponse", namespace = "http://Extern.BESA/")
public class GetAgentLocationResponse {

    @XmlElement(name = "return", namespace = "")
    private String _return;

    /**
     * Gets the value of the return property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReturn(String value) {
        this._return = value;
    }
}
