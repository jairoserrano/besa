package BESA.Mobile.Message;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Kernel.Agent.Event.EventBESA;

/**
 *
 * @author Andrea Barraza
 */
public final class EventMessageBPO extends DirectedMessageBPO {

    private static final MessageBPO.Type messageType = MessageBPO.Type.EVENT;
    private EventBESA event;

    protected EventMessageBPO() {
        super(null, messageType);
    }

    protected EventMessageBPO(String idMessage, EventBESA event, String agIdReceiver, String agAliasReceiver) {
        super(idMessage, messageType);
        this.event = event;
        setAgIdReceiver(agIdReceiver);
        setAgAliasReceiver(agAliasReceiver);
    }

    public EventBESA getEvent() throws ExceptionBESA {
        if (event == null) {
            throw new ExceptionBESA("Event does not exist");
        }
        return event;
    }

    //To be able to reconstruct the Event then its DataBESA object must have a
    //constructor that does not receive parameters and getDataBesaFromString, getStringFromDataBesa implemented
    private void setEvent(String eventType, String dataClassName, String data) {
        try {
            DataBESA dataBESA = (DataBESA) Class.forName(dataClassName).newInstance();
            dataBESA.getDataBesaFromString(data);
            event = new EventBESA(eventType, dataBESA);
        } catch (Exception e) {
            event = null;
        }
    }

    @Override
    public String toString() {
        String messageString = "";
        messageString += super.toString();
        try {
            String messageString2 = strSeparatorB;
            messageString2 += getEvent().getType() + strSeparatorB;
            messageString2 += getEvent().getData().getClass().getName() + strSeparatorB;
            messageString2 += getEvent().getData().getStringFromDataBesa();
            return messageString.concat(messageString2);
        } catch (ExceptionBESA ex) {
            return messageString;
        }
    }

    @Override
    protected void buildMessage(String messageString) {
        String[] split = messageString.split(MessageBPO.strSeparatorB);
        buildMessage(split[0], messageType, split[1], split[2]);
        if (split.length == 6) {
            setEvent(split[3], split[4], split[5]);
        }
    }

}
