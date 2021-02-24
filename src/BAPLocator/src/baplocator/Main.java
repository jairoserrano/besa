/*
 * @(#)Main.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package baplocator;

import BESA.Extern.WSPublisher;

/**
 * TODO. 
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.4
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new WSPublisher().startService(args[0]);
    }
}
