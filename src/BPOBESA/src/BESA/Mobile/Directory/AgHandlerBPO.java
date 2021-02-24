package BESA.Mobile.Directory;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Mobile.IAdmBPO;
import BESA.Mobile.Message.EventMessageBPO;
import BESA.Mobile.Message.MessageFactoryBPO;


/**
 *
 * @author Andrea Barraza
 */

public class AgHandlerBPO extends AgHandlerBESA{ 

    private String containerID;

    public AgHandlerBPO(String agId,String alias,String containerID) {
        super(agId, alias);
        this.containerID = containerID;
    }

    public synchronized void sendEvent(EventBESA event, int timeOut) throws ExceptionBESA {
        sendEventAux(event, timeOut);
    }

    @Override
    public synchronized void sendEvent(EventBESA event) throws ExceptionBESA {
        sendEventAux(event, (int) (AdmBESA.getInstance().getConfigBESA()).getSendEventTimeout());
    }

    private synchronized void sendEventAux(EventBESA event, int timeOut) throws ExceptionBESA {
         try {
                //Send Message
                EventMessageBPO message = MessageFactoryBPO.createEventMessage(event, getAgId(),getAlias());
                ((IAdmBPO)AdmBESA.getInstance()).getPostOffice().sendMessageToLocation(containerID, message);
                message.waitForACK(timeOut);
                //Check if message timed out
                if (message.getACK() == null) {
                    ((IAdmBPO)AdmBESA.getInstance()).getPostOffice().messageTimedOut(containerID,message);
                    throw new ExceptionBESA("Message Timed-Out");
                }else
                //Verify Message State
                if (message.getACK().isNegative()) {
                    throw new ExceptionBESA(message.getACK().getExceptionMessage());
                }
            } catch (InterruptedException ex) {
                throw new ExceptionBESA("[AgPostManHandler_BPO] InterruptedException");
            }
    }

    public String getLocation() {
        return containerID;
    }
    
    @Override
    public AgentBESA getAg(){
        return null;
    }
    
   
}
