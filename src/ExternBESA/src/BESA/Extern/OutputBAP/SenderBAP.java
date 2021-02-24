/*
 * @(#)SenderBAP.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved. Takina
 * and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern.OutputBAP;

import BESA.Extern.BAP;
import BESA.Extern.Data.MessageBAP;
import BESA.Extern.ExternAdmBESA;
import BESA.Extern.ExternExceptionBESA;
import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.Event.KernellAgentEventExceptionBESA;
import BESA.Log.ReportBESA;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This class represents
 *
 * @author SIDRe - Pontificia Universidad Javeriana
 * @author Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since JDK1.0
 */
public class SenderBAP {

    /**
     *
     */
    private ExternAdmBESA admLocal;

    /**
     *
     * @param admLocal
     */
    public SenderBAP(ExternAdmBESA admLocal) {
        this.admLocal = admLocal;
    }

    /**
     *
     * @param ev
     * @param uri
     */
    public void sendEvent(EventBESA ev, String uri, String bPOLocation, String command) throws ExternExceptionBESA, KernellAgentEventExceptionBESA {
        StringTokenizer stk = new StringTokenizer(ev.getData().getClass().getPackage().getName(), ".");
        ArrayList<String> tokens = new ArrayList<String>();
        while (stk.hasMoreTokens()) {
            String token = new String(stk.nextToken());
            tokens.add(token);
        }
        String byteArray = (command
                + BAP.BAP_DELIMITER + this.admLocal.getIdAdm()
                + BAP.BAP_DELIMITER + uri
                + BAP.BAP_DELIMITER + ev.getType()
                + BAP.BAP_DELIMITER + tokens.get(0)
                + BAP.BAP_DELIMITER + ev.getData().getClass().getName());
                //+ BAP.BAP_DELIMITER + ev.getData().getStringFromDataBesa());//.getBytes();
        
        if (bPOLocation != null) {
            try {
                this.sendByteArray(byteArray, bPOLocation.split(BAP.BAP_DELIMITER)[0], Integer.parseInt(bPOLocation.split(BAP.BAP_DELIMITER)[1]),
                        ev.getData());
            } catch (Exception ex) {
                ReportBESA.error("[SenderBAP::sendEvent] Couldn't send the event to remote agent " + uri + ": " + ex.toString());
                throw new ExternExceptionBESA("Couldn't send the event to remote agent " + uri + ": " + ex.toString());
            }
        } else {
            ReportBESA.error("[SenderBAP::sendEvent] The agent " + uri + " doesn't exist into application environments.");
            throw new ExternExceptionBESA("The agent " + uri + " doesn't exist into application environments.");
        }
    }

    /**
     *
     * @param bapSouAdd
     * @param bapSouPort
     * @param bapDesAdd
     * @param bapDesPort
     * @param url
     */
    public void lookUpAgent(String bapSouAdd, int bapSouPort, String bapDesAdd, int bapDesPort, String url, String command) throws ExternExceptionBESA {
        String byteArray = (command
                + BAP.BAP_DELIMITER + bapSouAdd
                + BAP.BAP_DELIMITER + bapSouPort
                + BAP.BAP_DELIMITER + url);//.getBytes();
        try {
            this.sendByteArray(byteArray, bapDesAdd, bapDesPort, null);
        } catch (Exception ex) {
            ReportBESA.error("[SenderBAP::lookUpAgent] Couldn't send the lookup message of the agent " + url + ": " + ex.toString());
            throw new ExternExceptionBESA("Couldn't send the lookup message of the agent " + url + ": " + ex.toString());
        }
    }

    /**
     *
     * @param bapSouAdd
     * @param bapSouPort
     * @param bapDesAdd
     * @param bapDesPort
     * @param url
     */
    public void sendAgentFound(String bapSouAdd, int bapSouPort, String bapDesAdd, int bapDesPort, String url, String command) throws ExternExceptionBESA {
        String byteArray = (command
                + BAP.BAP_DELIMITER + bapSouAdd
                + BAP.BAP_DELIMITER + bapSouPort
                + BAP.BAP_DELIMITER + url);//.getBytes();
        try {
            this.sendByteArray(byteArray, bapDesAdd, bapDesPort, null);
        } catch (Exception ex) {
            ReportBESA.error("[SenderBAP::sendAgentFound] Couldn't send the agent found message of the agent " + url + ": " + ex.toString());
            throw new ExternExceptionBESA("Couldn't send the agent found message of the agent " + url + ": " + ex.toString());
        }
    }

    public void sendAgentDestroy(String bapSouAdd, int bapSouPort, String bapDesAdd, int bapDesPort, String agAlias) throws ExternExceptionBESA {
        String byteArray = (BAP.SEND_AGENT_DESTROY
                + BAP.BAP_DELIMITER + bapSouAdd
                + BAP.BAP_DELIMITER + bapSouPort
                + BAP.BAP_DELIMITER + agAlias);//.getBytes();
        try {
            this.sendByteArray(byteArray, bapDesAdd, bapDesPort, null);
        } catch (Exception ex) {
            ReportBESA.error("[SenderBAP::sendAgentDestroy] Couldn't send the agent destroy message of the agent " + agAlias + ": " + ex.toString());
            throw new ExternExceptionBESA("Couldn't send the agent destroy message of the agent " + agAlias + ": " + ex.toString());
        }
    }

    /**
     *
     * @param ack
     * @param bapAdd
     * @param bapPort
     */
    public void sendACK(String ack, String bapAdd, int bapPort) throws ExternExceptionBESA {
        try {
            this.sendByteArray(ack, bapAdd, bapPort, null);//ack.getBytes()
        } catch (Exception ex) {
            ReportBESA.error("[SenderBAP::sendACK] Couldn't send the agent destroy message of the BAP " + bapAdd + ": " + ex.toString());
            throw new ExternExceptionBESA("Couldn't send the agent destroy message of the BAP " + bapAdd + ": " + ex.toString());
        }
    }

    /**
     *
     * @param byteArray
     * @param bapAdd
     * @param bapPort
     */
    public void sendByteArray(String command, String bapAdd, int bapPort, DataBESA data) throws Exception {
        Socket socket = new Socket(bapAdd, bapPort);

        MessageBAP mbap = new MessageBAP();
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        mbap.setCommand(command);
        mbap.setData(data);
        oos.writeObject(mbap);

        ReportBESA.debug("Send: " + command);

        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        MessageBAP messageBAP = (MessageBAP) ois.readObject();
        
        if (messageBAP.getCommand().equalsIgnoreCase("ACK")) {
            ReportBESA.debug("ACK Recieved of " + command);
            oos.close();
            ois.close();
        } else {
            throw new ExternExceptionBESA("Couldn't send data");
        }
    }
}
