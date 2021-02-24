package BESA.Mobile;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Mobile.Agents.PostMan.Data.MessageDataBPO;
import BESA.Mobile.Agents.PostMan.Guards.SendMessageGuardBPO;
import BESA.Mobile.Agents.PostMan.PostManAgentBPO;
import BESA.Mobile.Agents.PostMan.State.PostManAgentStateBPO;
import BESA.Mobile.Message.MessageFactoryBPO;

/**
 *
 * @author Andrea Barraza
 * The PostOffice is in charge of the creation and destruction of PostMan Agents
 * The PostOffice maintains the connectionsAdministrator
 */
public class PostOfficeBPOCE extends PostOfficeBPO {

    public static PostOfficeBPOCE get() {
        if (instance == null) {
            instance = new PostOfficeBPOCE();
        }
        return (PostOfficeBPOCE) instance;
    }

    @Override
    protected String getPostManId(String containerID) throws ExceptionBESA {
        return super.getPostManId(containerID);
    }

    @Override
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
        PostManAgentBPO postMan_Agent = new PostManAgentBPO("PostManBPO_" + containerID+"_"+AdmBESA.getInstance().getIdAdm(), postMan_AgentState, struct, passwdAg);

        //Add to table
        postmanAgIdByContainerID.put(containerID, postMan_Agent.getAid());
        postMan_Agent.start();
        
        //Send a message to master that the postman has been created
        AgHandlerBESA ah = AdmBESA.getInstance().getHandlerByAid(postMan_Agent.getAid());
        MessageDataBPO data = new MessageDataBPO(MessageFactoryBPO.createAgentCreatedMessage(postMan_Agent.getAid(), postMan_Agent.getAlias()));
        EventBESA event = new EventBESA(SendMessageGuardBPO.class.getName(), data);
        ah.sendEvent(event);
        
        return postMan_Agent.getAid();
    }


    @Override
    //REVISAR
    public void couldNotEstablishConnectionWith(String remoteContainerID) {
        super.couldNotEstablishConnectionWith(remoteContainerID);
        //REVISAR:Avisarle al postman... deberia deshabilitar el mailing service
//        ((CompactAdmBESA) AdmBESA.getInstance()).setMasterContainerID();
    }
}
