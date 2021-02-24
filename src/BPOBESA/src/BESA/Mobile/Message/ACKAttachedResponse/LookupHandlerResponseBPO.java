package BESA.Mobile.Message.ACKAttachedResponse;

/**
 *
 * @author Andrea
 */
public class LookupHandlerResponseBPO extends ACKAttachedResponseBPO {
    private final static ACKAttachedResponseBPO.Type informationType = ACKAttachedResponseBPO.Type.GETHANDLERRESPONSE;
    private String agId;
    private String agAlias;

    public LookupHandlerResponseBPO(String agId, String agAlias) {
        super (informationType);
        setAgAlias(agAlias);
        setAgId(agId);
    }
    
      public LookupHandlerResponseBPO() {
        super (informationType);

    }

    public String getAgAlias() {
        return agAlias;
    }

    public final void setAgAlias(String agAlias) {
        this.agAlias = agAlias;
    }

    public String getAgId() {
        return agId;
    }

    public final void setAgId(String agId) {
        this.agId = agId;
    }

    @Override
    public String toString() {
        String messageString = super.toString();
        messageString += getAgId();
        messageString += strSeparatorD + getAgAlias();
        return messageString;
    }

    @Override
    protected void setInformation(String informationString) {
        String[] split = informationString.split(strSeparatorD);
        setAgId(split[0]);
        setAgAlias(split[1]);
    }
    
}
