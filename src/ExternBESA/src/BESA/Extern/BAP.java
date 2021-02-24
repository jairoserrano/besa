/*
 * @(#)BAP.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved. Takina
 * and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern;

import BESA.ExceptionBESA;
import BESA.Extern.InputBAP.ExternInputExceptionBESA;
import BESA.Extern.InputBAP.SocketServerBAP;
import BESA.Extern.OutputBAP.*;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * This class represents the access point of the Extern model.
 *
 * @author SIDRe - Pontificia Universidad Javeriana
 * @author Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since JDK1.0
 */
public class BAP {

    /**
     * Reference to local administrator of the BESA container.
     */
    private static ExternAdmBESA admLocal;
    /**
     * Communication port of the BAP.
     */
    private int bapPort;
    /**
     * BAP Agent.
     */
    private AgentBAP agBAP;
    /**
     * Passsword of the agent BAP.
     */
    private double agentBAPPwd = 0.2311;
    /**
     * This a web service connection interface that interact with BAPLocator
     * server.
     */
    private IBAPLocator bapLocator;
    /**
     * Table that represents the agents cache.
     */
    private Hashtable<String, String> agAliasTable;
    /**
     * Table that represents the agents cache.
     */
    private Hashtable<String, String> agIDTable;
    /**
     * List to set the delete order of the elements the cahe.
     */
    private ArrayList<String> agIDList;
    /**
     * List to set the delete order of the elements the cahe.
     */
    private ArrayList<String> agAliasList;
    /**
     * Cache size.
     */
    private static final int cacheSizeBAP = 10;
    /**
     * Look up agent time-out.
     */
    private static final int lookUpAgentTimeout = 5000;
    /**
     * In the communication between BAPs, the sentence "SEND_EVENT" indicates to
     * BAP receptor that the data are a DataBESA.
     */
    public static final String SEND_EVENT_BAP_BY_ID = "SEND_EVENT_BY_ID";
    /**
     * In the communication between BAPs, the sentence "SEND_EVENT" indicates to
     * BAP receptor that the data are a DataBESA.
     */
    public static final String SEND_EVENT_BAP_BY_ALIAS = "SEND_EVENT_BY_ALIAS";
    /**
     *
     */
    public static final String LOOK_UP_MSN = "LOOK_UP_MSN";
    public static final String CONTAINER_LOCATION_MSN = "CONTAINER_LOCATION_MSN";
    /**
     * In the communication between BAPs, the sentence "SEND_EVENT" indicates to
     * BAP receptor that the data are a DataBESA.
     */
    public static final String GET_AGENT_LOCATION = "GET_AGENT_LOCATION";
    /**
     * In the communication between BAPs, the sentence "LOOKUP_AGENT" indicates
     * to BAP receptor that if has a agent into registry by alias.
     */
    public static final String SEND_LOOKUP_AGENT_BY_ALIAS = "LOOKUP_AGENT_BY_ALIAS";
    /**
     * In the communication between BAPs, the sentence "LOOKUP_AGENT" indicates
     * to BAP receptor that if has a agent into registry by id.
     */
    public static final String SEND_LOOKUP_AGENT_BY_ID = "LOOKUP_AGENT_BY_ID";
    /**
     * In the communication between BAPs, the sentence "AGENT_FOUND" indicates
     * to BAP receptor that the agent is into registry.
     */
    public static final String SEND_AGENT_FOUND_BY_ALIAS = "AGENT_FOUND_BY_ALIAS";
    /**
     * In the communication between BAPs, the sentence "AGENT_FOUND" indicates
     * to BAP receptor that the agent is into registry.
     */
    public static final String SEND_AGENT_FOUND_BY_ID = "AGENT_FOUND_BY_ID";
    /**
     *
     */
    public static final String SEND_AGENT_DESTROY = "AGENT_DESTROY";
    /**
     * In the communication between BAPs, the sentence "AGENT_FOUND" indicates
     * to BAP receptor that the agent isn't into registry.
     */
    public static final String SEND__AGENT_DESTROY = "AGENT_DESTROY ";
    /**
     * In the communication between BAPs, the sentence "SEND_ACK" indicates to
     * BAP receptor that the data arrived sucessful.
     */
    public static final String SEND_ACK = "SEND_ACK";
    /**
     * BAP delimiter for messages between BAP and BAP LOCATOR.
     */
    public static final String BAP_DELIMITER = "%";
    /**
     * BAP delimiter for messages between BAP LOCATOR and BAP.
     */
    public static final String BAP_LOCATOR_DELIMITER = "°";
    /**
     * BAP delimiter for messages between BAP and BAP LOCATOR.
     */
    public static final String DATA_DELIMITER = "#";
    private String bapAdd;

