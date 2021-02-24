/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serviceprovider;

import BESA.Adapter.AdapterBESA;
import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Kernel.Social.ServiceProvider.agent.SPService;
import BESA.Kernel.Social.ServiceProvider.agent.SPServiceDataRequest;
import presentation.model.adapter.PresentationAdapter;

/**
 *
 * @author fabianjose
 */
public class SPServiceSendMessage extends SPService {

    public static String SERVICE_NAME = "SendMessage";

    @Override
    public DataBESA executeService(SPServiceDataRequest data, AdapterBESA adapter) {
        RequestMessage requestMessage = (RequestMessage) data;
        PresentationAdapter presentationAdapter = (PresentationAdapter) adapter;

        presentationAdapter.displayMessage(requestMessage.getQuery());

        ResponseMessage responseMessage = new ResponseMessage(ResponseMessage.Response.ACK);
        return responseMessage;
    }
}
