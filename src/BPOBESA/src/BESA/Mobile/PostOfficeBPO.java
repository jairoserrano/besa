package BESA.Mobile;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AGENTSTATE;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Mobile.Agents.PostMan.Data.MessageDataBPO;
import BESA.Mobile.Agents.PostMan.Data.StringDataBPO;
import BESA.Mobile.Agents.PostMan.Guards.ChangeCommunicationChannelGuardBPO;
import BESA.Mobile.Agents.PostMan.Guards.InterpretMessageGuardBPO;
import BESA.Mobile.Agents.PostMan.Guards.MessageTimedOutGuardBPO;
import BESA.Mobile.Agents.PostMan.Guards.SendMessageGuardBPO;
import BESA.Mobile.Agents.PostMan.PostManAgentBPO;
import BESA.Mobile.Agents.PostMan.State.PostManAgentStateBPO;
import BESA.Mobile.CommunicationChannel.ICommunicationChannelBPO;
import BESA.Mobile.ConnectionsAdministrator.CommunicationChannelDirectoryBPO;
import BESA.Mobile.ConnectionsAdministrator.ConnectionsAdministratorBPO;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.Message.MessageBPO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Andrea Barraza
 * The PostOffice is in charge of the creation and destruction of PostMan Agents
 * The PostOffice maintains the connectionsAdministrator
 */
public class PostOfficeBPO implements IPostOfficeBPO {

    protected static PostOfficeBPO instance;
    protected HashMap<String, String> postmanAgIdByContainerID;
    protected ConnectionsAdministratorBPO connectionsAdministrator;
    protected boolean isEnabled;

    protected PostOfficeBPO() {
        postmanAgIdByContainerID = new HashMap();
        connectionsAdministrator = new ConnectionsAdministratorBPO();
        isEnabled = true;
    }

    public static PostOfficeBPO get() {
        if (instance == null) {
            instance = new PostOfficeBPO();
        }
        return instance;
    }

    protected String getPostManId(String containerID) throws ExceptionBESA {
        //FIRST - Try to find postman locally      
        if (postmanAgIdByContainerID.containsKey(containerID)) {
            return postmanAgIdByContainerID.get(containerID);
        }
        //SECOND - Try to find postman using BAP
        //REVISAR - Agregar al corregir el BAP
//        AgHandlerBESA ah = AdmBESA.getInstance().getHandlerByAlias("PostManBPO_"+ containerID);
//        if (ah != null) {
//            return ah.getAgId();
//        }

        //THIRD - If postman does not exist create it
        try {
            String postMan_agId = createPostMan(containerID);
            return postMan_agId;
        } catch (ExceptionBESA ex) {
            throw new ExceptionBESA("PostMan does not exist and could not be added");
        }
    }

    protected synchronized String createPostMan(String containerID) throws ExceptionBESA {
        if (!isEnabled) {
            throw new ExceptionBESA("PostOffice is Disabled");
        }
        //State
        PostManAgentStateBPO postMan_AgentState = new PostManAgentStateBPO(containerID);
        //Creating Agent Structure
        StructBESA struct = getPostManStruct();
        //Create Agent
        double passwdAg = 1.0;
        PostManAgentBPO postMan_Agent = new PostManAgentBPO("PostManBPO_" + containerID, postMan_AgentState, struct, passwdAg);

        //Add to table
        postmanAgIdByContainerID.put(containerID, postMan_Agent.getAid());

        postMan_Agent.start();
        return postMan_Agent.getAid();
    }

    protected StructBESA getPostManStruct() throws ExceptionBESA {
        //Creating Agent Structure
        StructBESA struct = new StructBESA();
        //Adding Behaviors
        struct.addBehavior("MessageTimedOut_Behavior_BPO");
        struct.addBehavior("SendMessage_Behavior_BPO");
        struct.addBehavior("InterpretMessage_Behavior_BPO");
        struct.addBehavior("ChangeCommunicationChannel_Behavior_BPO");
        //Adding Guards
        struct.bindGuard("MessageTimedOut_Behavior_BPO", MessageTimedOutGuardBPO.class);
        struct.bindGuard("SendMessage_Behavior_BPO", SendMessageGuardBPO.class);
        struct.bindGuard("InterpretMessage_Behavior_BPO", InterpretMessageGuardBPO.class);
        struct.bindGuard("ChangeCommunicationChannel_Behavior_BPO", ChangeCommunicationChannelGuardBPO.class);

        return struct;
    }

    protected AgHandlerBESA getHandlerForPostMan(String containerID) throws ExceptionBESA {
        return AdmBESA.getInstance().getHandlerByAid(getPostManId(containerID));
    }

