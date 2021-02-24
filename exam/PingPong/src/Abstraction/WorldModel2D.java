/*
 * @(#)WorldModel2D.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Abstraction;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import Control.Player.Behavior.TravelGuard;
import Control.Player.Data.BallPosition;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the an world model in two dimension implementation.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class WorldModel2D extends WorldModel {

    /**
     * Update the world model.
     *
     * @param ball Current ball information.
     */
    @Override
    public void localUpdate(Ball ball, DEF player, double[] pPV) {
        double index = 0.0;
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        double deltaX = ball.getTPV()[0] - ball.getPV()[0];
        double deltaY = ball.getTPV()[1] - ball.getPV()[1];
        double sqrt = Math.sqrt((deltaY * deltaY) + (deltaX * deltaX));
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        double deltaPlayer = ball.getTPV()[1] - pPV[1];
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        ball.setVisible(true);
        while (index < sqrt) {
            ReportBESA.debug("[Ball]: X: " + ball.getPV()[0] + " Y: " + ball.getPV()[1]);
            ReportBESA.debug("[" + player.name() + "]: X: " + pPV[0] + " Y: " + pPV[1]);
            pPV[1] = pPV[1] + (deltaPlayer * 0.1);
            ball.getPV()[0] = ball.getPV()[0] + (deltaX * 0.1);
            ball.getPV()[1] = ball.getPV()[1] + (deltaY * 0.1);
            index += (sqrt * 0.1);
            this.notifyLocalGUI(ball, player, pPV);
            delay();
        }
        ball.setVisible(false);
    }

    /**
     * Update the world model of way local.
     *
     * @param ball Current ball information.
     */
    @Override
    public void distributedUpdate(AgHandlerBESA ah, Ball ball, double[] pPV, boolean mode) {
        double index = 0.0;
        double deltaPlayer = 0.0;
        int orientation = ball.getOrientation();
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        //double deltaX = Math.abs(ball.getTPV()[0] - ball.getPV()[0]);
        //double deltaY = Math.abs(ball.getTPV()[1] - ball.getPV()[1]);
        
        double deltaX = ball.getTPV()[0] - ball.getPV()[0];
        double deltaY = ball.getTPV()[1] - ball.getPV()[1];
        
        double sqrt = Math.sqrt((deltaY * deltaY) + (deltaX * deltaX));
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        if (mode) {
            //----------------------------------------------------------------//
            //
            //----------------------------------------------------------------//
            if (PLAYER.equalsIgnoreCase(DEF.PLAYER_B.name())) {
                ball.getPV()[0] = 0;
            } else {
                ball.getPV()[0] = 300;
                ball.getPV()[1] = ball.getPV()[1];
                deltaX = ball.getTPV()[0] - ball.getPV()[0];
                deltaY = ball.getTPV()[1] - ball.getPV()[1];
                sqrt = Math.sqrt((deltaY * deltaY) + (deltaX * deltaX));
            }
        }
        while (index < 270) {
            
            //----------------------------------------------------------------//
            //
            //----------------------------------------------------------------//            
            if (mode) {
                //----------------------------------------------------------------//
                //
                //----------------------------------------------------------------//
                deltaPlayer = ball.getTPV()[1] - pPV[1];
                if (Math.abs(deltaPlayer) > 0) {
                    pPV[1] = pPV[1] + (deltaPlayer * 0.1);
                }
            }
            //----------------------------------------------------------------//
            //
            //----------------------------------------------------------------//

            //ball.getPV()[0] = ball.getPV()[0] + (deltaX * 0.1 * orientation);
            //ball.getPV()[1] = ball.getPV()[1] + (deltaY * 0.1 * orientation);
            
            ball.getPV()[0] = ball.getPV()[0] + (deltaX * 0.1);
            ball.getPV()[1] = ball.getPV()[1] + (deltaY * 0.1);
            
            
            index += (sqrt * 0.1);
            if (!(index < 270)) {
                ball.setVisible(false);
            }
            //----------------------------------------------------------------//
            // Informs the other agent about the current  ball position.      //
            //----------------------------------------------------------------//
            if (ah != null) {
                try {
                    BallPosition ballPosition = new BallPosition();
                    ballPosition.setBall(ball);
                    EventBESA event = new EventBESA(TravelGuard.class.getName(), ballPosition);
                    ah.sendEvent(event);
                } catch (ExceptionBESA ex) {
                    ReportBESA.error(ex.toString());
                }
            }
            //----------------------------------------------------------------//
            // Displays the changes into scenario.                            //
            //----------------------------------------------------------------//
            ReportBESA.debug("[" + PLAYER + "]: X: " + pPV[0] + " Y: " + pPV[1]);
            this.notifyDistributedGUI(ball, pPV);
            delay();
        }
        ball.setVisible(true);
    }

    /**
     * 
     */
    private void delay() {
        try {
            Thread.sleep(100);//100);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorldModel2D.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void distributedUpdate(Ball ball, double[] pPV) {
        double deltaPlayer = 0.0;
        //--------------------------------------------------------------------//
        //
        //--------------------------------------------------------------------//
        deltaPlayer = ball.getTPV()[1] - pPV[1];
        pPV[1] = pPV[1] + (deltaPlayer * 0.1);
        //--------------------------------------------------------------------//
        // Displays the changes into scenario.                                //
        //--------------------------------------------------------------------//
        ReportBESA.debug("[" + PLAYER + "]: X: " + pPV[0] + " Y: " + pPV[1]);
        ball.setVisible(false);
        this.notifyDistributedGUI(ball, pPV);
        delay();
    }
}
