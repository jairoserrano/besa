/*
 * @(#)GuardBAP.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved. Takina
 * and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern.OutputBAP;

import BESA.ExceptionBESA;
import BESA.Extern.BAP;
import BESA.Extern.ExternAdmBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the guard that send events to BAP.
 *
 * @author SIDRe - Pontificia Universidad Javeriana
 * @author Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since JDK1.0
 */
public class GuardBAP extends GuardBESA {

    /**
     * Sends events to BAP.
     *
     * @param eventBesa BESA event.
     */
    @Override
    public void funcExecGuard(EventBESA eventBesa) {
        DataBAP dataBAP = (DataBAP) eventBesa.getData();                        //Gets the BESA data from event.
        //--------------------------------------------------------------------//
        // Checks if the data is for send event to BAP.                       //
        //--------------------------------------------------------------------//
        if (dataBAP.getStr().equals(BAP.SEND_EVENT_BAP_BY_ID)) {
            try {
                ((ExternAdmBESA) (this.getAgent().getAdmLocal())).getBap().sendEvent(dataBAP.getEv(), dataBAP.getAgAlias(), dataBAP.getbPOLocation(),BAP.SEND_EVENT_BAP_BY_ID);
            } catch (ExceptionBESA ex) {
                Logger.getLogger(GuardBAP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (dataBAP.getStr().equals(BAP.SEND_EVENT_BAP_BY_ALIAS)) {
            try {
                ((ExternAdmBESA) (this.getAgent().getAdmLocal())).getBap().sendEvent(dataBAP.getEv(), dataBAP.getAgAlias(), dataBAP.getbPOLocation(),BAP.SEND_EVENT_BAP_BY_ALIAS);
            } catch (ExceptionBESA ex) {
                Logger.getLogger(GuardBAP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
