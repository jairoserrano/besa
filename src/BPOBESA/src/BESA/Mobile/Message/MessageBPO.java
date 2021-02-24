package BESA.Mobile.Message;

import java.io.Serializable;

/**
 *
 * @author Andrea Barraza
 */
public abstract class MessageBPO implements Serializable {

    private String idMessage;
    private MessageBPO.Type messageType;

    protected MessageBPO(String id, MessageBPO.Type messageType) {
        buildMessage(id, messageType);
    }

    protected final void buildMessage(String id, MessageBPO.Type messageType) {
        setId(id);
        setMessageType(messageType);
    }

    protected abstract void buildMessage(String messageString);

    public final String getId() {
        return idMessage;
    }

    private void setId(String id) {
        this.idMessage = id;
    }

    public final MessageBPO.Type getMessageType() {
        return messageType;
    }

    private void setMessageType(Type messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        String messageString = "";
        messageString = messageType + strSeparatorA;
        messageString += getId();
        return messageString;
    }
    protected static final String strSeparatorA = "<sptA>";
    protected static final String strSeparatorB = "<sptB>";

    public static enum Type {
        ACK, EVENT, ADMINISTRATIVE, COMMUNICATION_CHANNEL
    }
}