    /**
     * Creates a new instance of the BAP.
     *
     * @param adm Current administrator.
     * @param bapPort Current the input point port.
     * @param bapLocatorAdd Current address for input point.
     */
    public BAP(ExternAdmBESA adm, int bapPort, String bapLocatorAdd) throws ExternInputExceptionBESA {
        if (admLocal == null) {                                                 //Checks if the current instance have not a reference of the ADM.
            admLocal = adm;                                                     //Sets the references.
        }
        //--------------------------------------------------------------------//
        // Creates the space memory for agent alias table and agent alias     //
        // list.                                                              //
        //--------------------------------------------------------------------//
        this.bapPort = bapPort;
        this.agAliasTable = new Hashtable<String, String>();
        this.agIDTable = new Hashtable<String, String>();
        this.agIDList = new ArrayList<String>();
        this.agAliasList = new ArrayList<String>();
        //--------------------------------------------------------------------//
        // Creates the agent that provides the communication services for     //
        // inside neighborhood.                                               //
        //--------------------------------------------------------------------//
        this.bapLocator = new BAPLocator().getBAPLocatorPort();
        String aliasAgentBAP = AgentBAP.class.getName();                        //Creates the agent alias.
        StateBAP stateAgentBAP = new StateBAP();                                //Creates the BAP state.
        //--------------------------------------------------------------------//
        // Creates the BAP struct.                                            //
        //--------------------------------------------------------------------//
        StructBESA structAgentBAP = new StructBESA();
        structAgentBAP.bindGuard(GuardBAP.class);//Binds the BAP guard.
        structAgentBAP.bindGuard(AgentLocationBAP.class);
        //--------------------------------------------------------------------//
        // Starts the server socket that allows receive connections from      //
        // the port that was specificated.                                    //
        //--------------------------------------------------------------------//
        try {
            this.agBAP = new AgentBAP(aliasAgentBAP, stateAgentBAP, structAgentBAP, this.agentBAPPwd); //Creates the agent.
            this.agBAP.start();
            //--------------------------------------------------------------------//
        } catch (KernelAgentExceptionBESA ex) {
            ReportBESA.error(ex);//TODO
        }
        SocketServerBAP ssb = new SocketServerBAP(admLocal, bapPort);           //Creates a new instance of SocketServerBAP.
        if (ssb.startSocketServerBAP()) {                                       //Checks if the SocketServer has created successful.
            ssb.start();                                                        //Starts the SocketServer.
        }
        //--------------------------------------------------------------------//
        // Registers into the BAP the data access to BAP.                     //
        //--------------------------------------------------------------------//
//        bapAdd = this.getAdmLocal().getConfigBESA().getIpaddress();
        InetAddress direccion;
        bapAdd = "0.0.0.0";
        try {
            direccion = InetAddress.getLocalHost();
            bapAdd = direccion.getHostAddress();
        } catch (UnknownHostException ex) {
            ReportBESA.error(ex);
        }
        this.registerBAP(this.getAdmLocal().getIdAdm(), bapAdd, bapPort);
    }

    /**
     * Sets the BAP agent.
     *
     * @param agentBAP BAP agent.
     */
    public void setAgBAP(AgentBAP agentBAP) {
        this.agBAP = agentBAP;
    }

