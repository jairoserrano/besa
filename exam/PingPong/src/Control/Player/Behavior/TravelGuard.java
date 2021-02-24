/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.Player.Behavior;

import Abstraction.Ball;
import Abstraction.WorldModel;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import Control.Player.Data.BallPosition;
import Control.Player.Data.PlayerState;
import Control.Player.Data.Response;

/**
 *
 * @author fabianjose
 */
public class TravelGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        //--------------------------------------------------------------------//
        // Gets data and the currente information ball.                       //
        //--------------------------------------------------------------------//
        BallPosition ballPosition = (BallPosition) event.getData();
        Ball ball = ((Response) ballPosition).getBall();
        //--------------------------------------------------------------------//
        // Gets the world model and updates.                                  //
        //--------------------------------------------------------------------//
        PlayerState playerState = (PlayerState) this.agent.getState();
        WorldModel worldModel = playerState.getWorldModel();
        if (!this.agent.getAdmLocal().isCentralized()) {
            worldModel.distributedUpdate(ball, playerState.getPPV());
        }
    }
    
}
