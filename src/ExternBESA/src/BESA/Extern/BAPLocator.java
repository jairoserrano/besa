/*
 * @(#)BAPLocator.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern;

import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class represents the access point of the Extern model. This class
 * sets the communication with the server of the web service.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
@WebServiceClient(name = "BAPLocator", targetNamespace = "http://Extern.BESA/")//, wsdlLocation = "http://localhost:8080/besa/baplocator?wsdl")
public class BAPLocator extends Service {

    /**
     * Greeting implementation service WSDL location.
     */
    private final static URL GREETINGIMPLSERVICE_WSDL_LOCATION;
    private final static String IP;
    private final static String PORT;

    /**
     * Static block for gets the web service URL.
     */
    static {
        URL url = null;
        IP = AdmBESA.getInstance().getConfigBESA().getBaplocatoradd();
        PORT = "" + AdmBESA.getInstance().getConfigBESA().getBloport();
        try {
            URL baseUrl;
            baseUrl = BESA.Extern.BAPLocator.class.getResource(".");
            url = new URL(baseUrl, "http://" + IP + ":" + PORT + "/besa/baplocator?wsdl");
        } catch (MalformedURLException e) {
            ReportBESA.error("Failed to create URL for the wsdl Location: 'http://" + IP + ":" + PORT + "/besa/baplocator?wsdl', retrying as a local file");
        }
        GREETINGIMPLSERVICE_WSDL_LOCATION = url;
    }

    /**
     * Creates a new instance.
     *
     * @param wsdlLocation wsdl Location
     * @param serviceName Service name.
     */
    public BAPLocator(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * Creates a new instance with default URL.
     */
    public BAPLocator() {
        super(GREETINGIMPLSERVICE_WSDL_LOCATION, new QName("http://Extern.BESA/", "BAPLocatorService"));
    }

    /**
     * Gets the proxy to service.
     * 
     * @return Proxy to service.
     */
    @WebEndpoint(name = "BAPLocatorPort")
    public IBAPLocator getBAPLocatorPort() {
        return super.getPort(new QName("http://Extern.BESA/", "BAPLocatorPort"), IBAPLocator.class);
    }

    /**
     * Gets the proxy to service through a set features.
     *
     * @param features A list of WebServiceFeature to configure on the proxy.
     * Supported features not in the features parameter will have their
     * default values.
     *
     * @return Proxy to service.
     */
    @WebEndpoint(name = "BAPLocatorPort")
    public IBAPLocator getBAPLocatorPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://Extern.BESA/", "BAPLocatorPort"), IBAPLocator.class, features);
    }
}
