/*
 * @(#)SocketBAP.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved. Takina
 * and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern.InputBAP;

import BESA.ExceptionBESA;
import BESA.Extern.BAP;
import BESA.Extern.Data.MessageBAP;
import BESA.Extern.Directory.AgExternHandlerBESA;
import BESA.Extern.ExternAdmBESA;
import BESA.Extern.ExternExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BESA.Remote.Directory.RemoteAdmHandlerBESA;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a BAP client.
 *
 * @author SIDRe - Pontificia Universidad Javeriana
 * @author Takina - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since JDK1.0
 */
public class SocketBAP extends Thread {

    /**
     * Flah that representes the state of thread.
     */
    private boolean alive;
    /**
     * Client socket.
     */
    private Socket socket;
    /**
     * Input stream.
     */
    private ObjectInputStream inStream;
    /**
     * Adm BESA.
     */
    private ExternAdmBESA adm;
    /**
     * Count window.
     */
    private int window;

    /**
     * Creates a new instance of the BAP client.
     *
     * @param socketServerBAP BAP socket server.
     * @param socket BAP client that has received.
     */
    public SocketBAP(ExternAdmBESA adm, Socket socket) throws ExternInputExceptionBESA {
        this.alive = true;
        this.window = -1;
        this.socket = socket;
        this.adm = adm;
        try {
            InputStream is = socket.getInputStream();
            inStream = new ObjectInputStream(is);
        } catch (IOException e) {
            ReportBESA.error("[SocketBAP::SocketBAP] Couldn't get the connection with client socket of the BAP peer:" + e.toString());
            throw new ExternInputExceptionBESA("Couldn't get the connection with client socket of the BAP peer:" + e.toString());
        }
    }

