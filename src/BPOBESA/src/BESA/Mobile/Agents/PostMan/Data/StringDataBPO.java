package BESA.Mobile.Agents.PostMan.Data;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author Andrea Barraza
 */
public class StringDataBPO extends DataBESA {

    private String message;

    public StringDataBPO() {
    }

    public StringDataBPO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getStringFromDataBesa() {
        return message;
    }

    @Override
    public void getDataBesaFromString(String messageData) {
        setMessage(messageData);
    }

    @Override
    public String toString() {
        return getStringFromDataBesa();
    }
}
