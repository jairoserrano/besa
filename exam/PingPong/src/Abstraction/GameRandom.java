/*
 * @(#)GameRandom.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Abstraction;

import java.util.Random;

/**
 * This class represents to random.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class GameRandom {

    /**
     * Reference the ramdom object.
     */
    private static Random rand = new Random();

    /**
     * Crates a new player profile.
     * 
     * @return Player profile instance.
     */
    public static PlayerProfile createPlayerProfile() {
        //--------------------------------------------------------------------//
        // Defines the type values of the profile.                            //
        //--------------------------------------------------------------------//
        double precision = 0;
        double agility = 0;
        double reflections = 0;
        double force = 0;
        double physical = 0;
        DEF level = null;
        //--------------------------------------------------------------------//
        // Generates profile values.                                          //
        //--------------------------------------------------------------------//
        switch (evaluateCategory(rand.nextDouble())) {
            case NOVICE:
                precision = getTalent(DEF.NOVICE.getValue());
                agility = getTalent(DEF.NOVICE.getValue());
                reflections = getTalent(DEF.NOVICE.getValue());
                force = getTalent(DEF.NOVICE.getValue());
                physical = getTalent(DEF.NOVICE.getValue());
                level = DEF.NOVICE;
                break;
            case PROFESSIONAL:
                precision = getTalent(DEF.PROFESSIONAL.getValue());
                agility = getTalent(DEF.PROFESSIONAL.getValue());
                reflections = getTalent(DEF.PROFESSIONAL.getValue());
                force = getTalent(DEF.PROFESSIONAL.getValue());
                physical = getTalent(DEF.PROFESSIONAL.getValue());
                level = DEF.PROFESSIONAL;
                break;
            case EXPERT:
                precision = getTalent(DEF.EXPERT.getValue());
                agility = getTalent(DEF.EXPERT.getValue());
                reflections = getTalent(DEF.EXPERT.getValue());
                force = getTalent(DEF.EXPERT.getValue());
                physical = getTalent(DEF.EXPERT.getValue());
                level = DEF.EXPERT;
                break;
        }
        return new PlayerProfile(precision, agility, reflections, force, physical, level);
    }

    /**
     * Evaluates if the category of the number.
     * 
     * @param random Tve value to evaluate. 
     * @return The category.
     */
    private static DEF evaluateCategory(double random) {
        if (random >= DEF.LOWER_LIMIT.getValue() && random < DEF.INTERMEDIATE_LIMIT_A.getValue()) {
            return DEF.NOVICE;
        }
        if (random >= DEF.INTERMEDIATE_LIMIT_A.getValue() && random < DEF.INTERMEDIATE_LIMIT_B.getValue()) {
            return DEF.PROFESSIONAL;
        }
        if (random >= DEF.INTERMEDIATE_LIMIT_B.getValue() && random <= DEF.UPPER_LIMIT.getValue()) {
            return DEF.EXPERT;
        }
        return DEF.ERROR;
    }

    /**
     * Gets the talent from hability.
     * 
     * @param value Hability.
     * @return Talent.
     */
    private static double getTalent(double value) {
        return value + DEF.INTERMEDIATE_LIMIT_A.getValue() * rand.nextDouble();
    }

    /**
     *
     * @return
     */
    public static double getREvent() {
        return rand.nextDouble();
    }
}
