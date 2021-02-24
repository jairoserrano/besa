/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package BESA.Extern;

import BESA.Config.ConfigBESA;
import BESA.ExceptionBESA;
import BESA.Extern.Directory.AgExternHandlerBESA;
import BESA.Extern.InputBAP.ExternInputExceptionBESA;
import BESA.Extern.OutputBAP.AgentBAP;
import BESA.Extern.OutputBAP.AgentLocationBAP;
import BESA.Extern.OutputBAP.DataBAP;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Log.ReportBESA;
import BESA.Remote.DistributedExceptionBESA;
import BESA.Remote.RemoteAdmBESA;
import java.util.UUID;

/**
 *
 * @author fabianjose
 */
public class ExternAdmBESA extends RemoteAdmBESA {

    /**
     * Interoperability model BAP.
     */
    private BAP bap = null;
    private final int ATTEMPS = 7;

    /**
     * Creates a unique instance for interoperability model BAP.
     *
     * @param admAlias Alias/name of the admnistrador.
     * @param passwd Security key, it is necessary to be able to create or
     * destroy the administrator.
     * @param ipAddress The IP address of the machine that contains the
     * administrator to be registered.
     * @param rpcPort The port of the machine that contains the administrator to
     * be registered.
     * @param multicastAddr Multicast address used by the administrators in the
     * same SMA.
     * @param multicastPort Multicast port used by the administrators in the
     * same SMA.
     * @param centralized Indicates if the container is going to be executed in
     * a single machine or if it is going to be used in a distributed way.
     * @param bapPort BAP port.
     * @param bapLocatorAdd The IP address of the machine that BAP locator.
     * @return Reference to the unique/singleton instance of the local
     * administrator.
     */
    public ExternAdmBESA(ConfigBESA configBESA) throws DistributedExceptionBESA {
        //--------------------------------------------------------------------//
        // BAP is necessary to set here because this refers to the            //
        // instance of local container and this is not created until          //
        // the end of the constructor of the BESA adm.                        //
        //--------------------------------------------------------------------//
        super(configBESA);                                                      //Creates an instance for a locoal administrator.
        INSTANCE = this;
        try {//TODO Modificar con wait notify.
            Thread.sleep(1000);                                                 //I wait some time to see if anyone else is discharged.
        } catch (InterruptedException e) { //End try.
            ReportBESA.error(e);
        } //End catch.
        //--------------------------------------------------------------------//
        // Verify that there are no records of other containers,              //
        // if so its crates the BAP on the container, otherwise there         //
        // is no action that should have started another container            //
        // interoperability services to the inside and outside                //
        // the neighborhood.                                                  //
        //--------------------------------------------------------------------//
        if (remoteDirectory.thereAreNotAdm()) {
            try {
                bap = new BAP(this, configBESA.getBapport(), configBESA.getBaplocatoradd()); //Initialize the BAP in this neighborhood, will provide all services to the outside of the neighborhood, these are initialized here.
            } catch (ExternInputExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        } //End if.
    }

    /**
     * 
     * @param agAlias
     * @return
     * @throws ExceptionBESA 
     */
    @Override
    public AgHandlerBESA getHandlerByAlias(String agAlias) throws ExceptionBESA {
        AgHandlerBESA ah = localDirectory.getHandlerByAlias(agAlias);
        if (ah != null) {
            return ah;
        } else {
            String agLocation = null;
            if (bap != null) {
                agLocation = bap.getAgentLocationByAlias(agAlias);
                if (agLocation != null) {
                    String agID = UUID.randomUUID().toString();
                    AgExternHandlerBESA agExternHandlerBESA = registerExternAgent(agLocation, agID, agAlias);
                    return agExternHandlerBESA;
                }
            } else {
                AgHandlerBESA bapHandler = AdmBESA.getInstance().getHandlerByAlias(AgentBAP.class.getName());
                DataBAP dataBAP = new DataBAP(BAP.GET_AGENT_LOCATION, agAlias);
                EventBESA evBAP = new EventBESA(AgentLocationBAP.class.getName(), dataBAP);
                bapHandler.sendEvent(evBAP);
                //------------------------------------------------------------//
                //
                //------------------------------------------------------------//
                int cont = 0;
                while (cont < ATTEMPS) {
                    try {
                        cont++;
                        Thread.sleep(1000);
                        ah = localDirectory.getHandlerByAlias(agAlias);
                        if (ah != null) {
                            return ah;
                        }
                    } catch (InterruptedException ex) {
                        ReportBESA.error(ex);
                    }
                }
            }
            //----------------------------------------------------------------//
            // 
            //----------------------------------------------------------------//
            throw new SystemExceptionBESA("Agent with alias: " + agAlias + ", not found.");
        }
    }

    
    /**
     * 
     * @param agID
     * @return
     * @throws ExceptionBESA 
     */
    @Override
    public AgHandlerBESA getHandlerByAid(String agAlID) throws ExceptionBESA {
        AgHandlerBESA ah = localDirectory.getHandlerByAid(agAlID);
        if (ah != null) {
            return ah;
        } else {
            String agLocation = null;
            if (bap != null) {
                agLocation = bap.getAgentLocationByID(agAlID);
                if (agLocation != null) {
                    //String agID = UUID.randomUUID().toString();
                    AgExternHandlerBESA agExternHandlerBESA = registerExternAgent(agLocation, agAlID, "TempAlias"+agAlID);
                    return agExternHandlerBESA;
                }
            } else {
                AgHandlerBESA bapHandler = AdmBESA.getInstance().getHandlerByAlias(AgentBAP.class.getName());
                DataBAP dataBAP = new DataBAP(BAP.GET_AGENT_LOCATION, agAlID);
                EventBESA evBAP = new EventBESA(AgentLocationBAP.class.getName(), dataBAP);
                bapHandler.sendEvent(evBAP);
                //------------------------------------------------------------//
                //
                //------------------------------------------------------------//
                int cont = 0;
                while (cont < ATTEMPS) {
                    try {
                        cont++;
                        Thread.sleep(1000);
                        ah = localDirectory.getHandlerByAid(agAlID);
                        if (ah != null) {
                            return ah;
                        }
                    } catch (InterruptedException ex) {
                        ReportBESA.error(ex);
                    }
                }
            }
            //----------------------------------------------------------------//
            // 
            //----------------------------------------------------------------//
            throw new SystemExceptionBESA("Agent with ID: " + agAlID + ", not found.");
        }
    }
    
    /**
     * 
     * @param agLocation
     * @param agID
     * @param agAlias
     * @return 
     */
    public AgExternHandlerBESA registerExternAgent(String agLocation, String agID, String agAlias) throws SystemExceptionBESA {
        AgExternHandlerBESA agExternHandlerBESA = new AgExternHandlerBESA(getConfigBESA(), agLocation, agID, agAlias);
        super.registerAgent(agID, agExternHandlerBESA, agAlias);
        super.publicagent(agAlias, agID);
        return agExternHandlerBESA;
    }

    /**
     * 
     * @return 
     */
    public BAP getBap() {
        return bap;
    }
}
