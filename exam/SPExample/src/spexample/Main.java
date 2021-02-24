/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spexample;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.Social.ServiceProvider.agent.GuardServiceProviderRequest;
import BESA.Kernel.Social.ServiceProvider.agent.GuardServiceProviderSuscribe;
import BESA.Kernel.Social.ServiceProvider.agent.ServiceProviderDescriptor;
import BESA.Kernel.Social.ServiceProvider.agent.StateServiceProvider;
import BESA.Kernel.System.AdmBESA;
import agenttest.AgentTest;
import agenttest.GuardRequestTest;
import java.util.logging.Level;
import java.util.logging.Logger;
import presentation.model.World;
import presentation.model.adapter.PresentationAdapter;
import presentation.view.HiView;
import serviceprovider.SPServiceSendMessage;
import serviceprovider.ServiceProviderMessageAgent;

/**
 *
 * @author fabianjose
 */
public class Main {

    //========================================================================//
    // Container properties.                                                  //
    //========================================================================//
    /**
     * Container password.
     */
    private static double PASSWORD;
    /**
     * IP address of local BESA container.
     */
    //========================================================================//
    // Service provider properties.                                           //
    //========================================================================//
    /**
     *
     */
    private ServiceProviderDescriptor spDescriptorSendMessage;
    /**
     *
     */
    private SPServiceSendMessage sPServiceSendMessage;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //--------------------------------------------------------------------//
            // Creates and starts the BESA container.                             //
            //--------------------------------------------------------------------//
            AdmBESA adm = AdmBESA.getInstance("res/confbesa.xml");
            //--------------------------------------------------------------------//
            // Creates the service provider.                                      //
            //--------------------------------------------------------------------//
            Main main = new Main();

            main.sPServiceSendMessage = new SPServiceSendMessage();
            main.sPServiceSendMessage.setName(SPServiceSendMessage.SERVICE_NAME);
            
            main.spDescriptorSendMessage = new ServiceProviderDescriptor();
            main.spDescriptorSendMessage.addSPService(main.sPServiceSendMessage);

            HiView hiView = new HiView();
            World world = new World(hiView);
            PresentationAdapter presentationAdapter = new PresentationAdapter();
            presentationAdapter.setWorld(world);

            StructBESA theStruct = new StructBESA();
            theStruct.addBehavior("BehaviorServiceProvider");
            theStruct.bindGuard("BehaviorServiceProvider", GuardServiceProviderSuscribe.class);
            theStruct.bindGuard("BehaviorServiceProvider", GuardServiceProviderRequest.class);
            
            StateServiceProvider state = new StateServiceProvider(presentationAdapter, main.spDescriptorSendMessage);
            
            ServiceProviderMessageAgent serviceProviderMessageAgent = new ServiceProviderMessageAgent("SPMessageAgent", state, theStruct, PASSWORD);
            serviceProviderMessageAgent.start();
            
            presentationAdapter.setServiceProviderBESA(serviceProviderMessageAgent);
                        
            AgentTest agentTest = new AgentTest("Test", 7.77);
            agentTest.start();
            
            EventBESA ev = new EventBESA(GuardRequestTest.class.getName(), null);
            try {
                adm.getHandlerByAlias("Test").sendEvent(ev);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ExceptionBESA ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
