package BESA.Mobile.Message.Interpreter;

import BESA.ExceptionBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Mobile.Directory.AgHandlerBPO;
import BESA.Mobile.Exceptions.ExceptionBESAMessageInterpreterFailedBPO;
import BESA.Mobile.Message.ACKAttachedResponse.ACKAttachedResponseBPO;
import BESA.Mobile.Message.ACKAttachedResponse.LookupHandlerResponseBPO;
import BESA.Mobile.Message.AdministrativeMessageBPO;
import BESA.Mobile.Message.EventMessageBPO;
import BESA.Mobile.Message.MessageBPO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrea Barraza
 */
public class MessageInterpreterBPO {

    private static MessageInterpreterBPO instance;

    private MessageInterpreterBPO() {
    }

    public static MessageInterpreterBPO get() {
        if (instance == null) {
            instance = new MessageInterpreterBPO();
        }
        return instance;
    }

    public static ACKAttachedResponseBPO interpretMessage(MessageBPO message, String remoteContainerID) throws ExceptionBESAMessageInterpreterFailedBPO {
        MessageBPO.Type messageType = message.getMessageType();
        ACKAttachedResponseBPO response = null;
        switch (messageType) {
            case ADMINISTRATIVE:
                response = interpretAdministrativeMessage((AdministrativeMessageBPO) message, remoteContainerID);
                break;
            case EVENT:
                interpretEventMessage((EventMessageBPO) message);
                break;
            default:
                throw new ExceptionBESAMessageInterpreterFailedBPO("Message could not be interpreted");
        }

        return response;

    }

    private static ACKAttachedResponseBPO interpretAdministrativeMessage(AdministrativeMessageBPO message, String remoteContainerID) throws ExceptionBESAMessageInterpreterFailedBPO {
        AdministrativeMessageBPO.Action action = ((AdministrativeMessageBPO) message).getAction();
        AdmBESA admBESA = AdmBESA.getInstance();
        ACKAttachedResponseBPO response = null;

        switch (action) {
            case CREATE:
                System.out.println("\nLLEGO MSG - REGISTER AGENT FROM " + remoteContainerID + "::  " + message.getAgId() + ">>" + message.getAgAlias());
                AgHandlerBPO handler = new AgHandlerBPO(message.getAgId(), message.getAgAlias(), remoteContainerID);
//**********************        
        try {
            admBESA.registerAgent(message.getAgId(), handler, message.getAgAlias());
        } catch (ExceptionBESA ex) {
            Logger.getLogger(MessageInterpreterBPO.class.getName()).log(Level.SEVERE, null, ex);
        }

                break;
            case KILL:
                System.out.println("\nLLEGO MSG - KILL AGENT FROM " + remoteContainerID + "::  " + message.getAgId());
                try {
                    AgHandlerBESA agHandler = admBESA.getHandlerByAid(message.getAgId());
                    if (agHandler instanceof AgHandlerBPO) {
                        if (((AgHandlerBPO) (agHandler)).getLocation().equals(remoteContainerID)) {
                            admBESA.erase(agHandler);
                            break;
                        }
                    }
                    admBESA.killAgent(message.getAgId(), message.getAgPassword());
                } catch (ExceptionBESA ex) {
                    throw new ExceptionBESAMessageInterpreterFailedBPO("Action can't be interpreted for administrative message.");
                }
                break;
            case MOVE:
                //NOTA-REVISAR: Como se va mover un agente desde el mundo movil al estatico??
                break;
            case DOESAGENTEXIST:
                System.out.println("\nLLEGO MSG - DOESAGENTEXIST FROM " + remoteContainerID);

                AgHandlerBESA agh = null;
                try {
                    if (message.getAgAlias() != null) {
                        System.out.println("AGENT ALIAS " + message.getAgAlias());
                        agh = (AgHandlerBESA) admBESA.getHandlerByAlias(message.getAgAlias());
                    } else if (message.getAgId() != null) {
                        System.out.println("AGENT ID " + message.getAgId());
                        agh = (AgHandlerBESA) admBESA.getHandlerByAid(message.getAgId());
                    }

                    if (agh == null) {
                        throw new ExceptionBESA("Agent handler could not be found.");
                    } else {
                        response = new LookupHandlerResponseBPO(agh.getAgId(), agh.getAlias());
                    }
                } catch (ExceptionBESA ex) {
                    throw new ExceptionBESAMessageInterpreterFailedBPO("Action can't be interpreted for administrative message.");
                }
                break;
            case ADDSERVICE:
                if (message.hasDescriptors()) {
                    System.out.println("ADD SERVICE " + message.getServiceId() + " with descriptors " + message.getDescriptors());
                    AdmBESA.getInstance().addService(message.getServiceId(), message.getDescriptors());
                } else {
                    System.out.println("ADD SERVICE " + message.getServiceId() + " without descriptors ");
                    AdmBESA.getInstance().addService(message.getServiceId());
                }
                break;
            case BINDSERVICE:
                System.out.println("BIND SERVICE " + message.getServiceId() + " with agent " + message.getAgId());
                AdmBESA.getInstance().bindService(message.getAgId(), message.getServiceId());
                break;
            case BINDSPSERVICEINDIRECTORY:
                System.out.println("BINDSPSERVICEINDIRECTORY SERVICE " + message.getDirectoryServiceName() + " with agent " + message.getAgId());
                AdmBESA.getInstance().bindSPServiceInDirectory(message.getAgId(), message.getDirectoryServiceName());
                break;
            default:
                throw new ExceptionBESAMessageInterpreterFailedBPO(
                        "Action can't be interpreted for administrative message.");
        }

        return response;

    }

    private static void interpretEventMessage(EventMessageBPO message) throws ExceptionBESAMessageInterpreterFailedBPO {
        try {
            AgHandlerBESA ah;
            if (message.getAgIdReceiver() != null) {
                ah = AdmBESA.getInstance().getHandlerByAid(message.getAgIdReceiver());
            } else {
                ah = AdmBESA.getInstance().getHandlerByAlias(message.getAgAliasReceiver());
            }
            System.out.println("\nLLEGO MSG - SEND EVENT PARA " + ah.getAlias() + "\n");
            ah.sendEvent(message.getEvent());

        } catch (Exception ex) {
            throw new ExceptionBESAMessageInterpreterFailedBPO("Event Message Interpretation Error");
        }
    }
}
