/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serviceprovider;

import BESA.Kernel.Agent.Event.DataBESA;



/**
 *
 * @author fabianjose
 */
public class ResponseMessage extends DataBESA {

    public enum Response {
        ACK, NACK
    };
    
    private Response response;

    public ResponseMessage(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
