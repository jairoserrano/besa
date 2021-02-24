/*
 * @(#)PlayerProfile.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Abstraction;

/**
 * This class represents to player profile.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class PlayerProfile {

    /**
     * 
     */
    private double precision;
    /**
     *
     */
    private double agility;
    /**
     *
     */
    private double reflections;
    /**
     *
     */
    private double force;
    /**
     *
     */
    private double physical;
    /**
     * Idicates of the player level.
     */
    private DEF level;

    public PlayerProfile(double precision, double agility, double reflections, double force, double physical, DEF level) {
        this.precision = precision;
        this.agility = agility;
        this.reflections = reflections;
        this.force = force;
        this.physical = physical;
        this.level = level;
    }
}
