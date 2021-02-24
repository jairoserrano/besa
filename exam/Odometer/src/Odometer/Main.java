/*
 * @(#)Main.java 3.0	20/09/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Odometer;

import BESA.ExceptionBESA;
import OdoAgent.StateOdo;
import OdoAgent.AgentOdo;
import OdoAgent.Behavior.GuardOdoModify;
import DisplayAgent.Behavior.DisplayGuard;
import OdoAgent.Behavior.GuardMoveOdo;
import DisplayAgent.Behavior.StopGuard;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BESA.Util.PeriodicDataBESA;
import DisplayAgent.DisplayAgent;
import DisplayAgent.DisplayState;

/**
 * This class represents the start point of the application.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 2.0, 20/09/11
 * @since   JDK1.0
 */
public class Main {

    /**
     * Parameter for define the periodic time.
     */
    private static long PERIODIC_TIME = 1000;

    /**
     * Creates a new instance of Main
     */
    public Main() {
    }

    /**
     * Main method.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        //--------------------------------------------------------------------//
        // Creates and starts the BESA container.                             //
        //--------------------------------------------------------------------//
        AdmBESA adm = AdmBESA.getInstance();
        //--------------------------------------------------------------------//
        // Creates the display agent.                                         //
        //--------------------------------------------------------------------//        
        DisplayAgent displayAgent = null;
        String alias = "Display";
        DisplayState displaySate = new DisplayState();
        StructBESA displayStruct = new StructBESA();
        double passwdAg = 0.91;
        try {
            displayStruct.bindGuard(DisplayGuard.class);                        //Binds GuardOdoRequest: Displays the progress the odometer counter
            displayStruct.bindGuard(StopGuard.class);                           //Binds StopOdoGuard: Time out guard that control the live cycle of odometer.
            displayAgent = new DisplayAgent(alias, displaySate, displayStruct, passwdAg); 
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }        
        displayAgent.start();
        //--------------------------------------------------------------------//
        // Creates the odometer agent.                                        //
        //--------------------------------------------------------------------//
        AgentOdo agOdo = null;
        alias = "Odometro_1";
        StateOdo stateAgOdo = new StateOdo();
        StructBESA structOdo = new StructBESA();
        passwdAg = 0.92;        
        try {
            structOdo.addBehavior("BehaviorOdo"); 
            structOdo.bindGuard("BehaviorOdo", GuardOdoModify.class);           //Binds GuardOdoModify: Simulates the progress the odometer counter.
            structOdo.bindGuard("BehaviorOdo", GuardMoveOdo.class);             //Binds GuardMoveOdo: Periodic guard that directed the progress the odometer counter.            
            agOdo = new AgentOdo(alias, stateAgOdo, structOdo, passwdAg);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        agOdo.start();        
        //--------------------------------------------------------------------//
        // Starts the odometer, creates a PeriodicDataBESA guard and start    //
        // command (for starts the process the increments the counter). The   //
        // PeriodicDataBESA is a data type predefine for the configuration of // 
        // the periodic guard.                                                //
        //--------------------------------------------------------------------//
        AgHandlerBESA ah = null;
        PeriodicDataBESA periodicData = new PeriodicDataBESA(PERIODIC_TIME, PeriodicGuardBESA.START_PERIODIC_CALL);//Creates the start message for periodic guard.
        EventBESA startPeriodicEv = new EventBESA(GuardMoveOdo.class.getName(), periodicData);//Creates the event and sends to the GuardMoveOdo.
        try {
            ah = adm.getHandlerByAid(agOdo.getAid());
            ah.sendEvent(startPeriodicEv);
        } catch (ExceptionBESA e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------//
        // Creates a event with a start message for starts time-out           //
        // GuardBESA.                                                         //
        //--------------------------------------------------------------------//
        EventBESA startTimeOut = new EventBESA(StopGuard.class.getName(), null);
        try {
            ah = adm.getHandlerByAid(displayAgent.getAid());
            ah.sendEvent(startTimeOut);
        } catch (ExceptionBESA e) {
            e.printStackTrace();
        }
    }
}
