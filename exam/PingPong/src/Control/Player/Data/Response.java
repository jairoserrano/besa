/*
 * @(#)Response.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Control.Player.Data;

import Abstraction.Ball;
import BESA.Kernel.Agent.Event.DataBESA;

/**
 * This class represents a player response.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class Response extends DataBESA {

    /**
     * Represents the ball in the moment.
     */
    protected Ball ball;

    /**
     * Gets the current ball.
     *
     * @return Current ball.
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Sets the ball.
     * 
     * @param ball Ball.
     */
    public void setBall(Ball ball) {
        this.ball = ball;
    }
}
