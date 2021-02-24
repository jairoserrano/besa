/*
 * @(#)IBAPLocator.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern;

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
@WebService(name = "IBAPLocator", targetNamespace = "http://Extern.BESA/")
@XmlSeeAlso({ObjectFactory.class})
public interface IBAPLocator {

    /**
     * This method cleans the collections of the agents registry of the BAPLocator.
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "restore", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.Restore")
    @ResponseWrapper(localName = "restoreResponse", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.RestoreResponse")
    public void restore();

    /**
     * Register of a new BAP into registry of the BAPLocator.
     * 
     * @param arg0 Administrator ID.
     * @param arg1 BAP IP Address.
     * @param arg2 BAP Port.
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "registerBAP", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.RegisterBAP")
    @ResponseWrapper(localName = "registerBAPResponse", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.RegisterBAPResponse")
    public void registerBAP(
            @WebParam(name = "arg0", targetNamespace = "") String arg0,
            @WebParam(name = "arg1", targetNamespace = "") String arg1,
            @WebParam(name = "arg2", targetNamespace = "") int arg2);

    /**
     * Register of a new Agent into registry of the BAPLocator.
     *
     * @param arg0 Administrator ID.
     * @param arg1 BAP IP Address.
     * @param arg2 Agent alias.
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "registerAgent", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.RegisterAgent")
    @ResponseWrapper(localName = "registerAgentResponse", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.RegisterAgentResponse")
    public void registerAgent(
            @WebParam(name = "arg0", targetNamespace = "") String arg0,
            @WebParam(name = "arg1", targetNamespace = "") String arg1,
            @WebParam(name = "arg2", targetNamespace = "") int arg2);

    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "unRegisterAgent", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.UnRegisterAgent")
    @ResponseWrapper(localName = "unRegisterAgentResponse", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.UnRegisterAgentResponse")
    public void unRegisterAgent(
            @WebParam(name = "arg0", targetNamespace = "") String arg0);

    /**
     * Gets the agent location.
     *
     * @param arg0 Agent alias.
     * @return The agent location.
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAgentLocation", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.GetAgentLocation")
    @ResponseWrapper(localName = "getAgentLocationResponse", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.GetAgentLocationResponse")
    public String getAgentLocation(
            @WebParam(name = "arg0", targetNamespace = "") String arg0);

    /**
     * Gets the agent location.
     *
     * @param arg0 Agent alias.
     * @return The agent location.
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMasterLocation", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.GetMasterLocation")
    @ResponseWrapper(localName = "getMasterLocationResponse", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.GetMasterLocationResponse")
    public String getMasterLocation(
            @WebParam(name = "arg0", targetNamespace = "") String arg0);


    /**
     * Gets the location of a BAP.
     * 
     * @param arg0 Host address.
     * @param arg1 BAP port.
     * @return BAP location.
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getBAPsLocation", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.GetBAPsLocation")
    @ResponseWrapper(localName = "getBAPsLocationResponse", targetNamespace = "http://Extern.BESA/", className = "BESA.Extern.GetBAPsLocationResponse")
    public String getBAPsLocation(
            @WebParam(name = "arg0", targetNamespace = "") String arg0,
            @WebParam(name = "arg1", targetNamespace = "") int arg1);
}
