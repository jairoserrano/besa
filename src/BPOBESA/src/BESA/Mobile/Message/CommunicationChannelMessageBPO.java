package BESA.Mobile.Message;

/**
 *
 * @author Andrea
 */
public class CommunicationChannelMessageBPO extends MessageBPO {

    private static final MessageBPO.Type messageType = MessageBPO.Type.COMMUNICATION_CHANNEL;
    private String message;

    public CommunicationChannelMessageBPO(String idMessage, String message) {
        super(idMessage, messageType);
        this.message = message;
    }
    
    public CommunicationChannelMessageBPO() {
        super(null, messageType);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected void buildMessage(String messageString) {
        String split[] = messageString.split(MessageBPO.strSeparatorB);
        buildMessage(split[0], messageType);
        setMessage(split[1]);
    }

    @Override
    public String toString() {
        String messageString = "";
        messageString += super.toString();
        messageString += strSeparatorB + getMessage();
        return messageString;
    }
    
}
