/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Extern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author SAR
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBAPsLocationResponse", propOrder = {
    "_return"
})
public class GetBAPsLocationResponse {

    @XmlElement(name = "return")
    protected String _return;

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
