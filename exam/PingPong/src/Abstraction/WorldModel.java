/*
 * @(#)WorldModel.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Abstraction;

import BESA.Kernel.System.Directory.AgHandlerBESA;
import Presentation.GUIObserver;
import java.util.Vector;

/**
 * This class represents the world model generalization.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public abstract class WorldModel {

    /**
     * Observers.
     */
    private Vector<GUIObserver> observers;
    /**
     *
     */
    protected String PLAYER;

    /**
     * Creates a new instance.
     */
    public WorldModel() {
        observers = new Vector<GUIObserver>();
    }

    /**
     * This method notifies the GUI of the changues. 
     */
    public void notifyLocalGUI(Ball ball, DEF player, double[] playerPV) {
        for (int index = 0; index < observers.size(); index++) {
            observers.get(index).localChangueModel(ball, player, playerPV);
        }
    }

    /**
     * This method notifies the GUI of the changues.
     */
    public void notifyDistributedGUI(Ball ball, double[] playerPV) {
        for (int index = 0; index < observers.size(); index++) {
            observers.get(index).distributedChangueModel(ball, playerPV);
        }
    }

    /**
     * Registers observers.
     */
    public void registerObserver(GUIObserver observer) {
        observers.add(observer);
    }

    /**
     * Unregisters observers.
     */
    public void unregisterObserver(GUIObserver observer) {
        observers.remove(observer);
    }

    /**
     * Update the world model of way local.
     *
     * @param ball Current ball information.
     * @param pPV Player position vector.
     */
    public abstract void localUpdate(Ball ball, DEF player, double[] pPV);

    /**
     * Update the world model of way local.
     *
     * @param ball Current ball information.
     * @param pPV Player position vector.
     */
    public abstract void distributedUpdate(AgHandlerBESA ah, Ball ball, double[] pPV, boolean mode);

    /**
     *
     * @param PLAYER
     */
    public void setPLAYER(String PLAYER) {
        this.PLAYER = PLAYER;
    }

    public abstract void distributedUpdate(Ball ball, double[] pPV);
}
