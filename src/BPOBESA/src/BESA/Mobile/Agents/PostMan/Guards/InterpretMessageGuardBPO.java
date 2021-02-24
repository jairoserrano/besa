package BESA.Mobile.Agents.PostMan.Guards;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Mobile.Agents.PostMan.Data.MessageDataBPO;
import BESA.Mobile.Agents.PostMan.State.PostManAgentStateBPO;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.Message.ACKAttachedResponse.ACKAttachedResponseBPO;
import BESA.Mobile.Message.ACKMessageBPO;
import BESA.Mobile.Message.Interpreter.MessageInterpreterBPO;
import BESA.Mobile.Message.MessageBPO;
import BESA.Mobile.Message.MessageFactoryBPO;

/**
 *
 * @author Andrea Barraza
 */
public class InterpretMessageGuardBPO extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        if (event.getData() instanceof MessageDataBPO) {
            MessageBPO message = ((MessageDataBPO) event.getData()).getMessage();
            try {
                ACKAttachedResponseBPO response=MessageInterpreterBPO.interpretMessage(message, ((PostManAgentStateBPO) this.getAgent().getState()).getRemoteContainerID());
                //Send Positive ACK
                ACKMessageBPO ack = (ACKMessageBPO) MessageFactoryBPO.createPositiveACK(message.getId());
                ack.setAttachedResponse(response);
                ((PostManAgentStateBPO) this.agent.getState()).sendACK(ack);
            } catch (ExceptionBESAMessageInterpreterFailedBPO ex) {
                //Send Negative ACK
                ACKMessageBPO ack = (ACKMessageBPO) MessageFactoryBPO.createNegativeACK(message.getId(), ex.getMessage());
                ((PostManAgentStateBPO) this.agent.getState()).sendACK(ack);
            }
        }
    }
}
