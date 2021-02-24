/*
 * @(#)GUIObserver.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package Presentation;

import Abstraction.Ball;
import Abstraction.DEF;

/**
 * This class represents the start point of the application.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public interface GUIObserver {

    public void distributedChangueModel(Ball ball, double[] playerPV);
    public void localChangueModel(Ball ball, DEF player, double[] playerPV);
}
