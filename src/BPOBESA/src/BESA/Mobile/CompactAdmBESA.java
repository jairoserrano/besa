package BESA.Mobile;

import BESA.Config.ConfigBESA;
import BESA.ExceptionBESA;
import BESA.Mobile.BAPLocator.BAPLocator;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Local.LocalAdmBESA;
import BESA.Log.ReportBESA;
import BESA.Mobile.BAPLocator.IBAPLocator;
import BESA.Mobile.CommunicationChannel.BaseClasses.ICommunicationChannelBuilderBPO;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.CommunicationChannel.Types.Socket.SocketCommunicationChannelBuilderBPO;
import BESA.Mobile.ConnectionsAdministrator.ContainerConnectionInformationBPO;
import BESA.Mobile.Directory.AgHandlerBPO;
import BESA.Mobile.Exceptions.ExceptionBESACommunicationChannelFailedBPO;
import BESA.Mobile.Message.ACKAttachedResponse.LookupHandlerResponseBPO;
import BESA.Mobile.Message.AdministrativeMessageBPO;
import BESA.Mobile.Message.MessageFactoryBPO;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author Andrea
 */
public class CompactAdmBESA extends LocalAdmBESA implements IAdmBPO {

    private HandlerCache handlerCache;
    private String masterContainerID;
    private IBAPLocator bapLocator;

    public CompactAdmBESA(ConfigBESA configBESA) throws SystemExceptionBESA {
        super(configBESA);


        URL url = null;
        URL GREETINGIMPLSERVICE_WSDL_LOCATION;
        String IP = configBESA.getBaplocatoradd();
        String PORT = ""+ configBESA.getBloport();
        try {
            URL baseUrl;
            baseUrl = BESA.Mobile.BAPLocator.BAPLocator.class.getResource(".");
            url = new URL(baseUrl, "http://" + IP + ":" + PORT + "/besa/baplocator?wsdl");
        } catch (MalformedURLException e) {
            ReportBESA.error("Failed to create URL for the wsdl Location: 'http://" + IP + ":" + PORT + "/besa/baplocator?wsdl', retrying as a local file");
        }
        GREETINGIMPLSERVICE_WSDL_LOCATION = url;


        
        bapLocator = (IBAPLocator) new BAPLocator(GREETINGIMPLSERVICE_WSDL_LOCATION).getBAPLocatorPort();
        initCommunicationChannels();
        handlerCache = new HandlerCache();
        setMasterContainerID();
    }

    public CompactAdmBESA() throws SystemExceptionBESA {
//        bapLocator = (IBAPLocator) new BAPLocator().getBAPLocatorPort();
        initCommunicationChannels();
        handlerCache = new HandlerCache();
        setMasterContainerID();
    }

    private void initCommunicationChannels() throws SystemExceptionBESA {
        addCommunicationChannelType(new SocketCommunicationChannelBuilderBPO());
        try {
            addServerListener(this.config.getBpoPort(), SocketCommunicationChannelBuilderBPO.NAME);
        } catch (ExceptionBESACommunicationChannelFailedBPO ex) {
            throw new SystemExceptionBESA(ex.getMessage());
        }
    }
    //----------------------POSTOFFICE----------------------------------//

    @Override
    public IPostOfficeBPO getPostOffice() { //Returns interface
        return PostOfficeBPOCE.get();
    }

    private PostOfficeBPOCE getPostOfficeForAdm() {
        return PostOfficeBPOCE.get();
    }