    /**
     * This method receives the messages from other BAP.
     */
    @Override
    public void run() {
        MessageBAP receivedMsg = receiveMsg();                                  //Waits and receive the message
        if (receivedMsg == null) {
            return;
        }
        ReportBESA.debug("Receive the command: " + receivedMsg.getCommand());
        //--------------------------------------------------------------------//
        // Decomposes the message with help of the BAP delimiter.             //
        //--------------------------------------------------------------------//
        String delimiter = new String(BAP.BAP_DELIMITER);
        StringTokenizer stk = new StringTokenizer(receivedMsg.getCommand(), delimiter);
        ArrayList<String> tokens = new ArrayList<String>();
        while (stk.hasMoreTokens()) {
            String token = new String(stk.nextToken());
            tokens.add(token);
        }
        String msgType = new String((String) tokens.get(0));                    //Gets the first element of the message for identified the primitive.


        AgHandlerBESA ah = null;
        switch (msgType) {
            case BAP.LOOK_UP_MSN:
                Enumeration<String> containers = adm.getAdmAliasList();
                int size = 0;
                String response = "ERROR";
                ArrayList<String> aux = new ArrayList<String>();
                while (containers.hasMoreElements()) {
                    aux.add(containers.nextElement());
                    size++;
                }
                if (size > 0) {
                    if (window > size) {
                        window = 0;
                    } else {
                        window++;
                    }
                    response = aux.get(window);
                    RemoteAdmHandlerBESA admHandler = (RemoteAdmHandlerBESA) adm.getAdmByAlias(response);
                    response = admHandler.getIpRmiRegistry();
                } else {
                    response = adm.getConfigBESA().getAliasContainer() + "/" + adm.getConfigBESA().getIpaddress() + ":" + adm.getConfigBESA().getBpoPort();
                }
                try {
                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    MessageBAP messageBAP = new MessageBAP();
                    messageBAP.setCommand(response);
                    oos.writeObject(messageBAP);
                } catch (IOException ex) {
                    ReportBESA.error(ex);
                }
                ReportBESA.debug("Send ACK of " + msgType);
                return;
            case BAP.CONTAINER_LOCATION_MSN:
                RemoteAdmHandlerBESA admHandlerBESA = (RemoteAdmHandlerBESA) adm.getAdmByAlias(tokens.get(1));
                try {
                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    MessageBAP messageBAP = new MessageBAP();
                    messageBAP.setCommand(admHandlerBESA.getIpRmiRegistry());
                    oos.writeObject(messageBAP);
                } catch (IOException ex) {
                    ReportBESA.error(ex);
                }
                ReportBESA.debug("Send ACK of " + msgType);
                return;
            case BAP.SEND_EVENT_BAP_BY_ID:
                try {
                    ah = adm.getHandlerByAid(tokens.get(2));
                } catch (ExceptionBESA ex) {
                    ReportBESA.error(ex);
                }
                if (ah != null && (!(ah instanceof AgExternHandlerBESA))) {
                    try {
                        EventBESA ev = new EventBESA(tokens.get(3), receivedMsg.getData());
                        ah.sendEvent(ev);
                    } catch (Exception ex) {
                        ReportBESA.error("[SocketBAP::run] " + ex.toString());
                        ex.printStackTrace();
                    }
                }
                break;
            case BAP.SEND_EVENT_BAP_BY_ALIAS:
                try {
                    ah = adm.getHandlerByAlias(tokens.get(2));
                } catch (ExceptionBESA ex) {
                    ReportBESA.error(ex);
                }
                if (ah != null && (!(ah instanceof AgExternHandlerBESA))) {
                    try {
                        EventBESA ev = new EventBESA(tokens.get(3), receivedMsg.getData());
                        ah.sendEvent(ev);
                    } catch (Exception ex) {
                        ReportBESA.error("[SocketBAP::run] " + ex.toString());
                        ex.printStackTrace();
                    }
                }
                break;
            case BAP.SEND_LOOKUP_AGENT_BY_ID:
                try {
                    ah = adm.getHandlerByAid(tokens.get(3));
                } catch (ExceptionBESA ex) {
                    Logger.getLogger(SocketBAP.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (ah != null && (!(ah instanceof AgExternHandlerBESA))) {
                    try {
                        adm.getBap().sendAgentFound(tokens.get(1), Integer.parseInt(tokens.get(2)), tokens.get(3), BAP.SEND_AGENT_FOUND_BY_ID);
                    } catch (ExternExceptionBESA ex) {
                        ReportBESA.error("[SocketBAP::run] " + ex.toString());
                        ex.printStackTrace();
                    }
                }
                break;
            case BAP.SEND_LOOKUP_AGENT_BY_ALIAS:
                try {
                    ah = adm.getHandlerByAlias(tokens.get(3));
                } catch (ExceptionBESA ex) {
                    Logger.getLogger(SocketBAP.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (ah != null && (!(ah instanceof AgExternHandlerBESA))) {
                    try {
                        adm.getBap().sendAgentFound(tokens.get(1), Integer.parseInt(tokens.get(2)), tokens.get(3), BAP.SEND_AGENT_FOUND_BY_ALIAS);
                    } catch (ExternExceptionBESA ex) {
                        ReportBESA.error("[SocketBAP::run] " + ex.toString());
                        ex.printStackTrace();
                    }
                }
                break;
            case BAP.SEND_AGENT_FOUND_BY_ID:
                adm.getBap().setAgentLocationByID(tokens.get(1), Integer.parseInt(tokens.get(2)), tokens.get(3));
                break;
            case BAP.SEND_AGENT_FOUND_BY_ALIAS:
                adm.getBap().setAgentLocationByAlias(tokens.get(1), Integer.parseInt(tokens.get(2)), tokens.get(3));
                break;
            case BAP.SEND_AGENT_DESTROY:
                adm.getBap().setAgentDestroy(tokens.get(1), Integer.parseInt(tokens.get(2)), tokens.get(3));
                break;
        }
        //--------------------------------------------------------------------//
        // Send ACK.                                                          //
        //--------------------------------------------------------------------//        
        try {
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            MessageBAP messageBAP = new MessageBAP();
            messageBAP.setCommand("ACK");
            oos.writeObject(messageBAP);
            ReportBESA.debug("Send ACK of " + msgType);
        } catch (IOException ex) {
            Logger.getLogger(SocketBAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Read the object message from stream.
     *
     * @return Object message.
     */
    private MessageBAP receiveMsg() {
        try {
            return (MessageBAP) inStream.readObject();
        } catch (IOException ex) {
            try {
                throw new ExternInputExceptionBESA("Happened an error into reading the data input stream of the BAP client socket:" + ex.toString());
            } catch (ExternInputExceptionBESA e) {
                ReportBESA.error("[SocketBAP::run] " + e.toString());
                ex.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException ex) {
            try {
                throw new ExternInputExceptionBESA("The class can not build because missing .class file: " + ex.toString());
            } catch (ExternInputExceptionBESA e) {
                ReportBESA.error("[SocketBAP::run] " + e.toString());
                ex.printStackTrace();
                return null;
            }
        }
    }
}
