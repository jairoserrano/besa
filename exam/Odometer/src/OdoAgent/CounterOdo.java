/*
 * @(#)CounterOdo.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package OdoAgent;

/**
 * This class represents the odometer counter.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class CounterOdo {

    /**
     * Odometer counter value.
     */
    private int value;

    /**
     * Creates a new instance.
     *
     * @param value Initial value.
     */
    public CounterOdo(int value) {
        this.value = value;
    }

    /**
     * Gets the current counter value.
     *
     * @return Current value.
     */
    public synchronized int getValue() {
        return value;
    }

    /**
     * Sets the new value to the counter.
     *
     * @param value New value.
     */
    public synchronized void setValue(int value) {
        this.value = value;
    }

    /**
     * Increments the current value of the counter.
     *
     * @param step Value to increment.
     */
    public synchronized void addValue(int step) {
        this.value += step;
    }
}