    //-----------------MASTER INFORMATION-----------------------------//
    /*
     * Adds the master's connection information to the connections administrator
     */
    public final void setMasterContainerID() {
        //Get new master's info
        String masterInfoFromBAP = "ASMA_B/127.0.0.1:8083";
//        String masterInfoFromREALBAPLocator="";
//        try {
//            masterInfoFromREALBAPLocator = bapLocator.getMasterLocation(/*java.net.InetAddress.getLocalHost().getHostAddress()*/"1.1.1.1");
//        } catch (UnknownHostException ex) {
//            //aksdnf
//        }
        
        masterInfoFromBAP = bapLocator.getMasterLocation("1.1.1.1");
        
        
        System.out.println("MASTER INFORMATION " + masterInfoFromBAP);
//        System.out.println("MASTER INFORMATION FROM BAP LOCATOR :::: " + masterInfoFromREALBAPLocator);

        String[] split = masterInfoFromBAP.split("/");
        masterContainerID = split[0];
        ContainerConnectionInformationBPO info = new ContainerConnectionInformationBPO();
        info.setContainerLocation(split[1]); //IP:Port
        info.setConnectionType(SocketCommunicationChannelBuilderBPO.NAME);
        //Add connection information for the new masters to the connections administrator
        getPostOfficeForAdm().getConnectionsAdministrator().addContainerConnectionInformation(masterContainerID, info);
    }

    public String getMasterContainerID() {
        return masterContainerID;
    }

    //-----------------CONNECTION INFORMATION-----------------------------//
    @Override
    public ContainerConnectionInformationBPO getConnectionInfoFor(String containerID) throws ExceptionBESA {
        System.out.println("GETTING CONNECTION INFO FOR: " + containerID);
        //Always return master's location
        return getPostOfficeForAdm().getConnectionsAdministrator().getContainerConnectionInformation(masterContainerID);
    }

    //--------------------------------------------------------//
    @Override
    public AgHandlerBESA getHandlerByAid(String agId) throws ExceptionBESA {
        try {
            return super.getHandlerByAid(agId);
        } catch (ExceptionBESA e) {
            //Verify the handler cache
            AgHandlerBESA agHandler = handlerCache.getHandlerByAid(agId); //Revisar - que sucede cuando el handler se encuentra desactualizado?
            if (agHandler == null) { //If handler is not in the cache
                try {
                    //Ask Master
                    System.out.println("ENTRO AL SEND MESSAGE DOESAGENTEXIST BY AGID ");
                    AdministrativeMessageBPO message = MessageFactoryBPO.createDoesAgentExistMessage(agId, null);
                    sendMessageToMaster(message);
                    //Retrieve Alias from ACK
                    String agAlias = ((LookupHandlerResponseBPO) message.getACK().getAttachedResponse()).getAgAlias();
                    System.out.println("ALIAS DEL HANDLER QUE LOOKUP RESPONSE: " + agAlias);
                    agHandler = new AgHandlerBPO(agId, agAlias, getMasterContainerID());
                    handlerCache.add(agHandler);
                } catch (ExceptionBESA exp) {
                    throw e;
                }
            }
            return agHandler;
        }
    }

    @Override
    public AgHandlerBESA getHandlerByAlias(String agAlias) throws ExceptionBESA {
        try {
            return super.getHandlerByAlias(agAlias);
        } catch (ExceptionBESA e) {
            if (agAlias.contains("PostManBPO_")) { //In the CompactEdition, only local Postman can be used
                throw e;
            }
            //Verify the handler cache
            AgHandlerBESA agHandler = handlerCache.getHandlerByAlias(agAlias);
            if (agHandler == null) {//If handler is not in the cache
                try {
                    //Ask Master
                    System.out.println("ENTRO AL SEND MESSAGE DOESAGENTEXIST BY AGALIAS ");
                    AdministrativeMessageBPO message = MessageFactoryBPO.createDoesAgentExistMessage(null, agAlias);
                    sendMessageToMaster(message);
                    //Retrieve AgId from ACK
                    String agId = ((LookupHandlerResponseBPO) message.getACK().getAttachedResponse()).getAgId();
                    System.out.println("ENTRO A ID DEL HANDLER QUE LOOKUP RESPONSE: " + agId);
                    agHandler = new AgHandlerBPO(agId, agAlias, getMasterContainerID());
                    handlerCache.add(agHandler);
                } catch (ExceptionBESA exp) {
                    throw e;
                }
            }
            return agHandler;
        }
    }

