/*
 * @(#)Main.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Pingpong;

import Abstraction.DEF;
import Abstraction.Game;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import Control.Builder.Builder;
import Control.Builder.DistributedBuilder;
import Control.Builder.LocalBuilder;

/**
 * This class represents the start point of the application.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class PlayerAMain {

    /**
     * Main method.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        //--------------------------------------------------------------------//
        // Creates an starts the BESA container.                              //
        //--------------------------------------------------------------------//        
        AdmBESA admLocal = AdmBESA.getInstance("res/Container1.xml");
        ReportBESA.info("Creates and starts the BESA container.");
        //--------------------------------------------------------------------//
        // Builds the system.                                                 //
        //--------------------------------------------------------------------//
        ReportBESA.info("Builds the system.");
        Builder builder;
        if (admLocal.isCentralized()) {                                         //Checks if the system is CENTRALIZED.
            builder = new LocalBuilder();                                       //Instances the local builder.
        } else {
            builder = new DistributedBuilder();                                 //Instances the distributed builder.
        }
        //--------------------------------------------------------------------//
        // Creates the view and model.                                        //
        //--------------------------------------------------------------------//
        ReportBESA.info("Creates the view and model.");
        builder.buildGUI();
        builder.buildModel(DEF.WORLD_2D);
        //--------------------------------------------------------------------//
        // Creates the agents.                                                //
        //--------------------------------------------------------------------//
        ReportBESA.info("Creates the agents.");
        builder.buildPlayerA();
        builder.buildPlayerB();
        //--------------------------------------------------------------------//
        // Starts the game.                                                   //
        //--------------------------------------------------------------------//
        ReportBESA.info("Starts the game.");
        Game game = builder.getGame();
        game.start(admLocal);
    }
}
