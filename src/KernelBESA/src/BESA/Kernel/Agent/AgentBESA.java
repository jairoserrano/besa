/*
 * @(#)AgentBESA.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved. Takina
 * and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Kernel.Agent;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

/**
 * This class represents an agent in the platform BESA. An agent consists of a
 * concurrent behaviors group, that has associate guards as well.
 *
 * In order to construct new agents it is necessary to create a class that
 * extends of AgentBESA.
 *
 * @author SIDRe - Pontificia Universidad Javeriana
 * @author Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since JDK1.0
 */
public abstract class AgentBESA {

    /**
     * State agent specific.
     */
    private Object stateAgentSpecific;
    /**
     * BESA local administrator.
     */
    private static AdmBESA adm;
    /**
     * Password necessary to access to the agent.
     */
    private double passwd;
    /**
     * Agent ID.
     */
    private String aid = null;
    /**
     * Indicates that the agent is alive.
     */
    private Boolean alive;
    /**
     * Agent behaviors list.
     */
    private ArrayList<BehaviorBESA> behaviors;
    /**
     * Agent alias.
     */
    private String alias;
    /**
     * Internal state of the BESA agent.
     */
    protected StateBESA state;
    /**
     * Channel associated to a BESA agent.
     */
    private ChannelBESA channel;
    /**
     * Agent BESA internal structure.
     */
    private StructBESA structAgent;
    /**
     * Database Connection
     */
    private Connection db = null;

    /**
     * Builds a BESA agent. In order to construct an agent BESA is necessary to
     * provide a structure that contains the associations between behaviors,
     * guards and multi-guards, in addition to a state in where the internal
     * states of the agent are stored.
     *
     * @param alias Name of the agent with whom it will be identified in the
     * BESA container.
     * @param state Internal state of the agent, which inherits of StateBESA.
     * @param structAgent Structure that indicates the associations between
     * guards, multi-guards and behaviors.
     * @param passwd Password necessary to access to the agent.
     */
    public AgentBESA(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        boolean indMove = true;                                                 //Flag that indicates if the agent is in moving for default.
        String agentID = null;
        try {
            //System.out.println(alias + " Entrando");
            this.alias = alias;                                                 //Sets the alias.
            // TODO: DELETE @jairo
            if (!this.loadAgent()) {
                //System.out.println(alias + " Cargando");
                this.structAgent = structAgent;                                 //Sets the struct.
                this.state = state;                                             //Sets the state.
                this.passwd = passwd;                                           //Sets the passwd.
            }
            // TODO: DELETE @jairo
            //System.out.println(alias + " Pasó");
            AgentBESA.adm = AdmBESA.getInstance();                              //Binds to administrator container reference.            
            if (!AgentBESA.adm.doesAgentExist(alias)) {                         //Checks if the agent does not exist.
                indMove = false;                                                //Indicates that the agent is not moving.
            } else {                                                            //The agent exists.
                AgHandlerBESA agH = AgentBESA.adm.getHandlerByAlias(alias);
                if (agH.getClass().getSimpleName().equalsIgnoreCase("AgLocalHandlerBESA")) { //Checks if the agent is local.    
                    indMove = false;                                            //Indicates that the agent is not moving.
                } else {
                    agentID = AgentBESA.adm.erase(agH);                         //Deletes the local reference the agent. The agent is moving.
                } //End else.
            } //End if.
            this.aid = AgentBESA.adm.registerAgent(this, agentID, alias);       //Register agent white pages and get Aid            
            this.behaviors = new ArrayList<>();                                 //Creates the array of behaviors.
            this.channel = new ChannelBESA(this);                               //Creates chanel.
            this.setAlive();                                                    //Ensure that the run main loop start channel.
            if (!adm.isCentralized()) {                                         //Check if the local administrator is not centralized.
                if (indMove == false) {                                         //Check if exists or not the agent.
                    adm.publicagent(alias, this.aid);                           //It makes the publication of the new agent in the other containers.
                } //End if.
            } //End if.
        } catch (Exception e) { //End try.
            ReportBESA.error("Couldn't create an instance the agent " + this.alias + ": " + e.toString());
            throw new KernelAgentExceptionBESA("Couldn't create an instance the agent " + this.alias + ": " + e.toString());
        } //End catch.
    }

