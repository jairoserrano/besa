/*
 * @(#)PutOnGuard.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Control.Player.Behavior;

import Abstraction.Ball;
import Abstraction.DEF;
import Abstraction.GameRandom;
import Abstraction.WorldModel;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import Control.Player.Data.Ping;
import Control.Player.Data.PlayerState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the put on action.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class PutOnGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        ReportBESA.debug("[" + this.agent.getAlias() + "]: Put on.");
        //--------------------------------------------------------------------//
        // Gets the agent handler through of player alias.                    //
        //--------------------------------------------------------------------//
        AgHandlerBESA ah = null;
        if (this.agent.getAlias().equalsIgnoreCase(DEF.PLAYER_A.name())) {
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias(DEF.PLAYER_B.name());
            } catch (ExceptionBESA ex) {
                Logger.getLogger(PutOnGuard.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias(DEF.PLAYER_A.name());
            } catch (ExceptionBESA ex) {
                Logger.getLogger(PutOnGuard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        Ball ball = calculateAtractor();
        //--------------------------------------------------------------------//
        // Gets the world model and updates.                                  //
        //--------------------------------------------------------------------//
        PlayerState playerState = (PlayerState) this.agent.getState();
        WorldModel worldModel = playerState.getWorldModel();
        if (this.agent.getAdmLocal().isCentralized()) {
            worldModel.localUpdate(ball, DEF.PLAYER_A, playerState.getPPV());
        } else {
            worldModel.distributedUpdate(ah, ball, playerState.getPPV(), false);
        }
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        Ping ping = new Ping();
        ping.setBall(ball);
        //--------------------------------------------------------------------//
        // Sends the ping event to agent that represente the moment when the  //
        // player hits the ball with adders to the table of the oponent.      //
        //--------------------------------------------------------------------//       
        EventBESA ev = new EventBESA(RespondGuard.class.getName(), ping);
        try {
            ah.sendEvent(ev);
        } catch (ExceptionBESA e) {
            e.printStackTrace();
        }
    }

    private Ball calculateAtractor() {
        double random;
        Ball ball = new Ball();
        //--------------------------------------------------------------------//
        // Sets the starts position and default orientation.                  //
        //--------------------------------------------------------------------//
        ball.getPV()[0] = 98.0;
        ball.getPV()[1] = 95.0;
        ball.setOrientation(1);
        //--------------------------------------------------------------------//
        // Calculates the target.  y = (AX2 - AX1)x + AX1.                    //
        //--------------------------------------------------------------------//
        random = GameRandom.getREvent();
        /*ball.getTPV()[0] = DEF.PING_TRAGET_REGION_AX1.getValue();
        ball.getTPV()[1] = ((DEF.PING_TRAGET_REGION_AY2.getValue() - DEF.PING_TRAGET_REGION_AY1.getValue()) * random) + DEF.PING_TRAGET_REGION_AY1.getValue();*/
        ball.getTPV()[0]  = 500;
        ball.getTPV()[1]  = 95.0;
        return ball;
    }
}
