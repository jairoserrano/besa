package BESA.Mobile.Message;

import BESA.Mobile.Message.ACKAttachedResponse.ACKAttachedResponseBPO;

/**
 *
 * @author Andrea Barraza
 */
public final class ACKMessageBPO extends MessageBPO {

    private static final MessageBPO.Type messageType = MessageBPO.Type.ACK;
    private boolean isNegative; //If isNegative=true then the ACKMessage informs that a message was not sent
    //If the ACK is negative, then the Exception Message informs the reasons why a message could not be sent.
    private String exceptionMessage;
    private ACKAttachedResponseBPO attachedResponse;

    protected ACKMessageBPO() {
        super(null, messageType);
    }

    protected ACKMessageBPO(String idMessage) { //Initiated as Positive ACK
        super(idMessage, messageType);
        isNegative = false;
        exceptionMessage = null;
    }

    protected ACKMessageBPO(String idMessage, String exceptionMessage) { //Initiated as Negative ACK
        super(idMessage, messageType);
        setIsNegative(exceptionMessage);
    }

    public boolean isNegative() {
        return isNegative;
    }

    private void setIsNegative(String exceptionMessage) {
        isNegative = true;
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public ACKAttachedResponseBPO getAttachedResponse() {
        return attachedResponse;
    }

    public void setAttachedResponse(ACKAttachedResponseBPO attachedResponse) {
        this.attachedResponse = attachedResponse;
    }

    private void setAttachedResponse(String attachedResponse) {
        if (attachedResponse != null) {
            this.attachedResponse = ACKAttachedResponseBPO.getAttachedResponse(attachedResponse);
        }
    }

    @Override
    public String toString() {
        String messageString = "";
        messageString += super.toString();
        if (attachedResponse != null) {
            messageString += strSeparatorB + attachedResponse.toString();
        } else {
            messageString += strSeparatorB + "null";
        }
        if (isNegative) {
            messageString += strSeparatorB + exceptionMessage;
        }
        return messageString;
    }

    @Override
    protected void buildMessage(String messageString) {
        String split[] = messageString.split(MessageBPO.strSeparatorB);
        buildMessage(split[0], messageType);
        setAttachedResponse(split[1]);
        if (split.length == 3) {
            setIsNegative(split[2]);
        }

    }

}