    @Override
    public void registerAgent(String agId, AgHandlerBESA agh, String agAlias){
        super.registerAgent(agId, agh, agAlias);
        if (!agAlias.contains("PostManBPO_")) {  //Local PostMan in the compact edition are not registered in the master for security reasons
            try {
                sendMessageToMaster(MessageFactoryBPO.createAgentCreatedMessage(agId, agAlias));
            } catch (ExceptionBESA ex) {
                System.out.println("Recorcholis"); //REVISAR: El registerAgent deberia tener un throws Exception
            }
        }
    }

    @Override
    public void killAgent(String agId, double agentPassword) throws ExceptionBESA {
        AgHandlerBESA agh;
        ExceptionBESA exception = null;
        try {
            agh = (AgHandlerBESA) super.getHandlerByAid(agId); //Lookup the agent in local white pages
        } catch (ExceptionBESA ex) {
            exception = ex;
            agh = handlerCache.getHandlerByAid(agId);
        }
        if (agh != null) { //If agent handler found 
            if (agh instanceof AgHandlerBPO) {
                System.out.println("KILL AGENT FROM DE:" + agh.getAlias() + "\n");
                sendMessageToMaster(MessageFactoryBPO.createAgentKilledMessage(agId, agentPassword));
            } else if (agh.getAg() != null) { //If a reference to the agent exists locally
                System.out.println("KILL LOCAL AGENT:" + agh.getAlias() + "\n");
                String agAlias = agh.getAlias();
                super.killAgent(agId, agentPassword);
                if (!doesAgentExist(agAlias)) { //Verify if agent was killed
                    if (!agAlias.contains("PostManBPO_")) { //If the agent is not a postman
                        sendMessageToMaster(MessageFactoryBPO.createAgentKilledMessage(agId, agentPassword));
                    }
                } else {
                    throw new ExceptionBESA("Could not kill agent");
                }
            } else {
                super.erase(agh);
            }
        } else { //if agent handler not found
            try { //Send message to master anyway
                sendMessageToMaster(MessageFactoryBPO.createAgentKilledMessage(agId, agentPassword));
            } catch (ExceptionBESA ex) {
                throw exception;
            }
        }
    }

    @Override
    public synchronized void addService(String servId) {
        super.addService(servId);
        try { //Inform Master
            sendMessageToMaster(MessageFactoryBPO.createAddServiceMessage(servId));
        } catch (ExceptionBESA ex) {
            //REVISAR: Que hago?
        }
    }

    @Override
    public synchronized void addService(String servId, ArrayList<String> descriptors) {
        super.addService(servId, descriptors);
        try { //Inform Master
            sendMessageToMaster(MessageFactoryBPO.createAddServiceMessage(servId, descriptors));
        } catch (ExceptionBESA ex) {
            //REVISAR: Que hago?
        }
    }

    @Override
    public synchronized boolean bindService(String agId, String servId) {
        boolean success = super.bindService(agId, servId);
        try { //Inform Master
            sendMessageToMaster(MessageFactoryBPO.createBindServiceMessage(agId, servId));
        } catch (ExceptionBESA ex) {
            success = false;
        }
        return success; //REVISAR: Sería esto correcto si el service si hizo bind pero no se pudo enviar el msg... creo que de tdos mdos si no puede enviar el msg se bloquea hmm bu
    }

    @Override
    public void bindSPServiceInDirectory(String agentId, String directoryServiceName) {
        super.bindSPServiceInDirectory(agentId, directoryServiceName);
        try { //Inform Master
            sendMessageToMaster(MessageFactoryBPO.createBindSPServiceInDirectoryMessage(agentId, directoryServiceName));
        } catch (ExceptionBESA ex) {
            //REVISAR: Que hago?
        }
    }

    private void sendMessageToMaster(AdministrativeMessageBPO message) throws ExceptionBESA {
        getPostOfficeForAdm().sendMessageToLocation(getMasterContainerID(), message);
        waitForMessageACK(getMasterContainerID(), message);
    }

