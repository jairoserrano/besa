/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package BESA.Extern.OutputBAP;

import BESA.ExceptionBESA;
import BESA.Extern.BAP;
import BESA.Extern.ExternAdmBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import java.util.UUID;

/**
 *
 * @author fabianjose
 */
public class AgentLocationBAP extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        DataBAP dataBAP = (DataBAP) event.getData();                        //Gets the BESA data from event.
        //--------------------------------------------------------------------//
        // Checks if the data is for send event to BAP.                       //
        //--------------------------------------------------------------------//
        if (dataBAP.getStr().equals(BAP.GET_AGENT_LOCATION)) {
            ExternAdmBESA externAdmBESA = ((ExternAdmBESA) (this.getAgent().getAdmLocal()));
            try {
                String agLocation = externAdmBESA.getBap().getAgentLocationByAlias(dataBAP.getAgAlias());
                if (agLocation != null) {
                    String agID = UUID.randomUUID().toString();
                    externAdmBESA.registerExternAgent(agLocation, agID, dataBAP.getAgAlias());
                }
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }
    }
    
}
