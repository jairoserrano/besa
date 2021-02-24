/*
 * @(#)DEF.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Abstraction;

/**
 * Defines the contants values of the system.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public enum DEF {

    //------------------------------------------------------------------------//
    // Defines the player constants.                                          //
    //------------------------------------------------------------------------//
    /**
     * Player of team name's A.
     */
    PLAYER_A(0),
    /**
     * Player of team name's B.
     */
    PLAYER_B(1),
    /**
     *
     */
    XA_POSITION(4),
    /**
     *
     */
    YA_POSITION(61),
    /**
     *
     */
    XB_POSITION(517),
    /**
     *
     */
    YB_POSITION(256),
    //------------------------------------------------------------------------//
    // Defines the world model constants.                                     //
    //------------------------------------------------------------------------//
    /**
     * Player of team name's A.
     */
    WORLD_2D(0),
    /**
     * Player of team name's B.
     */
    WORLD_3D(1),

    //------------------------------------------------------------------------//
    //
    //------------------------------------------------------------------------//

    PING_TRAGET_REGION_AX1(500),
    PING_TRAGET_REGION_AY1(100),

    PING_TRAGET_REGION_AX2(500),
    PING_TRAGET_REGION_AY2(270),

    //------------------------------------------------------------------------//
    //
    //------------------------------------------------------------------------//

    PONG_TRAGET_REGION_AX1(110),
    PONG_TRAGET_REGION_AY1(100),

    PONG_TRAGET_REGION_AX2(110),
    PONG_TRAGET_REGION_AY2(270),

    //------------------------------------------------------------------------//
    // Defines the properties and limits of the player profile.               //
    //------------------------------------------------------------------------//
    /**
     * Incates the expert level of the player.
     */
    EXPERT(0.66),
    /**
     * Incates the professional level of the player.
     */
    PROFESSIONAL(0.33),
    /**
     * Incates the novice level of the player.
     */
    NOVICE(0.0),
    /**
     * Erorr.
     */
    ERROR(-1),
    //------------------------------------------------------------------------//
    // Defines the random limts.                                              //
    //------------------------------------------------------------------------//
    /**
     * Lower limit
     */
    LOWER_LIMIT(0.0),
    /**
     * Intermediate limit.
     */
    INTERMEDIATE_LIMIT_A(0.33),
    /**
     * Intermediate limit.
     */
    INTERMEDIATE_LIMIT_B(0.66),
    /**
     * Upper limit.
     */
    UPPER_LIMIT(1);
    //------------------------------------------------------------------------//
    // Enum type body.                                                        //
    //------------------------------------------------------------------------//
    /**
     * Value of the expretion.
     */
    private final double value;

    /**
     * Creates a value.
     *
     * @param value Value.
     */
    private DEF(double value) {
        this.value = value;
    }

    /**
     * Gets the expretion value.
     *
     * @return value.
     */
    public final double getValue() {
        return value;
    }
}
