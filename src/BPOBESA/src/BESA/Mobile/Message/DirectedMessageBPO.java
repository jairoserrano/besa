package BESA.Mobile.Message;

/**
 *
 * @author Andrea Barraza
 */
public abstract class DirectedMessageBPO extends BlockingMessageBPO {

    private String agIdReceiver;
    private String agAliasReceiver;

    public DirectedMessageBPO(String id, MessageBPO.Type messageType) {
        super(id, messageType);
    }

    public String getAgIdReceiver() {
        return agIdReceiver;
    }

    protected void setAgIdReceiver(String agIdReceiver) {
        this.agIdReceiver = agIdReceiver;
    }

    public String getAgAliasReceiver() {
        return agAliasReceiver;
    }

    protected void setAgAliasReceiver(String agAliasReceiver) {
        this.agAliasReceiver = agAliasReceiver;
    }

    @Override
    public String toString() {
        String messageString = "";
        messageString += super.toString();
        messageString += strSeparatorB + getAgIdReceiver();
        messageString += strSeparatorB + getAgAliasReceiver();
        return messageString;
    }

    protected void buildMessage(String id, MessageBPO.Type messageType, String agIdReceiver, String agAliasReceiver) {
        super.buildMessage(id, messageType);
        setAgIdReceiver(agIdReceiver);
        setAgAliasReceiver(agAliasReceiver);
    }
}
