package BESA.Mobile.Agents.PostMan.Data;

import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.Message.MessageBPO;
import BESA.Mobile.Message.MessageFactoryBPO;

/**
 *
 * @author Andrea Barraza
 */
public final class MessageDataBPO extends DataBESA {

    private MessageBPO message;

    public MessageDataBPO() {
    }

    public MessageDataBPO(MessageBPO message) {
        setMessage(message);
    }

    public MessageBPO getMessage() {
        return message;
    }

    public void setMessage(MessageBPO message) {
        this.message = message;
    }

    @Override
    public String getStringFromDataBesa() {
        return message.toString();
    }

    @Override
    public void getDataBesaFromString(String messageData) {
        try {
            setMessage(MessageFactoryBPO.rebuildMessageFromString(messageData));
        } catch (ExceptionBESAMessageInterpreterFailedBPO ex) {
            message= null;
        }
    }

    @Override
    public String toString(){
        return getStringFromDataBesa();
    }

}
