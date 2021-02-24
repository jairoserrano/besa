/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.model.adapter;

import BESA.Adapter.AdapterBESA;
import BESA.Kernel.Social.ServiceProvider.agent.ServiceProviderBESA;
import presentation.model.World;
import serviceprovider.SPServiceSendMessage;

/**
 *
 * @author fabianjose
 */
public class PresentationAdapter extends AdapterBESA {

    private World world;
    private ServiceProviderBESA serviceProviderBESA = null;

    public PresentationAdapter() {
        super(null, null);
    }

    public void displayMessage(String query) {
        world.displayMessage(query);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        this.world.setPresentationAdapter(this);
    }

    public void sendHi() {
        serviceProviderBESA.processAsychEvent(new MessageData("Hi"), SPServiceSendMessage.SERVICE_NAME);
    }

    public void setServiceProviderBESA(ServiceProviderBESA serviceProviderBESA) {
        this.serviceProviderBESA = serviceProviderBESA;
    }
}
