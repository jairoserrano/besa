/*
 * @(#)IBAPLocator.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * TODO. 
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.4
 */
@WebService
public interface IBAPLocator {

    @WebMethod
    public void restore();

    @WebMethod
    public void registerBAP(String admId, String bapAdd, int bapPort);

    @WebMethod
    public void registerAgent(String agAlias, String bapAdd, int bapPort);

    @WebMethod
    public void unRegisterAgent(String agAlias);

    @WebMethod
    public String getAgentLocation(String agAlias);

    @WebMethod
    public String getMasterLocation(String agAlias);

    @WebMethod
    public String getBAPsLocation(String bapAdd, int bapPort);
}
