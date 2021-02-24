/*
 * @(#)DataOdo.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package OdoAgent.Data;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 * This class represents menssage type. Defines the data type exchanged guards.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class DataOdo extends DataBESA {

    /**
     * Message.
     */
    private String message;
    /**
     * Value.
     */
    private String valueString;

    /** 
     * Creates a new instance of DataOdo.
     * @param mensaje
     */
    public DataOdo(String mensaje) {
        this.message = mensaje;
    }

    /**
     * Gets the menssage.
     *
     * @return Menssage.
     */
    public String getMenssage() {
        return message;
    }

    /**
     * Sets the menssage.
     *
     * @param str Menssage.
     */
    public void strToDataBesa(String str) {
        message = str;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }
}