    //NOTIFY POSTMAN CHANGES IN COMMUNICATION CHANNEL----------------------------------------------------------------------------------------
    @Override
    public void notifyChangeInReadCommunicationChannel(String containerID) throws ExceptionBESA {
        notifyChangeInCommunicationChannel(containerID, false);
    }

    @Override
    public void notifyChangeInWriteCommunicationChannel(String containerID) throws ExceptionBESA {
        notifyChangeInCommunicationChannel(containerID, true);
    }

    private void notifyChangeInCommunicationChannel(String containerID, boolean isWrite) throws ExceptionBESA {
        if (!isEnabled) {
            return;
        }
        //Notify postman there was a change of communication channel 
        String postMan_agId = getPostManId(containerID);
        StringDataBPO data = new StringDataBPO(isWrite ? "true" : "false");
        EventBESA event = new EventBESA(ChangeCommunicationChannelGuardBPO.class.getName(), data);
        AgHandlerBESA ah = (AgHandlerBESA) AdmBESA.getInstance().getHandlerByAid(postMan_agId);
        ah.sendEvent(event);
    }

    //SEND EVENTS TO POSTMAN-----------------------------------------------------------------------------------------------
    @Override
    public void sendMessageToLocation(String containerID, MessageBPO message) throws ExceptionBESA {
        if (isEnabled) {
            sendEventToPostMan(getPostManId(containerID), message, SendMessageGuardBPO.class.getName());
        }
    }

    @Override
    public void messageTimedOut(String receiverContainerID, MessageBPO message) throws ExceptionBESA {
        String postMan_agId = getPostManId(receiverContainerID);
        sendEventToPostMan(postMan_agId, message, MessageTimedOutGuardBPO.class.getName());
    }

    @Override
    public void interpretMessage(String emisorContainerID, MessageBPO message) throws ExceptionBESAMessageInterpreterFailedBPO {
        try {
            sendEventToPostMan(getPostManId(emisorContainerID), message, InterpretMessageGuardBPO.class.getName());
        } catch (ExceptionBESA ex) {
            throw new ExceptionBESAMessageInterpreterFailedBPO("Message could not be interpreted");
        }
    }

    private void sendEventToPostMan(String postMan_agId, MessageBPO message, String evType) throws ExceptionBESA {
        MessageDataBPO data = new MessageDataBPO(message);
        EventBESA event = new EventBESA(evType, data);
        AgHandlerBESA ah = (AgHandlerBESA) AdmBESA.getInstance().getHandlerByAid(postMan_agId);
        if (!ah.getState().equals(AGENTSTATE.KILL)) {
            ah.sendEvent(event);
        }
    }

    //COMMUNICATION ADMINISTRATOR----------------------------------------------------------------------------------------
    @Override
    public final ConnectionsAdministratorBPO getConnectionsAdministrator() {
        return connectionsAdministrator;
    }

    @Override
    public void connectTo(String containerID) {
        if (!isEnabled) {
            return;
        }
        connectionsAdministrator.connectTo(containerID);
    }

    @Override
    public void acceptConnection(ICommunicationChannelBPO communicationChannel) {
        if (!isEnabled) {
            return;
        }
        //Note: When received from a server.accept() this communicationChannel does not know the id of the remote container
        connectionsAdministrator.acceptConnection(communicationChannel);
    }

    @Override
    public void couldNotEstablishConnectionWith(String remoteContainerID) {
        connectionsAdministrator.removeContainerConnectionInformation(remoteContainerID);
    }

    //SHUTDOWN-----------------------------------------------------------------------------------------------
    private synchronized void removeLocation(String containerID) throws ExceptionBESA {
        if (postmanAgIdByContainerID.containsKey(containerID)) {
            String postMan_agId = postmanAgIdByContainerID.get(containerID);
            AgHandlerBESA ah = AdmBESA.getInstance().getHandlerByAid(postMan_agId);
            if (ah != null) {
                // Kill PostMan agent
                AdmBESA.getInstance().killAgent(postMan_agId, 1.0);
            }
        }
    }

    public synchronized void shutDown() throws ExceptionBESA {
        System.out.println("ENTRO A POSTOFFICE SHUTDOWN");
        isEnabled = false;

        //Close all connection setups and server listeners
        connectionsAdministrator.shutDown();

        //Stop local postman agents
        for (Iterator it = postmanAgIdByContainerID.entrySet().iterator(); it.hasNext();) {
            String containerID = (String) ((Map.Entry) it.next()).getKey();
            System.out.println("KILL POSTMAN FOR: " + containerID);
            removeLocation(containerID);
        }
        postmanAgIdByContainerID.clear();

        //Close communication channels
        CommunicationChannelDirectoryBPO.get().shutDown();

    }
}
