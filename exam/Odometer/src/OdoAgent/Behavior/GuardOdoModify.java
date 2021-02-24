/*
 * @(#)GuardOdoModify.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package OdoAgent.Behavior;

import OdoAgent.Data.DataOdo;
import OdoAgent.StateOdo;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Log.ReportBESA;
import OdoAgent.CounterOdo;

/**
 * This class represents the GuardOdoModify. The GuardOdoModify simulates the
 * progress the odometer counter. The simulation is controlled for the
 * GuardMoveOdo.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @see GuardMoveOdo
 * @since   JDK1.0
 */
public class GuardOdoModify extends GuardBESA {

    /**
     * Code blocks that checks if the guard can be executed or not.
     *
     * @param objEvalBool References to the agent state.
     * @return true if the guard can be executed or false in other case.
     */
    @Override
    public boolean funcEvalBool(StateBESA objEvalBool) {
        CounterOdo counter = ((StateOdo) objEvalBool).getCounter();             //Gets the agent state.
        ReportBESA.debug("ESTOY EVALUANDO MODIFY");
        return counter.getValue() < 1000; //Checks if activated the condition.
    }

    /**
     * Function executed guard.
     *
     * @param dataEvent Event with DataOdo data. DataOdo contains the possible
     * message: increments (INC) and decrements (DEC).
     * @see GuardMoveOdo
     * @see DataOdo
     */
    @Override
    public void funcExecGuard(EventBESA dataEvent) {
        //--------------------------------------------------------------------//
        // Gets the data from event and gets the odometer state.              //
        //--------------------------------------------------------------------//
        String data = ((DataOdo) dataEvent.getData()).getMenssage();
        StateOdo stateOdo = (StateOdo) getAgent().getState();
        //--------------------------------------------------------------------//
        // Checks the menssage the data. If the menssage is INC, is prosseded //
        // to imcrementes the odometer counter. If the menssage is DEC, is    //
        // prosseded to decrements the odometer counter.                      //
        //--------------------------------------------------------------------//
        ReportBESA.info("ESTOY EJECUTANDO MODIFY");
        if (stateOdo.getState() == StateOdo.State.Init) {                       //Checks if the odometer was in initial state.
            stateOdo.changueState(StateOdo.State.Start);                        //Indicates that odometer is moved.
        }
        if (data.equals("INC")) {                                               //Checks if the menssage is of increment.
            stateOdo.incrementCounter();
        } else if (data.equals("DEC")) {                                        //Checks if the menssage is of decrements.
            stateOdo.decrementCounter();
        }
        ReportBESA.debug(data + "-> Count = " + stateOdo.getCounter().getValue());
    }
}
