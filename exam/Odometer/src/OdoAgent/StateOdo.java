/*
 * @(#)StateOdo.java 2.0	20/09/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package OdoAgent;

import BESA.Kernel.Agent.StateBESA;
import java.util.ArrayList;

/**
 * This class represents a odometer agent state.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class StateOdo extends StateBESA {

    /**
     * Enumeration that indicates the possible states of the odometer.
     */
    public enum State {
        Init, Start, Stop
    }
    /**
     * Current odometer state.
     */
    private State state;
    /**
     * Initializes into initState method using the profile.
     */
    private int step;
    /**
     * Counter of the odometer.
     */
    private CounterOdo counter;
    
    /**
     * Creates a new instance.
     *
     */
    public StateOdo() {
        super();       
    }

    /**
     * Change the state.
     * 
     * @param state
     */
    public void changueState(State state) {
        this.state = state;
    }

    /**
     * Initializes the state.
     * 
     * @param profile Startup parameters.
     */
    public synchronized void initState(ArrayList profile) {
        step = ((Integer) profile.get(0));
    }

    /**
     * Increments the counter.
     */
    public synchronized void incrementCounter() {
        counter.addValue(step);
        update();
    }

    /**
     * Decrements the counter.
     */
    public synchronized void decrementCounter() {
        counter.addValue(-1 * step);
        update();
    }

    /**
     * Gets the step.
     *
     * @return Step.
     */
    public synchronized int getStep() {
        return step;
    }

    /**
     * Sets step.
     *
     * @param step Step.
     */
    public synchronized void setStep(int step) {
        this.step = step;
    }

    /**
     * Gets the counter.
     * @return Counter.
     */
    public CounterOdo getCounter() {
        return counter;
    }

    /**
     * Sets the counter.
     * 
     * @param counter Counter.
     */
    public void setCounter(CounterOdo counter) {
        this.counter = counter;
    }

    /**
     * Sets the state.
     * 
     * @param state State.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Gets state.
     *
     * @return State.
     */
    public State getState() {
        return state;
    }
}
