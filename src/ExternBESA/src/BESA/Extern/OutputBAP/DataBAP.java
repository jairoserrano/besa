/*
 * @(#)DataBAP.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern.OutputBAP;

import BESA.Extern.ExternExceptionBESA;
import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents 
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class DataBAP extends DataBESA {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    /**
     *
     */
    public String str;
    //Evento enviado al agente externo
    /**
     *
     */
    private EventBESA ev;
    //Alias del agente externo de destino
    /**
     *
     */
    private String agAlias;
    private String bPOLocation;
    

    /**
     *
     * @param str
     * @param ev
     * @param agAlias
     */
    public DataBAP(String str, EventBESA ev, String agAlias, String bPOLocation) {
        this.str = str;
        this.ev = ev;
        this.agAlias = agAlias;
        this.bPOLocation = bPOLocation;
    }

    public DataBAP(String str, String agAlias) {
        this.str = str;
        this.agAlias = agAlias;
    }

    /**
     *
     * @param str
     */
    @Override
    public void getDataBesaFromString(String str) {
        this.str = str;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getStringFromDataBesa() {
        try {
            throw new ExternExceptionBESA("The default non functional method has been invoked - This method"
                    + "must be overwrite for proper operation.");
        } catch (ExternExceptionBESA ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     *
     * @return
     */
    public String getStr() {
        return this.str;
    }

    /**
     *
     * @return
     */
    public EventBESA getEv() {
        return this.ev;
    }

    /**
     *
     * @return
     */
    public String getAgAlias() {
        return this.agAlias;
    }

    public String getbPOLocation() {
        return bPOLocation;
    }

    public void setbPOLocation(String bPOLocation) {
        this.bPOLocation = bPOLocation;
    }    
}