    /**
     * Register of a new BAP into registry of the BAPLocator.
     *
     * @param admId Administrator ID.
     * @param bapAdd BAP IP Address.
     * @param bapPort BAP Port.
     */
    public void registerBAP(String admId, String bapAdd, int bapPort) {
        this.bapLocator.registerBAP(admId, bapAdd, bapPort);
    }

    /**
     * Register of a new Agent into registry of the BAPLocator.
     *
     * @param bapAdd Administrator ID.
     * @param bapPort BAP IP Address.
     * @param agAlias Agent alias.
     */
    public void registerAgent(String bapAdd, int bapPort, String agAlias) {
        this.bapLocator.registerAgent(agAlias, bapAdd, bapPort);
    }

    public void unRegisterAgent(String agID, String agAlias, Vector<Referred> referredList) throws ExternExceptionBESA {
        this.bapLocator.unRegisterAgent(agID);
        SenderBAP sender = new SenderBAP(admLocal);
        for (int index = 0; index < referredList.size(); index++) {
            Referred referred = referredList.get(index);
            sender.sendAgentDestroy(bapAdd, this.bapPort, referred.getBapDesAdd(), referred.getBapDesPort(), agAlias);
        }
    }

    /**
     * Gets the agent location.
     *
     * @param adID Agent alias.
     * @return The agent location.
     */
    public synchronized String getAgentLocationByID(String adID) throws ExternExceptionBESA {
        //--------------------------------------------------------------------//
        // Defines the variable "agLocation" that saves the BAP Agent         //
        // location data. As BAP + BAP port.                                  //
        //--------------------------------------------------------------------//
        String agLocation = "";
        //--------------------------------------------------------------------//
        // Checks if the agent is into cache for search the agent and return  //
        // his location data. If there are not agent then uses the BAP        //
        // Locator interface for nkow his location.                           //
        //--------------------------------------------------------------------//
        if (this.agIDTable.get(adID) != null) {
            //----------------------------------------------------------------//
            // Orders the cache from the agent minus searched until the       //
            // agent more searched.                                           //
            //----------------------------------------------------------------//
            this.agIDList.remove(adID);
            this.agIDList.add(adID);
            agLocation = (String) this.agIDTable.get(adID);
        } else {                                                                //There is not agent then uses the BAP locator interface.
            agLocation = getAgLocationByID(adID);                                   //Gets the agente location from the BAB Locator.
            //----------------------------------------------------------------//
            // If the agent there is not into registry of the BAP Locator     //
            // then gets the location of the all the BAPs and with multi-BAP  //
            // message is query the location of the agent.                    //
            //----------------------------------------------------------------//
            if (agLocation == null) {                                           //Checks if the agent there is not into registry.
                String[] BAPList = this.bapLocator.getBAPsLocation(bapAdd, bapPort).split(BAP.BAP_LOCATOR_DELIMITER);

//TODO Borrar codigo de prueba para getMasterLocation
//String cosa = this.bapLocator.getMasterLocation("10.0.0.1");
//System.out.println("!!!!!!!!!!!!!!: " + cosa);

                for (String bapLocation : BAPList) {
                    if (!bapLocation.equals("")) {
                        //------------------------------------------------//
                        // Sends multi-BAP look up message.               //
                        //------------------------------------------------//
                        SenderBAP sender = new SenderBAP(admLocal);
                        sender.lookUpAgent(bapAdd, this.bapPort, bapLocation.split(BAP.BAP_DELIMITER)[0], Integer.parseInt(bapLocation.split(BAP.BAP_DELIMITER)[1]), adID, BAP.SEND_LOOKUP_AGENT_BY_ID);
                    }
                }
                //--------------------------------------------------------//
                // Waits until get response of the agent found.           //
                //--------------------------------------------------------//
                try {
                    this.wait(3000);                                            //See setAgentLocation method.
                } catch (InterruptedException ex) {
                    ReportBESA.error("[BAP::getAgentLocation()] " + ex.getMessage());
                }
                //--------------------------------------------------------//
                // Gets the location agent from the cache that has        //
                // updated.                                               //
                //--------------------------------------------------------//
                if (agLocation == null) {
                    agLocation = (String) this.agIDTable.get(adID);
                }
            }
        }
        return agLocation;                                                      //Returns the location agent.
    }

