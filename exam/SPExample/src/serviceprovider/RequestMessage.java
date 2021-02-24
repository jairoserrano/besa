/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serviceprovider;


import BESA.Kernel.Social.ServiceProvider.agent.SPServiceDataRequest;
import agenttest.GuardReplyTest;

/**
 *
 * @author fabianjose
 */
public class RequestMessage extends SPServiceDataRequest {

    private String query;

    public RequestMessage(String query) {
        super(GuardReplyTest.class.getName(), ResponseMessage.class.getName());
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
