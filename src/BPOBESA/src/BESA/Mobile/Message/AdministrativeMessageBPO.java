package BESA.Mobile.Message;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Andrea Barraza
 */
public final class AdministrativeMessageBPO extends BlockingMessageBPO {

    private static final MessageBPO.Type messageType = MessageBPO.Type.ADMINISTRATIVE;
    private AdministrativeMessageBPO.Action action;
    private String agId; //The ID of the Agent for which the action applies to.
    private String agAlias; //The Alias of the Agent for which the action applies to.
    //If the AdministrativeMessage.Action is Move, then the place where the agent will be moved must be specified
    private String aliasDestinationAdmBESA;
    private double agPassword; //The agent password
    //Service Oriented Attributes
    private String serviceId;
    private ArrayList<String> descriptors;
    private String directoryServiceName;
    private static final String descriptorsSpt = "&";
    private boolean hasDescriptors;

    protected AdministrativeMessageBPO() {
        super(null, messageType);
        descriptors = new ArrayList<String>();
        descriptors.add("VACIO");
        hasDescriptors = false;
        setAlwaysRetryToSendMessage(true);
    }

    protected AdministrativeMessageBPO(String idMessage) {
        super(idMessage, messageType);
        descriptors = new ArrayList<String>();
        descriptors.add("VACIO");
        hasDescriptors = false;
        setAlwaysRetryToSendMessage(true);
    }

    protected void setDoesAgentExist(String agId, String agAlias) {
        setAgAlias(agAlias);
        setAgId(agId);
        this.action = Action.DOESAGENTEXIST;
    }

    protected void setCreateAgent(String agId, String agAlias) {
        setAgAlias(agAlias);
        setAgId(agId);
        this.action = Action.CREATE;
    }

    protected void setEliminateAgent(String agId, double agPassword) {
        setAgPassword(agPassword);
        setAgId(agId);
        this.action = Action.KILL;
    }

    protected void setMoveAgent(String agId, String agAlias, String aliasDestinationAdmBESA, double agPassword) {
        setAgAlias(agAlias);
        setAgId(agId);
        setAgPassword(agPassword);
        this.aliasDestinationAdmBESA = aliasDestinationAdmBESA;
        this.action = Action.MOVE;
    }

    protected void setAddService(String serviceId) {
        this.serviceId = serviceId;
        this.action = Action.ADDSERVICE;
    }

    protected void setAddService(String serviceId, ArrayList<String> descriptors) {
        this.serviceId = serviceId;
        this.descriptors = descriptors;
        hasDescriptors = true;
        this.action = Action.ADDSERVICE;
    }

    protected void setBindService(String agId, String serviceId) {
        this.agId = agId;
        this.serviceId = serviceId;
        this.action = Action.BINDSERVICE;
    }

    protected void setBindSPServiceInDirectory(String agId, String directoryServiceName) {
        this.agId = agId;
        this.directoryServiceName = directoryServiceName;
        this.action = Action.BINDSPSERVICEINDIRECTORY;
    }

    public AdministrativeMessageBPO.Action getAction() {
        return action;
    }

    private void setAction(String action) {
        if (action.equals(Action.CREATE.toString())) {
            this.action = Action.CREATE;
        } else if (action.equals(Action.KILL.toString())) {
            this.action = Action.KILL;
        } else if (action.equals(Action.MOVE.toString())) {
            this.action = Action.MOVE;
        } else if (action.equals(Action.DOESAGENTEXIST.toString())) {
            this.action = Action.DOESAGENTEXIST;
        } else if (action.equals(Action.ADDSERVICE.toString())) {
            this.action = Action.ADDSERVICE;
        } else if (action.equals(Action.BINDSERVICE.toString())) {
            this.action = Action.BINDSERVICE;
        } else if (action.equals(Action.BINDSPSERVICEINDIRECTORY.toString())) {
            this.action = Action.BINDSPSERVICEINDIRECTORY;
        }
    }

    public String getAgId() {
        return agId;
    }

    private void setAgId(String agId) {
        this.agId = agId;
    }

    public String getAgAlias() {
        return agAlias;
    }

    private void setAgAlias(String agAlias) {
        this.agAlias = agAlias;
    }

    public String getMovingDestination() {
        return aliasDestinationAdmBESA;
    }

    private void setMovingDestination(String movingDestination) {
        this.aliasDestinationAdmBESA = movingDestination;
    }

    public double getAgPassword() {
        return agPassword;
    }

    private void setAgPassword(double agPassword) {
        this.agPassword = agPassword;
    }

    public ArrayList<String> getDescriptors() {
        return descriptors;
    }

    private String getDescriptorsToString() {
        String descriptorsToString = "";
        if (descriptors.size() > 0) {
            for (int i = 0; i < descriptors.size(); i++) {
                descriptorsToString += descriptors.get(i) + descriptorsSpt;
            }
            return descriptorsToString.substring(0, descriptorsToString.length() - 1);
        }
        return descriptorsToString;
    }

    private void setDescriptors(ArrayList<String> descriptors) {
        this.descriptors = descriptors;
    }

    private void setDescriptors(String descriptors) {
        this.descriptors.clear();
        if (descriptors.equals("")) {
            return;
        }
        String[] descriptorsVector = descriptors.split(descriptorsSpt);
//        for (int i = 0; i < descriptorsVector.length; i++) {
//            this.descriptors.add(descriptorsVector[i]);
//        }
        this.descriptors.addAll(Arrays.asList(descriptorsVector));
    }

    public boolean hasDescriptors() {
        return hasDescriptors;
    }

    private void setHasDescriptors(boolean hasDescriptors) {
        this.hasDescriptors = hasDescriptors;
    }

    public String getDirectoryServiceName() {
        return directoryServiceName;
    }

    private void setDirectoryServiceName(String directoryServiceName) {
        this.directoryServiceName = directoryServiceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    private void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        String messageString = "";
        messageString += super.toString() + strSeparatorB;
        messageString += getAction() + strSeparatorB;
        messageString += getAgId() + strSeparatorB;
        messageString += getAgAlias() + strSeparatorB;
        messageString += getAgPassword() + strSeparatorB;
        messageString += getServiceId() + strSeparatorB;
        messageString += getDirectoryServiceName() + strSeparatorB;
        messageString += getDescriptorsToString() + strSeparatorB;
        messageString += hasDescriptors()?"true":"false";
        if (getAction() == Action.MOVE) {
            messageString += strSeparatorB + getMovingDestination();
        }
        return messageString;
    }

    @Override
    protected void buildMessage(String messageString) {
        String split[] = messageString.split(MessageBPO.strSeparatorB);
        buildMessage(split[0], messageType);
        setAction(split[1]);
        setAgId(split[2]);
        setAgAlias(split[3]);
        setAgPassword(Double.valueOf(split[4]));
        setServiceId(split[5]);
        setDirectoryServiceName(split[6]);
        setDescriptors(split[7]);
        setHasDescriptors(split[8].equals("true")?true:false);
        if (split.length == 10) {
            setMovingDestination(split[9]);
        }
    }

    public static enum Action {
        CREATE, KILL, MOVE, DOESAGENTEXIST, ADDSERVICE, BINDSERVICE, BINDSPSERVICEINDIRECTORY
    }
}
