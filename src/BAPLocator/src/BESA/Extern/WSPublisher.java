/*
 * @(#)IBAPLocator.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern;

import javax.xml.ws.Endpoint;

/**
 * TODO. 
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.4
 */
public class WSPublisher {

    /**
     * 
     */
    public void startService(String ip) {
        Endpoint.publish("http://" + ip + "/besa/baplocator", new BAPLocator());
    }
}