    /**
     * Gets the agent location.
     *
     * @param agAlias Agent alias.
     * @return The agent location.
     */
    public synchronized String getAgentLocationByAlias(String agAlias) throws ExternExceptionBESA {
        //--------------------------------------------------------------------//
        // Defines the variable "agLocation" that saves the BAP Agent         //
        // location data. As BAP + BAP port.                                  //
        //--------------------------------------------------------------------//
        String agLocation = "";
        //--------------------------------------------------------------------//
        // Checks if the agent is into cache for search the agent and return  //
        // his location data. If there are not agent then uses the BAP        //
        // Locator interface for nkow his location.                           //
        //--------------------------------------------------------------------//
        if (this.agAliasTable.get(agAlias) != null) {
            //----------------------------------------------------------------//
            // Orders the cache from the agent minus searched until the       //
            // agent more searched.                                           //
            //----------------------------------------------------------------//
            this.agAliasList.remove(agAlias);
            this.agAliasList.add(agAlias);
            agLocation = (String) this.agAliasTable.get(agAlias);
        } else {                                                                //There is not agent then uses the BAP locator interface.
            agLocation = getAgLocationByAlias(agAlias);                                //Gets the agente location from the BAB Locator.
            //----------------------------------------------------------------//
            // If the agent there is not into registry of the BAP Locator     //
            // then gets the location of the all the BAPs and with multi-BAP  //
            // message is query the location of the agent.                    //
            //----------------------------------------------------------------//
            if (agLocation == null) {                                           //Checks if the agent there is not into registry.
                String[] BAPList = this.bapLocator.getBAPsLocation(bapAdd, bapPort).split(BAP.BAP_LOCATOR_DELIMITER);

//TODO Borrar codigo de prueba para getMasterLocation
//String cosa = this.bapLocator.getMasterLocation("10.0.0.1");
//System.out.println("!!!!!!!!!!!!!!: " + cosa);

                for (String bapLocation : BAPList) {
                    if (!bapLocation.equals("")) {
                        //------------------------------------------------//
                        // Sends multi-BAP look up message.               //
                        //------------------------------------------------//
                        SenderBAP sender = new SenderBAP(admLocal);
                        sender.lookUpAgent(bapAdd, this.bapPort, bapLocation.split(BAP.BAP_DELIMITER)[0], Integer.parseInt(bapLocation.split(BAP.BAP_DELIMITER)[1]), agAlias, BAP.SEND_LOOKUP_AGENT_BY_ALIAS);
                    }
                }
                //--------------------------------------------------------//
                // Waits until get response of the agent found.           //
                //--------------------------------------------------------//
                try {
                    this.wait(3000);                                            //See setAgentLocation method.
                    System.out.println("OUT WAIT");
                } catch (InterruptedException ex) {
                    ReportBESA.error("[BAP::getAgentLocation()] " + ex.getMessage());
                }
                //--------------------------------------------------------//
                // Gets the location agent from the cache that has        //
                // updated.                                               //
                //--------------------------------------------------------//
                if (agLocation == null) {
                    agLocation = (String) this.agAliasTable.get(agAlias);
                }
            }
        }
        return agLocation;                                                      //Returns the location agent.
    }

    /**
     * Gets the agente location from the BAB Locator.
     *
     * @param agID Agent alias.
     * @return Agent location.
     */
    private String getAgLocationByID(String agID) {
        String agLocation = "";
        agLocation = this.bapLocator.getAgentLocation(agID);                    //Gets the agent location.
        if (agLocation != null) {
            this.agIDTable.put(agID, agLocation);                               //Updates agent table.
        }
        return agLocation;
    }

