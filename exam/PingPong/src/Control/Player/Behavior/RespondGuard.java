/*
 * @(#)RespondGuard.java 2.0	11/01/11
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
import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import Control.Player.Data.Ping;
import Control.Player.Data.PlayerState;
import Control.Player.Data.Pong;
import Control.Player.Data.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the respond action.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class RespondGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        DEF player = null;
        //--------------------------------------------------------------------//
        // Gets data and the currente information ball.                       //
        //--------------------------------------------------------------------//
        DataBESA data = event.getData();
        Ball ball = ((Response) data).getBall();
        //--------------------------------------------------------------------//
        // Gets the world model and updates.                                  //
        //--------------------------------------------------------------------//
        PlayerState playerState = (PlayerState) this.agent.getState();
        WorldModel worldModel = playerState.getWorldModel();
        if (!this.agent.getAdmLocal().isCentralized()) {
            worldModel.distributedUpdate(null,ball, playerState.getPPV(), true);
        }
        //--------------------------------------------------------------------//
        // Gets the agent handler through of player alias.                    //
        //--------------------------------------------------------------------//
        AgHandlerBESA ah = null;
        Response response = null;
        if (this.agent.getAlias().equalsIgnoreCase(DEF.PLAYER_A.name())) {
            player = DEF.PLAYER_A;
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias(DEF.PLAYER_B.name());
            } catch (ExceptionBESA ex) {
                Logger.getLogger(RespondGuard.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            player = DEF.PLAYER_B;
            try {
                ah = this.agent.getAdmLocal().getHandlerByAlias(DEF.PLAYER_A.name());
            } catch (ExceptionBESA ex) {
                Logger.getLogger(RespondGuard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //--------------------------------------------------------------------//
        // Response.                                                          //
        //--------------------------------------------------------------------//
        if (data instanceof Ping) {
            ReportBESA.debug("[" + this.agent.getAlias() + "]: Pong.");
            response = new Pong();
            ball.setOrientation(-1);
            calculateAtractor(ball, false);
        } else {
            ReportBESA.debug("[" + this.agent.getAlias() + "]: Ping.");
            response = new Ping();
            ball.setOrientation(1);
            calculateAtractor(ball, true);
        }
        //--------------------------------------------------------------------//
        // Calculate.                                                         //
        //--------------------------------------------------------------------//
        if (!this.agent.getAdmLocal().isCentralized()) {
            worldModel.distributedUpdate(ah, ball, playerState.getPPV(), false);
        } else {
            worldModel.localUpdate(ball, player, playerState.getPPV());
        }
        response.setBall(ball);
        //--------------------------------------------------------------------//
        // Sends the ping event to agent that represente the moment when the  //
        // player hits the ball with adders to the table of the oponent.      //
        //--------------------------------------------------------------------//        
        EventBESA ev = new EventBESA(RespondGuard.class.getName(), response);
        try {
            ah.sendEvent(ev);
        } catch (ExceptionBESA e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param ball
     * @param ping
     */
    private void calculateAtractor(Ball ball, boolean ping) {
        double event;
        //--------------------------------------------------------------------//
        // Calculates the target.  y = (AX2 - AX1)x + AX1.                    //
        //--------------------------------------------------------------------//
        event = GameRandom.getREvent();
        if (ping) {
            ball.getTPV()[0] = DEF.PING_TRAGET_REGION_AX1.getValue();
            ball.getTPV()[1] = ((DEF.PING_TRAGET_REGION_AY2.getValue() - DEF.PING_TRAGET_REGION_AY1.getValue()) * event) + DEF.PING_TRAGET_REGION_AY1.getValue();
        } else {
            ball.getTPV()[0] = DEF.PONG_TRAGET_REGION_AX1.getValue();
            ball.getTPV()[1] = ((DEF.PONG_TRAGET_REGION_AY2.getValue() - DEF.PONG_TRAGET_REGION_AY1.getValue()) * event) + DEF.PONG_TRAGET_REGION_AY1.getValue();
        }
    }
}
