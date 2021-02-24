/*
 * @(#)Main.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Abstraction;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import Control.Player.Behavior.PutOnGuard;
import Control.Player.Data.PutOnData;
import Presentation.GUIObserver;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the game instance.
 *
 * @author SIDRe - Pontificia Universidad Javeriana
 * @author Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since JDK1.0
 */
public class Game {

    /**
     * GUI.
     */
    private GUIObserver GUI;
    /**
     * World model.
     */
    private WorldModel worldModel;
    /**
     *
     */
    private String PLAYER;

    /**
     * Creates a new instance.
     */
    public Game() {
    }

    /**
     * Starts the game.
     *
     * @param admLocal
     */
    public void start(AdmBESA admLocal) {
        if (admLocal.isCentralized() || PLAYER.equalsIgnoreCase(DEF.PLAYER_A.name())) {
            //----------------------------------------------------------------//
            // Gets the agent handler through of player alias.                //
            //----------------------------------------------------------------//
            AgHandlerBESA ah = null;
            try {
                ah = admLocal.getHandlerByAlias(DEF.PLAYER_A.name());
                //----------------------------------------------------------------//
                // Sends the put on event to agent.                               //
                //----------------------------------------------------------------//
                //delay();
                PutOnData putOnData = new PutOnData();
                EventBESA ev = new EventBESA(PutOnGuard.class.getName(), putOnData);
                ah.sendEvent(ev);
            } catch (ExceptionBESA ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

        }
    }

    /**
     * Gets the GUI.
     *
     * @return GUI.
     */
    public GUIObserver getGUI() {
        return GUI;
    }

    /**
     * Sets GUI.
     *
     * @param GUI GUI.
     */
    public void setGUI(GUIObserver GUI) {
        this.GUI = GUI;
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
     *
     */
    private void delay() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets the world model.
     *
     * @param worldModel World model.
     */
    public void setWorldModel(WorldModel worldModel) {
        this.worldModel = worldModel;
    }

    /**
     *
     * @param PLAYER
     */
    public void setPLAYER(String PLAYER) {
        this.PLAYER = PLAYER;
    }
}
