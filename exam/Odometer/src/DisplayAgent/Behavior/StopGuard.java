/*
 * @(#)StopOdoGuard.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package DisplayAgent.Behavior;

import BESA.ExceptionBESA;
import OdoAgent.StateOdo;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.Agent.TimeOutGuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BESA.Util.PeriodicDataBESA;
import DisplayAgent.DisplayState;
import OdoAgent.Behavior.GuardMoveOdo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents StopOdoGuard. The StopOdoGuard finalize the simulation
 * the progress of the odometer counter. This class extends of TimeOutGuardBESA
 * which implements the instructions that believes controller the guard as timeout.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class StopGuard extends TimeOutGuardBESA {

    /**
     * Time out.
     */
    private final long TIMEOUT = 8000;

    /**
     * Creates a new instance.
     *
     */
    public StopGuard() {
        super();
    }

    /**
     * Intructions blocks previous to the starts the time out.
     *
     * @param event Events that grants the starts the time out.
     */
    @Override
    public void funcNormalExecGuard(EventBESA event) {
        ReportBESA.info("Inicia el tiempo de espera.");
        this.startTimeOut(TIMEOUT);
    }

    /**
     * Instructions blocks after to end time out.
     *
     * @param event Automatical event that indicates that time out end.
     */
    @Override
    public void funcTimeOutExecGuard(EventBESA event) {
        //--------------------------------------------------------------------//
        // Gets the agent handler.                                            //
        //--------------------------------------------------------------------//
        AgentBESA agOdo = this.getAgent();
        AdmBESA admLocal = this.getAgent().getAdmLocal();
        AgHandlerBESA ah = null;
        try {
            ah = admLocal.getHandlerByAid(agOdo.getAid());
        } catch (ExceptionBESA ex) {
            Logger.getLogger(StopGuard.class.getName()).log(Level.SEVERE, null, ex);
        }
        //--------------------------------------------------------------------//
        // Stop the time out because the time out do reset.                   //
        //--------------------------------------------------------------------//
        this.stopTimeOut();
        //--------------------------------------------------------------------//
        // Creates an event whit PeriodicDataBESA type for stop the           //
        // GuardMoveOdo.                                                      //
        //--------------------------------------------------------------------//
        PeriodicDataBESA periodicData = new PeriodicDataBESA(PeriodicGuardBESA.STOP_CALL);//Data type that indicates to periodic thread of the GuardMoveOdo that shoul stop.
        EventBESA ev = new EventBESA(GuardMoveOdo.class.getName(), periodicData);
        try {
            ah.sendEvent(ev);
        } catch (ExceptionBESA e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------//
        // Changes the odometer state to STOP state.                          //
        //--------------------------------------------------------------------//
        DisplayState displayState = (DisplayState) this.getAgent().getState();
        displayState.getOdoGUI().getjLValue().setForeground(new java.awt.Color(255, 0, 0));//Updates the interface.
        displayState.changueState(StateOdo.State.Stop);
        //--------------------------------------------------------------------//
        // Kills the odometer agent.                                          //
        //--------------------------------------------------------------------//
        try {            
            admLocal.killAgent(ah.getAgId(), 0.91);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        ReportBESA.info("Se elimino el agente.");
        //--------------------------------------------------------------------//
        // Hills the local administrator.                                     //
        //--------------------------------------------------------------------//
        ReportBESA.info("Cierre concluido.");
        try {
            admLocal.kill(0.91);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
