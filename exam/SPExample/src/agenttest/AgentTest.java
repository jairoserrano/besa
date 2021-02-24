/*
 * @(#)AgentTest.java 3.0	20/09/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package agenttest;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.Social.ServiceProvider.agent.GuardServiceProviderSuscribe;
import BESA.Kernel.Social.ServiceProvider.agent.ServiceProviderBESA;
import BESA.Kernel.Social.ServiceProvider.agent.ServiceProviderDataSuscribe;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import java.util.logging.Level;
import java.util.logging.Logger;
import presentation.model.adapter.MessageData;
import serviceprovider.ResponseMessage;
import serviceprovider.SPServiceSendMessage;
import serviceprovider.ServiceProviderMessageAgent;

/**
 * This class represents the test agent.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina - Pontificia Universidad Javeriana
 * @version 2.0, 20/09/11
 * @since   JDK1.0
 */
public class AgentTest extends AgentBESA {

    public AgentTest(String alias, double passwd) throws KernellAgentExceptionBESA {
        super(alias, new StateTest(), getStruct(), passwd);
    }

    @Override
    public void setupAgent() {
        try {
            //----------------------------------------------------------------//
            // SYNCHRONIC SERVICE                                             //
            //----------------------------------------------------------------//
            //Busca en el directorio el agente que presta el servicio
            String spAgId = AdmBESA.getInstance().lookupSPServiceInDirectory(ServiceProviderMessageAgent.SERVICEONDIRECTORY);
            AgHandlerBESA agH = AdmBESA.getInstance().getHandlerByAid(spAgId);
            //Crea el data de suscripcion
            ServiceProviderDataSuscribe spDataSuscribe = new ServiceProviderDataSuscribe(
                    GuardReplyTest.class.getName(),
                    ServiceProviderBESA.SYNCHRONIC_SERVICE,
                    SPServiceSendMessage.SERVICE_NAME,
                    ResponseMessage.class.getName());
            //Crea el evento a enviar
            EventBESA evSP = new EventBESA(GuardServiceProviderSuscribe.class.getName(), spDataSuscribe);
            evSP.setSenderAgId(this.getAid());
            //Env�a el evento
            agH.sendEvent(evSP);
            //----------------------------------------------------------------//
            // ASYNCHRONIC SERVICE                                            //
            //----------------------------------------------------------------//
            //Crea el evento a enviar
            spDataSuscribe = new ServiceProviderDataSuscribe(
                    GuardTest.class.getName(),
                    ServiceProviderBESA.ASYNCHRONIC_SERVICE,
                    SPServiceSendMessage.SERVICE_NAME,
                    MessageData.class.getName());
            evSP = new EventBESA(GuardServiceProviderSuscribe.class.getName(), spDataSuscribe);
            evSP.setSenderAgId(this.getAid());
            //Env�a el evento
            agH.sendEvent(evSP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdownAgent() {
    }

    private static StructBESA getStruct() {
        try {
            StructBESA structAgent = new StructBESA();
            structAgent.addBehavior("BehaviorTest");
            structAgent.bindGuard("BehaviorTest", GuardTest.class);
            structAgent.bindGuard("BehaviorTest", GuardReplyTest.class);
            structAgent.bindGuard("BehaviorTest", GuardRequestTest.class);
            return structAgent;
        } catch (ExceptionBESA ex) {
            Logger.getLogger(AgentTest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