    /**
     * Load Agent from Database
     *
     * @autor Jairo Serrano
     * @return true if CheckPoint data exists
     */
    private synchronized boolean loadAgent() {

        //System.out.println(this.alias + ".db");

        File file = new File(this.alias + ".db");

        if (file.exists()) {

            try {
                Class.forName("org.sqlite.JDBC");
                this.db = DriverManager.getConnection("jdbc:sqlite:" + this.alias + ".db");
                System.out.println("Database exists");

                Statement stmt;
                stmt = this.db.createStatement();
                ResultSet rs;

                rs = stmt.executeQuery("SELECT count(ID) FROM AGENT");

                if (!rs.next()) {
                    return false;
                }

                rs = stmt.executeQuery("SELECT * FROM AGENT ORDER BY ID DESC LIMIT 1;");

                while (rs.next()) {

                    String statefromdb = rs.getString("state");
                    String structAgentfromdb = rs.getString("structAgent");
                    String passwdAgentfromdb = rs.getString("passwdAgent");
                    String nameClassAgentfromdb = rs.getString("nameClassAgent");

                    byte[] b1 = Base64.getDecoder().decode(statefromdb);
                    ByteArrayInputStream bi1 = new ByteArrayInputStream(b1);
                    ObjectInputStream si1 = new ObjectInputStream(bi1);

                    byte[] b2 = Base64.getDecoder().decode(structAgentfromdb);
                    ByteArrayInputStream bi2 = new ByteArrayInputStream(b2);
                    ObjectInputStream si2 = new ObjectInputStream(bi2);

                    // TODO: Eliminar cuando se haga la carga desde ADM @jairo
                    this.structAgent = (StructBESA) si2.readObject();
                    this.state = (StateBESA) si1.readObject();
                    this.passwd = (double) new Double(passwdAgentfromdb);

                    System.out.println("Status restored from database");

                }
                rs.close();
                stmt.close();

                //Load data from state
                return true;

            } catch (IOException | ClassNotFoundException | SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

        }
        // Si no hay archivo de DB retorna false
        return false;

    }

    /**
     * Guarda el estado del Agente completo
     *
     * @autor Jairo Serrano
     */
    public synchronized void saveAgent() {

        File file = new File(this.alias + ".db");

        if (file.exists()) {                                                    // the file already existed and the program will enter this block

            try {

                Class.forName("org.sqlite.JDBC");
                this.db = DriverManager.getConnection("jdbc:sqlite:" + this.alias + ".db");

                //agh.setState(AGENTSTATE.CHECKPOINT);
                ByteArrayOutputStream stateObj = new ByteArrayOutputStream();
                ObjectOutputStream so = new ObjectOutputStream(stateObj);
                so.writeObject(this.state);
                so.flush();

                ByteArrayOutputStream structAgentObj = new ByteArrayOutputStream();
                ObjectOutputStream sosa = new ObjectOutputStream(structAgentObj);
                sosa.writeObject(this.structAgent);
                sosa.flush();

                String serialState = Base64.getEncoder().encodeToString(stateObj.toByteArray());
                String serialStructAgent = Base64.getEncoder().encodeToString(structAgentObj.toByteArray());

                String sql = "INSERT INTO AGENT (state, structAgent, passwdAgent, nameClassAgent) VALUES (?,?,?,?);";
                PreparedStatement pstmt = this.db.prepareStatement(sql);
                pstmt.setString(1, serialState);
                pstmt.setString(2, serialStructAgent);
                pstmt.setString(3, String.valueOf(this.passwd));
                pstmt.setString(4, this.getClass().getName());
                pstmt.executeUpdate();

                //ReportBESA.trace("Checkpoint of Agents successfully");
                System.out.println("Checkpoint of Agent " + this.alias + " successfully");
                //agh.notifyCheckpoint();

            } catch (IOException | ClassNotFoundException | SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

        } else { //the file did not exist and you can send your error msg

            try {
                Class.forName("org.sqlite.JDBC");
                this.db = DriverManager.getConnection("jdbc:sqlite:" + this.alias + ".db");

                Statement stmt = this.db.createStatement();

                //ag.getAlias(), ag.getState(), ag.getStructAgent(), passwdAgent, nameClassAgent
                String sql = "CREATE TABLE AGENT "
                        + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " state           TEXT NOT NULL,"
                        + " passwdAgent     TEXT NOT NULL,"
                        + " structAgent     TEXT NOT NULL,"
                        + " nameClassAgent  TEXT NOT NULL,"
                        + " timemark TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

                System.out.println(sql);
                stmt.executeUpdate(sql);
                stmt.close();

                System.out.println("Database creation successfully");

            } catch (ClassNotFoundException | SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

        }

    }

    /**
     * This method starts the agent behavior. TODO Verificar las excepciones.
     */
    public void start() {
        this.state.initGuards();                                                //Starts guards.
        Thread thread = new Thread(this.channel);                               //Creates chanel thread.
        thread.start();                                                         //Starts the chanel.
        try {
            this.waitChannelReady();                                            //Synchronization boot - Wait full channel setup.
            ReportBESA.trace("Channel is ready.");
            AgHandlerBESA agH = AgentBESA.adm.getHandlerByAlias(alias);
            agH.setState(AGENTSTATE.ALIVE);                                     //Updates the agent state.
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex.toString());
            ex.printStackTrace();
        }
    }

    /**
     * Sends an event to an agent. This method doesn't have to be invoked by the
     * user, only must be called by the container to do the events
     * treatment.<BR><BR>
     *
     * EventBESA contains a field to add the sender, which must be filled in
     * explicit form by the programmer.
     *
     * TODO Verificar seguridad.
     *
     * @param ev Event that is deposited in the mailbox.
     * @throws BESA.Exception.ExceptionBESA Is generated if some error happens.
     */
    public void sendEvent(EventBESA ev) throws KernelAgentExceptionBESA {
        try {
            this.channel.getMbox().sendEvent(ev);                               //Send an event agent using the agent's internal reference.
        } catch (Exception e) { //End try.
            ReportBESA.error("Couldn't send the event: " + e.toString());
            throw new KernelAgentExceptionBESA("Couldn't send the event: " + e.toString());
        } //End catch.
    }

    /**
     * Starting routine of the agent. Invoked automatically before the infinite
     * cycle. To consider when implementing this method:
     *
     * - The state was initialized previously in the constructor of the agent. -
     * Create the guards (automatic bind). - Create the behaviors (automatic
     * registry). - Register agent services.
     *
     * It must be implemented by the user to create an agent.
     */
    abstract public void setupAgent();

    /**
     * Closing routine of the agent. In this resources are freed and behaviors
     * are stopped.
     *
     * It is invoked automatically when leaving the infinite cycle.
     *
     * It must be provided by the user.
     */
    abstract public void shutdownAgent();

    /**
     * Returns the aid that identifies of unique way the agent in the container.
     *
     * @return String that identifies of unique way the agent between the BESA
     * containers.
     * @uml.property name="aid"
     */
    final public String getAid() {
        return aid;
    }

    /**
     * Obtains the alias of the agent.
     *
     * @return Alias of the agent.
     * @uml.property name="alias"
     */
    final public String getAlias() {
        return alias;
    }

    /**
     * Returns the agent internal state.
     *
     * @return Internal state of the agent, which inherits of StateBESA.
     */
    public StateBESA getState() {
        return state;
    }

    /**
     * Indicates that the agent is alive.
     */
    private synchronized void setAlive() {
        this.alive = Boolean.TRUE;
    }

    /**
     * Method used for synchronization in mobility tasks.<BR><BR>
     *
     * It doesn't have to be called by the user.
     */
    public synchronized void resetAlive() {
        this.alive = Boolean.FALSE;
    }

    /**
     * Invoked routine to stop an agent and move it. It doesn't have to be
     * called directly by the user, to move an agent, this method must be
     * invoked from AdmBESA.
     *
     * @param passwd Password of the agent.
     */
    final public void move(double passwd) throws KernelAgentExceptionBESA {
        if (Math.abs(this.passwd - passwd) < 0.00001) {                         //Checks password.
            synchronized (alive) {                                              //Synchronized access to the variable alive.
                if (this.isAlive().booleanValue()) {                            //Checks if is alive.
                    this.getChannel().initBehBarrier(this.behaviors.size());    //Synchronization boot - Wait full channel setup.
                    killBehaviors(passwd);                                      //Kills behavioors.
                    this.getChannel().waitBehBarrier();                         //Blocks the behavior while the barrier counter indicates it.
                    //--------------------------------------------------------//
                    // Elimina los comportamientos.                           // 
                    //--------------------------------------------------------//                    
                    for (int i = 0; i < this.behaviors.size(); i++) {
                        this.behaviors.remove(i);
                    }
                    this.resetAlive();                                          //Disables the functions agent.
                    String evType = "KILL_BESA_AGENT";
                    try {
                        EventBESA ev = new EventBESA(evType, this.getAdmLocal().getPasswd());
                        this.sendEvent(ev);
                    } catch (Exception e) {
                        ReportBESA.error("Couldn't move the agent " + this.alias + ": " + e.toString());
                        throw new KernelAgentExceptionBESA("Couldn't move the agent " + this.alias + ": " + e.toString());
                    }
                }
            }
        }
    }

    /**
     * Starts all the behaviors associated to an agent.
     *
     * @return The number of behaviors initialized.
     */
    final protected int startBehaviors() throws KernelAgentExceptionBESA {
        this.channel.initBehBarrier(behaviors.size());                          //Initializes the barrier counter of the behavior.
        int count = 0;
        for (int i = 0; i < behaviors.size(); ++i) {                            //Starts the threads of the behaviors associated.
            BehaviorBESA ibeh = (BehaviorBESA) behaviors.get(i);
            Thread thread = new Thread(ibeh);
            ibeh.setThread(thread);
            thread.start();
            count++;
        } //End for.
        ReportBESA.trace("Before of waitBehBarrier...");
        this.channel.waitBehBarrier();                                          //Blocks the behavior while the barrier counter indicates it.
        ReportBESA.trace("After of waitBehBarrier.");
        return count;
    }

    /**
     * Finalizes all the behaviors associated to an agent.
     *
     * @param passwd Password of the agent.
     */
    final protected void killBehaviors(double passwd) throws KernelAgentExceptionBESA {
        if (Math.abs(this.passwd - passwd) < 0.00001) {                         //Checks the password.
            for (int i = 0; i < behaviors.size(); ++i) {                        //Forces finalization of the threads of the behaviors associated.
                BehaviorBESA ibeh = (BehaviorBESA) behaviors.get(i);
                ReportBESA.trace("Before of kill of the Behavior:" + ibeh.getName());
                ibeh.kill();                                                    //Kills to behavior.
                ReportBESA.trace("After of kill of the Behavior.");
            } //End for.
        } //End if.
        ReportBESA.trace("The time amount of standby time: " + this.getChannel().getbarrierCounter());
        while (this.getChannel().getbarrierCounter() > 0) {
            this.getChannel().signalBehBarrier();
        } //End while.
    }

    /**
     * Synchronizes the death of threads of the behaviors associates.
     *
     * @param passwd Password of the agent.
     */
    final public void joinBehaviors(double passwd) throws KernelAgentExceptionBESA {
        if (Math.abs(this.passwd - passwd) < 0.00001) {
            for (int i = 0; i < behaviors.size(); ++i) {                        //Sync the end of the threads of the behaviors associated.
                BehaviorBESA ibeh = (BehaviorBESA) behaviors.get(i);
                try {
                    ibeh.getThread().join();
                } catch (InterruptedException ie) { //End try.
                    ReportBESA.error("[AgentBESA::joinBehaviors] Couldn't join behaviors." + ie.toString());
                    throw new KernelAgentExceptionBESA("Couldn't join behaviors." + ie.toString());
                } //End catch.
            } //End for.
        } //End if.
    }

    /**
     * Registers the associated port to a guard. Establishing a port associated
     * with the guard. Validates that has not been created a port for guard
     * event.
     *
     * @param guard Guard to being registered in the agent.
     * @return Port associated to the guard for this agent. Returns null if
     * there was already a port for the same event type
     */
    final protected synchronized PortBESA bindGuard(GuardBESA guard) {
        return channel.addPort(guard);
    }

    /**
     * Eliminates the associated port to a guard.
     *
     * @param guard Guard to being unregistered in the agent.
     * @return true if the guard has been eliminated; false otherwise.
     */
    final public synchronized boolean unbindGuard(GuardBESA guard) {
        return channel.removePort(guard);
    }

    /**
     * Registers a behavior. The behavior is including in a vector and the
     * repetitions are avoided.
     *
     * This list of behaviors is useful for the accomplishment of collective
     * synchronizations.
     *
     * Also it is used to kill automatically the behaviors associated to an
     * agent when this dies.
     *
     * @param beh Behavior to register.
     */
    final public synchronized void registerBehavior(BehaviorBESA beh) {
        if (!behaviors.contains(beh)) {
            behaviors.add(beh);
        }
    }

    /**
     * Eliminates a behavior of the structure. Unregistering automatically after
     * loop Behavior. Remove vector behavior. Purge ports.
     *
     * @param beh Behavior to eliminate.
     * @deprecated It's never used within the container.
     */
    final public synchronized void unregisterBehavior(BehaviorBESA beh) {
        behaviors.remove(beh);
        channel.purgePorts(beh);
    }

    /**
     * Associates a behavior to a guard that handles a type of event. It's
     * called internally by AgentBESA and it does not have to be invoked
     * directly .
     *
     * @param beh Behavior to register.
     * @param evType Type of event that corresponds to the name of the class
     * guard.
     * @return Created GuardBESA.
     */
    final public synchronized GuardBESA registerGuard(BehaviorBESA beh, String evType) {
        PortBESA port = channel.findPort(evType);
        if (port != null) {
            port.bindBehavior(beh);
            return port.getGuard();
        }
        return null;
    }

    /**
     * Eliminates the association of a behavior to a guard that handles a type
     * of event.
     *
     * @param beh Behavior associate.
     * @param evType Type of event.
     * @deprecated No longer it is used in new projects.
     */
    final public synchronized void unregisterGuard(BehaviorBESA beh, String evType) {
        PortBESA port = channel.findPort(evType);
        if (port != null) {
            port.unbindBehavior(beh);                                           //Detachs a behavior to a guard that handles an event type.
        }
    }

    /**
     * Indicates if the agent is alive or not.
     *
     * @return true if the agent is alive ; false otherwise.
     */
    final public synchronized Boolean isAlive() {
        return alive;
    }

    /**
     * Obtains the BESA channel associate to the agent.
     *
     * @return ChannelBESA associated to the agent.
     */
    final public ChannelBESA getChannel() {
        return channel;
    }

    /**
     * Sends a notify so that other threads can activate.
     */
    final public synchronized void signal() {
        this.notify();
    }

    /**
     * Waits to the channel is ready.
     */
    public synchronized void waitChannelReady() throws KernelAgentExceptionBESA {
        try {
            ReportBESA.trace("Before of waitChannelReady.");
            this.wait();
        } catch (InterruptedException ie) {
            ReportBESA.error("Happened an error on wait channel ready:" + ie.toString());
            throw new KernelAgentExceptionBESA("Happened an error on wait channel ready:" + ie.toString());
        }
    }

    /**
     * Initializes the local BESA container, invoking to the variable singleton.
     *
     * @param adm BESA container to assign.
     */
    public static void initAdmLocal(AdmBESA adm) {
        if (adm == null) {
            adm = adm;                                                          //Starts static variable singleton.
        }
    }

    /**
     * Returns a reference to the local BESA container.
     *
     * @return Local BESA container.
     */
    final public AdmBESA getAdmLocal() {
        return adm;
    }

    /**
     * Builds the internal agent structure.
     */
    public void init() throws KernelAgentExceptionBESA {
        try {
            ReportBESA.trace("Starts the setup of the agent: " + alias);
            structAgent.buildAgentStruct(this);
        } catch (Exception e) {
            ReportBESA.error("Happened an error on setup the agent: " + alias + ", message: " + e.toString());
            throw new KernelAgentExceptionBESA("Happened an error on setup the agent: " + alias + ", message: " + e.toString());
        }
    }

    /**
     * Verifies if the password given for the agent is valid.
     *
     * @param passwd Password of the agent to validate.
     * @return true if is the agent key; false otherwise.
     */
    public boolean verifyPasswd(double passwd) {
        if (Math.abs(this.passwd - passwd) < 0.00001) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Blocks the channel in logical way.
     */
    public synchronized void waitChannel() throws KernelAgentExceptionBESA {
        try {
            ReportBESA.trace("Before of waitChannel.");
            this.getChannel().waitlogicChannel();
            ReportBESA.trace("After of waitChannel.");
        } catch (Exception ie) {
            ReportBESA.error("Happened an error on wait channel:" + ie.toString());
            throw new KernelAgentExceptionBESA("Happened an error on wait channel:" + ie.toString());
        }
    }

    /**
     * Returns the agent internal structure.
     *
     * @return Description of the agent internal structure.
     */
    public StructBESA getStructAgent() {
        return structAgent;
    }

    /**
     * Sets the state agent specific.
     *
     * @param stateAgent State agent specific.
     */
    public void setReferenceState(Object stateAgent) {
        stateAgentSpecific = stateAgent;
    }

    /**
     * Gets the state agent specific.
     *
     * @return Returns State agent specific.
     */
    public Object getStateAgentSpecific() {
        return stateAgentSpecific;
    }

    /**
     * Gets the behaviors list.
     *
     * @return Behaviors list.
     */
    public ArrayList<BehaviorBESA> getBehaviors() {
        return behaviors;
    }
    
    /**     
     * Returns the agent password.
     * 
     * @return Internal state of the agent, which inherits of StateBESA.
     */
    public double getPassword() {
        return passwd;
    }
}