    //--------------------KILL
    @Override
    public void kill(double containerPassword) throws ExceptionBESA {
        if (Math.abs(this.passwd - containerPassword) < 0.0001) {
            //Kill all agents that are not PostMan. 
            //This step must be done before killing the PostMan agent so through the Postman Agent the Master Container 
            //can be informed of the dead of the other agents
            Enumeration<String> agents = localDirectory.getIDs();
            while (agents.hasMoreElements()) {
                String agId = agents.nextElement();
                AgHandlerBESA ah = this.getHandlerByAid(agId);
                if (!ah.getAlias().contains("PostManBPO_")) {
                    killAgent(agId, 77.77); //REVISAR - Aca no tengo password para los agentes ... qué hago?
                }
            }
            getPostOfficeForAdm().shutDown(); //Kills PostOffice including PostMan Agents
            super.kill(containerPassword);
        }
    }

    //SERVICES CONCERNING BPO ------------------------------------//
    private void waitForMessageACK(String remoteContainerID, AdministrativeMessageBPO message) throws ExceptionBESA {
        try {
            while (message.getACK() == null) {
                message.waitForACK(this.config.getSendEventTimeout());
                //Check if message timed out
                if (message.getACK() == null) {
                    PostOfficeBPO.get().messageTimedOut(remoteContainerID, message);
                }
            }
            if (message.getACK().isNegative()) {
                throw new ExceptionBESA(message.getACK().getExceptionMessage());
            }
        } catch (InterruptedException ex) {
            throw new ExceptionBESA("InterruptedException");
        }
    }

    //SERVICES CONCERNING COMMUNICATION CHANNELS ------------------------------------//
    @Override
    public boolean getDeviceSupportsCommunicationByObjects() {
        /*
         * TODO: This information should be read from ConfigBESA and it is used
         * if a certain device cannot communicate using objects through the
         * communication channel and therefore communication should be carried
         * out by strings.
         * 
         * For this version if communication is by string the sendEvent and possibly moveAgent
         * cause problems.
         */

        return true;//Default
    }

    private void addCommunicationChannelType(ICommunicationChannelBuilderBPO communicationChannelBuilder) {
        getPostOfficeForAdm().getConnectionsAdministrator().addCommunicationChannelType(communicationChannelBuilder);
    }
//
//    public final void removeCommunicationChannelType(String connectionTypeName) {
        /*
     * TODO: What happens with communication channels of this connectionType
     * that are opened?
     */
//        getPostOfficeForAdm().getConnectionsAdministrator().removeCommunicationChannelType(connectionTypeName);
//    }

    private void addServerListener(int port, String connectionTypeName) throws ExceptionBESACommunicationChannelFailedBPO {
        getPostOfficeForAdm().getConnectionsAdministrator().addServerListener(port, connectionTypeName);
    }

    @Override
    public final ICommunicationChannelBPO getCommunicationChannelAsClient(ContainerConnectionInformationBPO info) throws ExceptionBESACommunicationChannelFailedBPO {
        return getPostOfficeForAdm().getConnectionsAdministrator().getCommunicationChannelAsClient(info);
    }

//-----------------------------------------------------
    private class HandlerCache {

        private int SIZE = 20; //REVISAR- Obtener el numero del config
        private int nextToReplace;
        AgHandlerBESA[] handlers;

        public HandlerCache() {
            handlers = new AgHandlerBESA[SIZE];
            nextToReplace = 0;
        }

        public void add(AgHandlerBESA agHandler) {
            handlers[nextToReplace] = agHandler;
            nextToReplace = (nextToReplace == handlers.length - 1) ? 0 : nextToReplace + 1;
        }

        public AgHandlerBESA getHandlerByAlias(String agAlias) {
            return getHandler(agAlias, false);
        }

        public AgHandlerBESA getHandlerByAid(String agId) {
            return getHandler(agId, true);
        }

        private AgHandlerBESA getHandler(String name, boolean isById) {
            int i = 0;
            AgHandlerBESA handler = null;
            do {
                if (handlers[i] != null) {
                    if (isById) {
                        if (handlers[i].getAgId().equals(name)) {
                            handler = handlers[i];
                            break;
                        }
                    } else {
                        if (handlers[i].getAlias().equals(name)) {
                            handler = handlers[i];
                            break;
                        }
                    }
                }
                i++;
            } while (i < handlers.length && handler == null);

            return handler;
        }
    }
}
