/*
 * @(#)GuardMoveOdo.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package OdoAgent.Behavior;

import DisplayAgent.Behavior.DisplayGuard;
import BESA.ExceptionBESA;
import OdoAgent.Data.DataOdo;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import OdoAgent.StateOdo;

/**
 * This class represents to the GuardMoveOdo. The GuardMoveOdo directed the
 * progress the odometer counter. This class extends of the PeriodicGuardBESA
 * which implements the instructions that believes the periodic control of the
 * one guard.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class GuardMoveOdo extends PeriodicGuardBESA {

    /**
     * Creates a new instance.
     *
     * @param ag References to agent.
     */
    /*public GuardMoveOdo() {
    super();
    }*/
    /**
     * Code block that executed in function of the periodic time that was
     * specificated.
     *
     * @param event Automatic event that marks the time execution periodic
     * time.
     */
    @Override
    public void funcPeriodicExecGuard(EventBESA event) {
        //--------------------------------------------------------------------//
        // 
        //--------------------------------------------------------------------//        
        double rand = Math.random();
        DataOdo datOdo;                                                         //Creates the increment message.        
        if (rand >= 0.5) {
            datOdo = new DataOdo("INC");                                        //Creates the increment message.
        } else {
            datOdo = new DataOdo("DEC");                                        //Creates the decrement message.        
        }
        //--------------------------------------------------------------------//
        // Creates and sends the increment counter messages (the event).      //
        // The messages is sent to GuardOdoModify which simulates             //
        // the progress the odometer counter.                                 //
        //--------------------------------------------------------------------//
        AgHandlerBESA ah = null;
        EventBESA ev = new EventBESA(GuardOdoModify.class.getName(), datOdo);   //Creates the data event for GuardOdoModify.
        try {
            ah = AdmBESA.getInstance().getHandlerByAlias("Odometro_1");         //Gets the agent handler.
            ah.sendEvent(ev);                                                   //Sends the event.
        } catch (Exception ex) {
            ReportBESA.error(ex.getMessage());
        }
        //--------------------------------------------------------------------//
        // Creates and sends the show counter messages (the event).           //
        // The messages is sent to GuardOdoRequest which display              //
        // the progress the odometer counter.                                 //
        //--------------------------------------------------------------------//
        StateOdo stateOdo = (StateOdo) this.getAgent().getState();
        int currentValue = stateOdo.getCounter().getValue();
        String valueString = String.valueOf(currentValue);                      //Parses the currente counter value to string.
        datOdo = new DataOdo("SHOW");                                           //Creates the increment message.
        datOdo.setValueString(valueString);
        ev = new EventBESA(DisplayGuard.class.getName(), datOdo);               //Creates the data event for GuardOdoRequest.
        try {
            ah = this.agent.getAdmLocal().getHandlerByAlias("Display");         //Gets the agent handler.
            ah.sendEvent(ev);                                                   //Sends the event.
            ReportBESA.debug("SHOW Count =" + stateOdo.getCounter().getValue());
        } catch (ExceptionBESA e) {
            ReportBESA.error(e.getMessage());
        }
    }
}
