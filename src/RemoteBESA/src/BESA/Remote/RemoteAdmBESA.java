/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Remote;

import BESA.Config.ConfigBESA;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AGENTSTATE;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.System.Directory.AdmHandlerBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Local.Directory.AgLocalHandlerBESA;
import BESA.Local.LocalAdmBESA;
import BESA.Log.ReportBESA;
import BESA.Remote.Directory.*;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class RemoteAdmBESA extends LocalAdmBESA {

    /**
     * Constant to define the type ALIVE container at a multicast message.
     */
    public static final String MSG_MULTICAST_ADM_ALIVE = "ALIVE";
    /**
     * Constant to define the type KILL container at a multicast message.
     */
    public static final String MSG_MULTICAST_ADM_KILL = "KILL";
    /**
     * Constant to define the type CREATE container at a multicast message.
     */
    public static final String MSG_MULTICAST_AGENT_CREATE = "CREATE";
    /**
     * Constant to define the type DESTROY container at a multicast message.
     */
    public static final String MSG_MULTICAST_AGENT_DESTROY = "DESTROY";
    /**
     * Constant to separate data in a multicast message.
     */
    public static final String MULTICAST_DELIMITER = "%";
    /**
     * Constant to define the type MOVE container at a multicast message.
     */
    public static final String MSG_MULTICAST_AGENT_MOVE = "MOVE";
    /**
     * Constant to define the type SET_STATE_MOVE container at a multicast
     * message.
     */
    public static final String MSG_MULTICAST_SET_STATE_AGENT_MOVE = "SET_STATE_MOVE";
    /**
     * Constant to define the type SET_STATE_ALIVE container at a multicast
     * message.
     */
    public static final String MSG_MULTICAST_SET_STATE_AGENT_ALIVE = "SET_STATE_ALIVE";
    /**
     * Multicast port.
     */
    protected PingThread ping;
    /**
     * Managing the reception and processing of multicast messages.
     */
    protected PongThread pong;
    /**
     * Port for multicast socket.
     */
    protected int multicastPort;
    /**
     * Multicast socket.
     */
    protected MulticastSocket multicastSocket;
    /**
     * Inet address for multicast.
     */
    protected InetAddress multicastInetAddr;
    /**
     * Socket ping-pong.
     */
    protected Socket socketPingPong = null;
    /**
     *
     */
    protected RemoteDirectoryBESA remoteDirectory;

    /**
     * Creates a unique instance for un distributed administrator.
     *
     * @param admAlias Alias/name of the admnistrador.
     * @param passwd Security key, it is necessary to be able to create or
     * destroy the administrator.
     * @param ipRmiRegistry The IP address of the machine that contains the
     * administrator to be registered.
     * @param portRmiRegistry The port of the machine that contains the
     * administrator to be registered.
     * @param multicastAddr Multicast address used by the administrators in the
     * same SMA.
     * @param multicastPort Multicast port used by the administrators in the
     * same SMA.
     * @param centralized Indicates if the container is going to be executed in
     * a single machine or if it is going to be used in a distributed way.
     * @return Reference to the unique/singleton instance of the local
     * administrator.
     */
    public RemoteAdmBESA(String admAlias, double passwd, String ipRmiRegistry, int portRmiRegistry, String multicastAddr, int multicastPort, boolean centralized) throws ExceptionBESA {
        remoteDirectory = new RemoteDirectoryBESA();
        ConfigBESA configBESA = new ConfigBESA();
        setConfig(configBESA);
        String messageException = new String("");
        try {
            this.centralized = centralized;                                     //Adds the centralized value.
            this.config = new ConfigBESA();                                     //Initializes the configuration object.
            messageException = new String("");
            //----------------------------------------------------------------//
            // Initializes the data tables.                                   //
            //----------------------------------------------------------------//
            this.passwd = passwd;                                               //Adds the password value.
            this.alive = true;                                                  //Indicates that container is alive.
            this.admHandler = new RemoteAdmHandlerBESA("", admAlias, ipRmiRegistry, portRmiRegistry); //Initializes the administrator handler.
            //TODO: JAIRO this.activateCheckpoint();
            messageException = new String("");
            if (!this.centralized) {                                            //Cheks if is not centralized.
                new DistributedInitBESA(this, (RemoteAdmHandlerBESA) this.admHandler, portRmiRegistry, multicastAddr, multicastPort); //Initializes the administrator handler.
            } //End if.
        } catch (Exception ioe) { //End try.
            throw new ExceptionBESA(messageException);
        } //End catch.
    }

    /**
     * Creates a unique instance for the local administrator in a JVM.
     *
     * @param admAlias Alias/name of the admnistrador.
     * @param passwd Security key, it is necessary to be able to create or
     * destroy the administrator.
     * @param ipAddress The IP address of the machine that contains the
     * administrator to be registered.
     * @param rmiPort The port of the machine that contains the administrator to
     * be registered.
     * @param multicastAddr Multicast address used by the administrators in the
     * same SMA.
     * @param multicastPort Multicast port used by the administrators in the
     * same SMA.
     * @param centralized Indicates if the container is going to be executed in
     * a single machine or if it is going to be used in a distributed way.
     * @return Reference to the unique/singleton instance of the local
     * administrator.
     * @throws java.lang.Exception If happens an error in the administrator
     * initialization.
     */
    public RemoteAdmBESA(ConfigBESA configBESA) throws DistributedExceptionBESA {
        //super(configBESA);
        remoteDirectory = new RemoteDirectoryBESA();
        this.config = configBESA;
        this.centralized = false;                                           //Adds the centralized value.
        //----------------------------------------------------------------//
        // Initializes the data tables.                                   //
        //----------------------------------------------------------------//        
        this.passwd = configBESA.getPasswordContainer();                    //Adds the password value.
        this.alive = true;                                                  //Indicates that container is alive.

        this.admHandler = new RemoteAdmHandlerBESA(configBESA);//Initializes the administrator handler.

        new DistributedInitBESA(this, (RemoteAdmHandlerBESA) this.admHandler, configBESA.getRmiPort(), configBESA.getMcaddress(), configBESA.getMcport()); //Initializes the administrator handler.
    }

    /**
     * This registers a remote administrator in another JVM - if not registered
     * is created handler and the lookup.
     *
     * @param admAlias Alias/name of the admnistrador.
     * @param passwd Security key, it is necessary to be able to create or
     * destroy the administrator.
     * @param ipRmiRegistry The IP address of the machine that contains the
     * administrator to be registered.
     * @param portRmiRegistry The port of the machine that contains the
     * administrator to be registered.
     * @return Adm remote handler BESA.
     * @throws Exception Exception in lookup.
     */
    public RemoteAdmHandlerBESA registerRemoteAdm(String admId, String admAlias, String ipRmiRegistry, int portRmiRegistry) throws SystemExceptionBESA {
        RemoteAdmHandlerBESA adm = remoteDirectory.getAdmRemoteHandlerbyId(admId);
        if (adm == null) {                                                      //Validates the table remote adm.
            RemoteAdmHandlerBESA admHand = null;
            try {
                admHand = new RemoteAdmHandlerBESA(admId, admAlias, ipRmiRegistry, portRmiRegistry); //Alias does not exist, initializes the administrator handler.
            } catch (Exception ex) {
                ReportBESA.error("Couldn't register the remote administrator " + admAlias + ": " + ex.toString());
                throw new SystemExceptionBESA("Couldn't register the remote administrator " + admAlias + ": " + ex.toString());
            }
            remoteDirectory.registerAdmRemoteHandler(admId, admHand, admAlias);
            return admHand;
        } else { //There is an administrator with the same alias.
            return adm;
        } //End else.
    }

    /**
     * Registering a remote administrator present in another JVM - if not
     * registered, is created handler and lookup.
     *
     * @param admAlias Alias/name of the admnistrador.
     * @return null if this is a problem -> exception in lookup || there is
     * already a local administrator with the same alias and a different name
     * RMI (possible ambiguity by the same names).
     * @throws Exception Exception in lookup.
     */
    public AdmHandlerBESA getAdmByAlias(String admAlias) {
        if (admAlias.equalsIgnoreCase(this.admHandler.getAlias())) {            //Checks if there is no adm with the same alias.
            return this.admHandler;
        } //End if.
        return remoteDirectory.getAdmByAlias(admAlias);
    }

    /**
     * Registering a remote administrator present in another JVM - if not
     * registered, is created handler and lookup.
     *
     * @param admId Administrator ID.
     * @return null if this is a problem -> exception in lookup || there is
     * already a local administrator with the same alias and a different name
     * RMI (possible ambiguity by the same names).
     * @throws Exception Exception in lookup.
     */
    public AdmHandlerBESA getAdmById(String admId) {
        if (admId.equalsIgnoreCase(this.admHandler.getAdmId())) {               //Checks if there is no adm with the same alias.
            return this.admHandler;
        } //End if.
        return remoteDirectory.getAdmById(admId);
    }

    /**
     * Gets alias Adm by ID.
     *
     * @param admId Administrator ID.
     * @return Adm alias.
     * @throws Exception NullPionterException because the adm does not find by
     * admId in the table.
     */
    public String getAliasAdmById(String admId) {
        return remoteDirectory.getAliasAdmById(admId);
    }

    /**
     * Unregister remote Adm from serv Id Table.
     *
     * @param admAlias Adm alias.
     */
    public void unregisterRemoteAdm(String admAlias) throws SystemExceptionBESA {
        remoteDirectory.unregisterRemoteAdm(admAlias);
    }

    /**
     * Registers remote agent.
     *
     * @param agAlias Agent alias.
     * @param agId Agent ID.
     * @param admId ID of System Administrator.
     */
    public synchronized void registerRemoteAgent(String agAlias, String agId, String admId) throws DistributedDirectoryExceptionBESA {
        AgRemoteHandlerBESA agh = new AgRemoteHandlerBESA((RemoteAdmHandlerBESA) this.getAdmById(admId), agAlias, agId); //Creates agent handler.
        super.registerAgent(agId, agh, agAlias);
    }

    @Override
    public void unregisterAgent(String agId) throws SystemExceptionBESA {
        super.unregisterAgent(agId);
        //--------------------------------------------------------------------//
        // Informs a other containers of the agent deat.                      //
        //--------------------------------------------------------------------//
        /*try {
            this.ping.updateAgentbyMulticast(MSG_MULTICAST_AGENT_DESTROY, agId,null); //It removes the reference in the other containers.
        } catch (SystemExceptionBESA ex) {
            Logger.getLogger(RemoteAdmBESA.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    /**
     * Se elimina las referencias de una agente remoto de las tabals de agnetes
     */
    /**
     * en este paso se debe actualizar el handler remoto
     *
     * @param agAlias
     * @param aliasAdmRemote
     */
//     public void updateUnregisterRemoteAgent(String agId, AdmRemoteHandler admRH) {
    public void updateUnregisterRemoteAgent(String agAlias, String aliasAdmRemote) throws SystemExceptionBESA {
        try {
            // Eliminar agente de tabla de agentes
            //	        AgHandler ahp =  this.getHandlerByAid(agId);
            AgHandlerBESA ahp = this.getHandlerByAlias(agAlias);
            if (ahp instanceof AgRemoteHandlerBESA) {
                RemoteAdmHandlerBESA admRH = (RemoteAdmHandlerBESA) this.getAdmByAlias(aliasAdmRemote);
                ((AgRemoteHandlerBESA) ahp).setAdmRemoteHandler(admRH);
            }
        } catch (Exception ioe) {
            ReportBESA.error("Couldn't update unregister remote agent " + aliasAdmRemote + ": " + ioe.toString());
            throw new SystemExceptionBESA("Couldn't update unregister remote agent " + aliasAdmRemote + ": " + ioe.toString());
        }
        //directoryManager.unbindAgent(agId);
        //      agAliasTable.remove(getAliasByAid(agId));
        // agIdTable.remove(agId);
    }

    @Override
    public void kill(double containerPassword) throws ExceptionBESA {
        //------------------------------------------------------------//
        // Kills the distribuit components and sends to other         //
        // administrator that this container is killed.               //
        //------------------------------------------------------------//
        //killCurrentContainer();
        if (!this.centralized) {
            try {
                ping.sendMulticast(MSG_MULTICAST_ADM_KILL); //Informs to other containers.
            } catch (SystemExceptionBESA ex) {
                Logger.getLogger(RemoteAdmBESA.class.getName()).log(Level.SEVERE, null, ex);
            }
            ping = null;
            pong = null;
            multicastPort = 0;
        } else {
            admHandler = null;
        }
    }

    @Override
    public void publicagent(String alias, String agId) throws SystemExceptionBESA {
        this.ping.updateAgentbyMulticast(MSG_MULTICAST_AGENT_CREATE, agId, alias);
    }

    /**
     * 18 - Ene - 2006
     *
     * @param ping
     */
    public void setPing(PingThread ping) {
        this.ping = ping;
    }

    /**
     * 18 - Ene - 2006
     *
     * @param pong
     */
    public void setPong(PongThread pong) {
        this.pong = pong;
    }

    /**
     * 18 - Ene - 2006
     *
     * @return
     */
    public PingThread getPing() {
        return this.ping;
    }

    /**
     * 18 - Ene - 2006
     *
     * @return
     */
    public PongThread getPong() {
        return this.pong;
    }

    /**
     *
     * @return
     */
    public int getMulticastPort() {
        return this.multicastPort;
    }

    /**
     * 18 - Ene - 2006
     *
     * @param port
     */
    public void setMulticastPort(int port) {
        this.multicastPort = port;
    }

    /**
     *
     * @return
     */
    public InetAddress getMulticastInetAddr() {
        return this.multicastInetAddr;
    }

    /**
     * 18 - Ene - 2006
     *
     * @param addr
     */
    public void setMulticastInetAddr(InetAddress addr) {
        this.multicastInetAddr = addr;
    }

    /**
     *
     * @return
     */
    public MulticastSocket getMulticastSocket() {
        return this.multicastSocket;
    }

    /**
     * 18 - Ene - 2006
     *
     * @param ms
     */
    public void setMulticastSocket(MulticastSocket ms) {
        this.multicastSocket = ms;
    }

    /**
     *
     * @return
     */
    public Socket getSocketPingPong() {
        return socketPingPong;
    }

    /**
     *
     * @param socketPingPong
     */
    public void setSocketPingPong(Socket socketPingPong) {
        this.socketPingPong = socketPingPong;
    }

    @Override
    public void killAgent(String agId, double agentPassword) throws ExceptionBESA {
        AgHandlerBESA agh = (AgHandlerBESA) this.getHandlerByAid(agId);                 //Gets the agent handler.
        if (agh instanceof AgLocalHandlerBESA) {
            super.killAgent(agId, agentPassword);
            try {
                this.ping.updateAgentbyMulticast(MSG_MULTICAST_AGENT_DESTROY, agId, agh.getAlias()); //It removes the reference in the other containers.
            } catch (SystemExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }
        if (agh instanceof AgRemoteHandlerBESA) {
            try {
                ((AgRemoteHandlerBESA) agh).getAdm().getAdmRemote().killRemoteAgent(agId, agentPassword);
            } catch (RemoteException ex) {
                ReportBESA.error(ex);
            }
        }
    }

    /**
     * Updates the agent dictory. This method is call the remote way.
     *
     * @param admId Administrator ID of the remote invoquer.
     * @param agIdList Remote agent ID list.
     * @param aliasList Remote alias list.
     */
    public void updateAgentDirectory(String admId, ArrayList agIdList, ArrayList aliasList) throws DistributedDirectoryExceptionBESA {
        while (!agIdList.isEmpty()) {
            String agId = (String) agIdList.remove(0);
            String agAlias = (String) aliasList.remove(0);
            if (!admId.equalsIgnoreCase(this.admHandler.getAdmId())) {
                this.registerRemoteAgent(agAlias, agId, admId);
            }
        }
    }

    public void importAgentDirectory(ArrayList<String> adIdList, ArrayList<String> aliasList) throws RemoteException {
        try {
            Enumeration keys = localDirectory.getIDs();
            while (keys.hasMoreElements()) {
//se quita ojo		    	AgHandler agh = ((AgHandlerBESA)this.admLocal.getAgIdTable().get(keys.nextElement())).obtainAgLocal();

                AgHandlerBESA agh = localDirectory.getAgHandlerBESAByID((String) keys.nextElement());
                //AgHandlerBESA agh = ((AgHandlerBESA) AdmRemoteBESA.admLocal.getAgIdTable().get(keys.nextElement()));
                if (agh instanceof AgLocalHandlerBESA) {
                    if (agh != null) {
                        //((AdmRemoteHandler)(this.admLocal.getAdmAliasTable().get(admAlias))).getAdmRemote().registerRemoteAgents (agh.getAlias(),agh.getAgId(),this.admLocal.getAdmHandler().getAlias());
                        adIdList.add(agh.getAgId());
                        aliasList.add(agh.getAlias());
                        //ojo trae los locales y los remotos, que pueden ser locales mios
//					((AdmRemoteHandler)(this.admLocal.getAdmAliasTable().get(admAlias))).getAdmRemote().registerRemoteAgents (agh.getAlias(),agh.getAgId(),this.admLocal.getAdmHandler().getAlias());
                    }
                }
            }
        } catch (Exception e) {
            ReportBESA.error("Couldn't import the agent into local directories: " + e.toString());
            throw new RemoteException("Couldn't import the agent into local directories: " + e.toString());
        }
    }

    /**
     * This method made a CheckPoint
     *
     * @autor Jairo Serrano
     */
    @Override
    public synchronized void executeCheckpoint() {

        Enumeration idList = this.getIdList();
        AgentBESA ag = null;
        // SOLO SALVAR LOS QUE ESTÁN EN ESTE CONTENEDOR
        //System.out.println("Entrando a checkpoint");
        while (idList.hasMoreElements()) {
            try {
                String agId = idList.nextElement().toString();
                AgHandlerBESA agh = (AgHandlerBESA) this.getHandlerByAid(agId);
                //System.out.println(agh.getClass().getName());
                //System.out.println("Entrando a backup " + agh.getAlias());

                if (agh instanceof AgLocalHandlerBESA) {
                    //@TODO: CAMBIAR A CHECKPOINT
                    agh.setState(AGENTSTATE.MOVE);
                    ag = agh.getAg();
                    ag.saveAgent();
                    agh.setState(AGENTSTATE.ALIVE);
                    agh.notifyMove();
                    //System.out.println("El agente existe " + ag.getAid());
                    //System.out.println("El agente existe " + ag.getAlias());
                } else {
                    //System.out.println("El agente NO existe " + agId);
                }

            } catch (ExceptionBESA ex) {
                System.out.println("El agente NO existe EXCEPC");
                //Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * This method activate CheckPoint System
     *
     * @autor Jairo Serrano
     */
    @Override
    public synchronized void activateCheckpoint() {
        /*String EntornoBESA = System.getenv().get("BESA_CHECKPOINT");
        switch (EntornoBESA) {
            case "RECURRENT":
                System.out.println("activando checkpoint Recurrente");
                this.checkpointActive = true;
                this.timerCheckpoint = new Timer();
                this.timerCheckpoint.schedule(new recurrentCheckpoint(), this.config.getCheckpointTime(), this.config.getCheckpointTime());
                break;
            case "CLEAN":
                System.out.println("Limpiando checkpoints");
                break;
            default:
                System.out.println("Ejecución sin Checkpoints");
                break;
        }*/
        System.out.println("activando checkpoint Recurrente");
        this.checkpointActive = true;
        this.timerCheckpoint = new Timer();
        this.timerCheckpoint.schedule(new recurrentCheckpoint(), this.config.getCheckpointTime(), this.config.getCheckpointTime());
    }

    /**
     * This class execute Recurrent CheckPoint
     *
     * @autor Jairo Serrano
     */
    class recurrentCheckpoint extends TimerTask {

        @Override
        public void run() {
            System.out.println("Iniciando Checkpoint");
            executeCheckpoint();
            System.out.println("Cerrando Checkpoint");
        }
    }

    /**
     * This method deactivate CheckPoint System
     *
     * @autor Jairo Serrano
     */
    @Override
    public synchronized void deactivateCheckpoint() {
        this.checkpointActive = false;
        this.timerCheckpoint.cancel();
    }

    /**
     * Moves the agent of a container to another container.
     *
     * @param agId agent ID.
     * @param aliasDestinationAdmBESA Destination alias.
     * @param passwdAgent Agent password.
     * @throws Exception
     */
    @Override
    public synchronized void moveAgent(String alias, String aliasDestinationAdmBESA, double passwdAgent) throws ExceptionBESA {
        String agId = super.searchAidByAlias(alias);
        AgHandlerBESA agh = (AgHandlerBESA) this.getHandlerByAlias(alias);                 //Gets agent handler.
        if (!this.getAdmHandler().getAlias().equals(aliasDestinationAdmBESA)) { //Checks if the alias is not equals to the destination alias.
            if (agh instanceof AgLocalHandlerBESA) {
                AgentBESA ag = agh.getAg();                                 //Gets agent.
                String nameClassAgent = ag.getClass().getName();            //Gets the class name.
                if (ag != null && ag.verifyPasswd(passwdAgent)) {           //Checks if the agent exist and the passwor is correct.
                    agh.setState(AGENTSTATE.MOVE);
                    ReportBESA.trace("Bloquea handlers debido a movimiento");
                    ag.move(passwdAgent);                                   //The agent is moved.
                    RemoteAdmHandlerBESA remoteAdmHandler = (RemoteAdmHandlerBESA) (this.getAdmByAlias(aliasDestinationAdmBESA));//Movement Invokes on the target container.
                    try {
                        remoteAdmHandler.getAdmRemote().moveAgentReceive(ag.getAlias(), ag.getState(), ag.getStructAgent(), passwdAgent, nameClassAgent);//Informs the other containers.
                    } catch (RemoteException ex) {
                        throw new ExceptionBESA(ex.toString());
                    }
                    super.unregisterAgent(agId);//Unregister the agent of the tables from a local agent registration.
                    ReportBESA.trace("Termina Bloqueo de  handlers debido a movimiento");
                    registerRemoteAgent(alias, agId, remoteAdmHandler.getAdmId());
                    agh.notifyMove();
                    return;
                }
            }
            if (agh instanceof AgRemoteHandlerBESA) {
                RemoteAdmHandlerBESA srcRemoteAdmHandler = ((AgRemoteHandlerBESA) agh).getAdm();//Gets the remote agent handler.
                try {
                    srcRemoteAdmHandler.getAdmRemote().moveAgentSend(alias, aliasDestinationAdmBESA, passwdAgent);//The agent is moved.
                    super.unregisterAgent(agId);
                    RemoteAdmHandlerBESA dstRemoteAdmHandler = (RemoteAdmHandlerBESA) (this.getAdmByAlias(aliasDestinationAdmBESA));//Movement Invokes on the target container.
                    registerRemoteAgent(alias, agId, dstRemoteAdmHandler.getAdmId());
                } catch (RemoteException ex) {
                    throw new ExceptionBESA(ex.toString());
                }
            }
        }
    }

    /**
     *
     * @param idList
     * @param aliasList
     */
    public void geLocalDirectoryList(ArrayList<String> idList, ArrayList<String> aliasList) {
        Enumeration keys = super.getIdList();

        while (keys.hasMoreElements()) {
            //quito    AgHandler agh = ((AgHandlerBESA)(this.getAgIdTable().get(keys.nextElement()))).obtainAgLocal();
            //AgHandlerBESA agh = ((AgHandlerBESA) (this.getAgIdTable().get(keys.nextElement())));

            AgHandlerBESA agh = null;
            try {
                agh = super.getHandlerByAid((String) keys.nextElement());
            } catch (ExceptionBESA ex) {
                Logger.getLogger(RemoteAdmBESA.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (agh instanceof AgLocalHandlerBESA) {
                if (agh != null) {
                    idList.add(agh.getAgId());
                    aliasList.add(agh.getAlias());
                }
            }
        }
    }

    /**
     *
     * @param msgType
     * @param agId
     */
    public void updateStateAgent(String msgType, String agId) throws SystemExceptionBESA {
        // 0. msgType y 1. agId
        AgHandlerBESA agh = null;
        try {
            agh = this.getHandlerByAid(agId);
        } catch (ExceptionBESA ex) {
            Logger.getLogger(RemoteAdmBESA.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Se actualiza en todos los contenedores el estado a move o kill, la idea es evitar
        // mientras se mueve que le envien eventos
        if (msgType.equalsIgnoreCase(AGENTSTATE.MOVE.name())) {
            agh.setState(AGENTSTATE.MOVE);
        } else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_AGENT_DESTROY)) {
            agh.setState(AGENTSTATE.KILL);
        } else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_ALIVE)) {
            agh.setState(AGENTSTATE.ALIVE);
        }
    }

    @Override
    public String registerAgent(AgentBESA ag, String id, String alias) {
        if (id == null) {
            String agId = this.generateAgId(alias);                               //Generates the agent ID. 
            return super.registerAgent(ag, agId, alias);
        }
        return super.registerAgent(ag, id, alias);
    }

    @Override
    public Enumeration<String> getAdmAliasList() {
        return remoteDirectory.getAdmAliasList();
    }
}
