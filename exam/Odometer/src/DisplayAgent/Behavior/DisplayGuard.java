/*
 * @(#)GuardOdoRequest.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package DisplayAgent.Behavior;

import OdoAgent.Data.DataOdo;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import DisplayAgent.DisplayState;
import OdoAgent.StateOdo;

/**
 * This class represents GuardOdoRequest. GuardOdoRequest which display the
 * progress the odometer counter. The display is controlled for the
 * GuardMoveOdo.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class DisplayGuard extends GuardBESA {

    /**
     * Digit limit for display into presentation.
     */
    private final int DIGITSLIMIT = 8;

    /**
     * Displays the progress the odometer counter.
     *
     * @param dataEvent Event with DataOdo data. DataOdo contains the possible
     * message: SHOW.
     */
    @Override
    public void funcExecGuard(EventBESA dataEvent) {
        //--------------------------------------------------------------------//
        // Gets the data from event. Gets the agente state.                   //
        //--------------------------------------------------------------------//
        DataOdo data = ((DataOdo) dataEvent.getData());
        DisplayState state = (DisplayState) this.getAgent().getState();
        state.changueState(StateOdo.State.Start);                               //Indicates that odometer is moved.
        //--------------------------------------------------------------------//
        // Checks if the menssage is SHOW, if it is, then makes the           //
        // calculations and display the change of the counter.                //
        //--------------------------------------------------------------------//
        String message = data.getMenssage();
        if (message.equals("SHOW")) {
            int cont = 0;
            String valueToUpdate = "";
            //----------------------------------------------------------------//
            // makes the calculates for the display counter digit.            //
            //----------------------------------------------------------------//
            String valueString = data.getValueString();
            int digitNumber = DIGITSLIMIT - valueString.length();               //Claculates the digit count to change.
            for (int index = 0; index < DIGITSLIMIT; index++) {                 //Changues the digits.
                if (((index + 1) % 3) == 0) {                                   //Checks if is the space place.
                    valueToUpdate += " ";                                       //Sets the space.
                    if (((valueString.length()) % 3) == 0) {
                        digitNumber--;
                    }
                } else {                                                        //Is the space of the number.
                    if (index >= digitNumber) {                                 //Checks if thera are diget to set.
                        valueToUpdate += valueString.charAt(cont);
                        cont++;
                    } else {
                        valueToUpdate += "0";
                    }
                }
            }
            //----------------------------------------------------------------//
            // Displays the change of the odometer counter.                   //
            //----------------------------------------------------------------//
            state.getOdoGUI().getjLValue().setText(valueToUpdate);           //Displays the counter change.            
        }
    }
}
