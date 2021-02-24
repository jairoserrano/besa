/*
 * @(#)Ball.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Abstraction;

import java.io.Serializable;

/**
 * This class represents a play ball.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class Ball implements Serializable {

    /**
     * Position vector <i,j,k>.
     */
    private double[] positionVector;
    /**
     * Target position vector <i,j,k>.
     */
    private double[] targetPositionVector;
    /**
     * Force vector <i,j,k>.
     */
    private double[] forceVector;
    /**
     * Linear velocity vector  <i,j,k>.
     */
    private double[] linearVelocityVector;
    /**
     * Angular velocity vector  <i,j,k>.
     */
    private double[] angularVelocityVector;
    /**
     * Orientation ball.
     */
    private int orientation;
    /**
     *
     */
    private boolean visible;

    /**
     * Creates a ne intance.
     */
    public Ball() {
        targetPositionVector = new double[3];
        positionVector = new double[3];
        forceVector = new double[3];
        linearVelocityVector = new double[3];
        angularVelocityVector = new double[3];
        orientation = 1;
        visible = true;
    }

    /**
     * Creates a ne intance.
     *
     * @param positionVector Position vector.
     * @param forceVector Force vector.
     * @param linearVelocityVector Linear velocity vector.
     * @param angularVelocityVector Angular velocity vector.
     */
    public Ball(double[] positionVector, double[] targetPositionVector, double[] forceVector, double[] linearVelocityVector, double[] angularVelocityVector) {
        this.targetPositionVector = targetPositionVector;
        this.positionVector = positionVector;
        this.forceVector = forceVector;
        this.linearVelocityVector = linearVelocityVector;
        this.angularVelocityVector = angularVelocityVector;
    }

    /**
     * Gets the angular velocity vector <i,j,k>.
     *
     * @return Angular velocity vector.
     */
    public double[] getAVV() {
        return angularVelocityVector;
    }

    /**
     * Gets the force vector  <i,j,k>.
     *
     * @return Force vector.
     */
    public double[] getFV() {
        return forceVector;
    }

    /**
     * Gets the linear velocity  <i,j,k>.
     *
     * @return Linear velocity vector.
     */
    public double[] getLVV() {
        return linearVelocityVector;
    }

    /**
     * Sets the angular velocity vector.
     * 
     * @param angularVelocityVector Angular velocity vector.
     */
    public void setAVelocityVector(double[] angularVelocityVector) {
        this.angularVelocityVector = angularVelocityVector;
    }

    /**
     * Sets the force vector.
     *
     * @param forceVector Force vector.
     */
    public void setForceVector(double[] forceVector) {
        this.forceVector = forceVector;
    }

    /**
     * Sets the linear velocity vector.
     *
     * @param linearVelocityVector Linear velocity vector.
     */
    public void setLinearVelocityVector(double[] linearVelocityVector) {
        this.linearVelocityVector = linearVelocityVector;
    }

    /**
     * Gets position vector.
     *
     * @return Position vector.
     */
    public double[] getPV() {
        return positionVector;
    }

    /**
     * Gets the target position vector.
     *
     * @return Target position vector.
     */
    public double[] getTPV() {
        return targetPositionVector;
    }

    /**
     * Sets position vector.
     * 
     * @param positionVector Position vector.
     */
    public void setPositionVector(double[] positionVector) {
        this.positionVector = positionVector;
    }

    /**
     * Sets the target position vector.
     *
     * @param positionVector Target position vector.
     */
    public void setTargetPositionVector(double[] targetPositionVector) {
        this.targetPositionVector = targetPositionVector;
    }

    /**
     * Gets the orientation.
     *
     * @return Orientation.
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation.
     *
     * @param orientation Orientation.
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * Gets the visible.
     *
     * @return Visible indicator.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visible indicator.
     *
     * @param visible Visible indicator.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
