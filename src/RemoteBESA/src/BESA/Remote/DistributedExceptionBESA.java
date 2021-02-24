/*
 * @(#)DistributedExceptionBESA.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Remote;

import BESA.ExceptionBESA;

/**
 * This class represents the BESA adapter exception set.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class DistributedExceptionBESA extends ExceptionBESA {
//TODO Cambiar nombre.
    /**
     * Creates a new instance.
     *
     * @param msg Exception message.
     */
    public DistributedExceptionBESA(String msg) {
        super(msg);
    }
}
