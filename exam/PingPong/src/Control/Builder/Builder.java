/*
 * @(#)Builder.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Control.Builder;

import Abstraction.DEF;
import Abstraction.Game;
import Abstraction.WorldModel;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Log.ReportBESA;
import Control.Player.Behavior.PutOnGuard;
import Control.Player.Behavior.RespondGuard;
import Control.Player.Behavior.TravelGuard;
import Control.Player.Data.PlayerState;
import Control.Player.PlayerAgent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This represents the generalization  the system builders.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public abstract class Builder {

    /**
     * This the object to build.
     */
    protected Game game;

    /**
     * Creates a new instance.
     */
    public Builder() {
        game = new Game();
    }

    /**
     * Creates a BESA agent.
     * 
     * @return A reference to the agent that was created.
     */
    protected AgentBESA createAgent(String plareAlias, WorldModel worldModel) {
        //--------------------------------------------------------------------//
        // Creates the agent struct.                                          //
        //--------------------------------------------------------------------//
        StructBESA agnetStruct = new StructBESA();
        try {
            //----------------------------------------------------------------//
            // Adds behaviors.                                                //
            //----------------------------------------------------------------//
            agnetStruct.addBehavior("PlayerBehavior");
            agnetStruct.addBehavior("PlayerTravelBehavior");
            //----------------------------------------------------------------//
            // Binds guards.                                                  //
            //----------------------------------------------------------------//
            agnetStruct.bindGuard("PlayerBehavior",RespondGuard.class);
            agnetStruct.bindGuard("PlayerBehavior",PutOnGuard.class);
            agnetStruct.bindGuard("PlayerTravelBehavior",TravelGuard.class); 
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
               
        //--------------------------------------------------------------------//
        // Creates the agent state.                                           //
        //--------------------------------------------------------------------//
        double agentPasswd = 0.91;
        PlayerState playerState = new PlayerState(worldModel);
        try {
            //--------------------------------------------------------------------//
            // Creates and starts the agent.                                      //
            //--------------------------------------------------------------------//
            return new PlayerAgent(plareAlias, playerState, agnetStruct, agentPasswd);
        } catch (KernelAgentExceptionBESA ex) {
            Logger.getLogger(Builder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Gets the game object.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Builds the GUI.
     */
    public abstract boolean buildGUI();

    /**
     * Builds the model.
     */
    public abstract boolean buildModel(DEF world);

    /**
     * Builds the player A.
     */
    public abstract boolean buildPlayerA();

    /**
     * Builds the player B.
     */
    public abstract boolean buildPlayerB();
}
