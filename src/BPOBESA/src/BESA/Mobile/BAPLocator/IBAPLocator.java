/*
 * @(#)IBAPLocator.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Mobile.BAPLocator;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This a web service connection interface that interact with BAPLocator server.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
@WebService(name = "IBAPLocator", targetNamespace = "http://BESA.Mobile.BAPLocator/")
@XmlSeeAlso({ObjectFactory.class})
public interface IBAPLocator {

  

    /**
     * Gets the agent location.
     *
     * @param arg0 Agent alias.
     * @return The agent location.
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMasterLocation", targetNamespace = "http://Extern.BESA/", className = "BESA.Mobile.BAPLocator.GetMasterLocation")
    @ResponseWrapper(localName = "getMasterLocationResponse", targetNamespace = "http://Extern.BESA/", className = "BESA.Mobile.BAPLocator.GetMasterLocationResponse")
    public String getMasterLocation(
            @WebParam(name = "arg0", targetNamespace = "") String arg0);


}
