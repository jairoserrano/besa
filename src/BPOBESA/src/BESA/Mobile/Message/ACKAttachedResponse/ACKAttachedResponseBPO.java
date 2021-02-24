package BESA.Mobile.Message.ACKAttachedResponse;

import java.io.Serializable;

/**
 *
 * @author Andrea
 */
public abstract class ACKAttachedResponseBPO implements Serializable{

    private ACKAttachedResponseBPO.Type informationType;

    protected ACKAttachedResponseBPO(ACKAttachedResponseBPO.Type informationType) {
        this.informationType = informationType;
    }

    protected abstract void setInformation(String informationString);

    public static ACKAttachedResponseBPO getAttachedResponse(String informationString) {
        String[] split = informationString.split(strSeparatorC);
        ACKAttachedResponseBPO info=null;
        if(split[0].equals(ACKAttachedResponseBPO.Type.GETHANDLERRESPONSE.toString())){
            info= new LookupHandlerResponseBPO();
        }
        if(info!=null){
            info.setInformation(split[1]);
        }
        return info;
    }
    
    @Override
    public String toString(){
        return informationType.toString()+strSeparatorC;
    }
    
    protected static final String strSeparatorC = "<sptC>";
    protected static final String strSeparatorD = "<sptD>";

    public static enum Type {
        GETHANDLERRESPONSE
    }

}