    /**
     * Gets the agente location from the BAB Locator.
     *
     * @param agAlias Agent alias.
     * @return Agent location.
     */
    private String getAgLocationByAlias(String agAlias) {
        String agLocation = "";
        agLocation = this.bapLocator.getAgentLocation(agAlias);                 //Gets the agent location.
        if (agLocation != null) {
            this.agAliasTable.put(agAlias, agLocation);                         //Updates agent table.
        }
        return agLocation;
    }

    /**
     * Waits until get response of the agent found.
     *
     * @deprecated
     */
    public synchronized void waitAgentLookUp() {
        try {
            Thread.sleep(BAP.lookUpAgentTimeout);
            this.notify();
        } catch (Exception ie) {
            ReportBESA.error("[BAP::waitAgentLookUp()] " + ie.getMessage());
        }
    }

    /**
     * Registers the agents location as resulted of the search the agent into
     * all the BAPs.
     *
     * @param bapAdd BAP Address.
     * @param bapPort BAP port.
     * @param agID Agent alias.
     */
    public void setAgentLocationByID(String bapAdd, int bapPort, String agID) {//synchronized
        try {
            this.agIDList.add(agID);                                      //Updates agent list.
            this.agIDTable.put(agID, bapAdd + BAP_DELIMITER + bapPort);   //Updates agent table.            
            this.notify();                                                      //See getAgentLocation method.
            this.registerAgent(bapAdd, bapPort, agID);
        } catch (Exception ie) {
            ReportBESA.error("[BAP::setAgentLocation()] " + ie.getMessage());
        }
    }

    /**
     * Registers the agents location as resulted of the search the agent into
     * all the BAPs.
     *
     * @param bapAdd BAP Address.
     * @param bapPort BAP port.
     * @param agAlias Agent alias.
     */
    public void setAgentLocationByAlias(String bapAdd, int bapPort, String agAlias) {//synchronized
        try {
            this.agAliasList.add(agAlias);                                      //Updates agent list.
            this.agAliasTable.put(agAlias, bapAdd + BAP_DELIMITER + bapPort);   //Updates agent table.            
            this.notify();                                                      //See getAgentLocation method.
            this.registerAgent(bapAdd, bapPort, agAlias);
        } catch (Exception ie) {
            ReportBESA.error("[BAP::setAgentLocation()] " + ie.getMessage());
        }
    }

    public void setAgentDestroy(String bapAdd, int bapPort, String agAlias) {
        try {
            this.agAliasList.remove(agAlias);                                   //Updates agent list.
            this.agAliasTable.remove(agAlias);                                  //Updates agent table.
        } catch (Exception ie) {
            ReportBESA.error("[BAP::setAgentLocation()] " + ie.getMessage());
        }
    }

    /**
     * Sends the agents location that has queried for a BAP.
     *
     * @param bapDesAdd BAP destination address.
     * @param bapDesPort BAP destination port.
     * @param url Agent alias.
     */
    public void sendAgentFound(String bapDesAdd, int bapDesPort, String url, String command) throws ExternExceptionBESA {
        SenderBAP sender = new SenderBAP(admLocal);
        sender.sendAgentFound(bapAdd, this.bapPort, bapDesAdd, bapDesPort, url, command);
    }

    /**
     *
     * @param ev
     * @param agAlias
     */
    public void sendEvent(EventBESA ev, String agAlias, String bPOLocation, String command) throws ExceptionBESA {
        SenderBAP sender = new SenderBAP(admLocal);
        sender.sendEvent(ev, agAlias, bPOLocation, command);
    }

    /**
     * Gets the local administrator.
     *
     * @return Local administrator
     */
    public AdmBESA getAdmLocal() {
        return admLocal;
    }

    /**
     * Gets the BAP agent.
     *
     * @return BAP agent.
     */
    public AgentBAP getAgBAP() {
        return this.agBAP;
    }
}
