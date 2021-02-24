/*
 * @(#)PlayerState.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Control.Player.Data;

import Abstraction.PlayerProfile;
import Abstraction.WorldModel;
import BESA.Kernel.Agent.StateBESA;

/**
 * This class represents player state in the game.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class PlayerState extends StateBESA {

    /**
     * World model.
     */
    private WorldModel worldModel;
    /**
     * Represents the player profile.
     */
    private PlayerProfile profile;
    /**
     * Playert position vector <i,j,k>.
     */
    private double[] playerPositionVector;

    /**
     * Creates a new instance.
     */
    public PlayerState(WorldModel worldModel) {
        this.worldModel = worldModel;
        playerPositionVector = new double[3];
    }

    /**
     * Gets the profile.
     *
     * @return Player profile.
     */
    public PlayerProfile getProfile() {
        return profile;
    }

    /**
     * Sets the profile.
     *
     * @param profile Player profile.
     */
    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
    }

    /**
     * Gets the world model.
     *
     * @return World model.
     */
    public WorldModel getWorldModel() {
        return worldModel;
    }

    /**
     * Sets the world model.
     *
     * @param worldModel World model.
     */
    public void setWorldModel(WorldModel worldModel) {
        this.worldModel = worldModel;
    }

    public double[] getPPV() {
        return playerPositionVector;
    }

    public void setPPV(double[] playerPositionVector) {
        this.playerPositionVector = playerPositionVector;
    }
}
