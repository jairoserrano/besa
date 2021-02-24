/*
 * @(#)PingSocketThread.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Remote;

import BESA.Remote.RemoteAdmBESA;
import java.io.IOException;
import java.io.PrintWriter;
import BESA.Kernel.System.Directory.AdmHandlerBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Log.ReportBESA;
import BESA.Remote.Directory.RemoteAdmHandlerBESA;

/**
 * This thread is responsible for notifying the other container that is alive.
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class PingSocketThread extends PingThread {

    /**
     *
     */
    private PrintWriter out = null;

    /**
     * Creates a new instance of PingSocketThread
     * @param admLocal
     */
    public PingSocketThread(RemoteAdmBESA admLocal) throws SystemExceptionBESA {
        super(admLocal);
        try {
            out = new PrintWriter(admLocal.getSocketPingPong().getOutputStream(), true);
        } catch (IOException e) {
            ReportBESA.error("The PingSocket couldn't get connection: " + e.toString());
            throw new SystemExceptionBESA("The PingSocket couldn't get connection: " + e.toString());
        }
    }

    /**
     *
     * @param msgType
     */
    public void sendMulticast(String msgType) {
        //Notese que este metodo es s�lo para anunciar cambios en el contenedor
        // caso mensajes ciclo vida del contenedor
        if (msgType.equals(RemoteAdmBESA.MSG_MULTICAST_ADM_ALIVE) || msgType.equals(RemoteAdmBESA.MSG_MULTICAST_ADM_KILL)) {
            // construir mensaje a enviar
            // 0.msgType 1.admAlias 2.ipAddress 3.rmiPort 4.msgStamp
            RemoteAdmHandlerBESA admHandler = (RemoteAdmHandlerBESA) admLocal.getAdmHandler();
            String msg = new String(msgType + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getAlias() + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getIpRmiRegistry() + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getPortRmiRegistry() + RemoteAdmBESA.MULTICAST_DELIMITER
                    + (msgStamp++));

            //try {
            ReportBESA.trace("Adm MULTICAST Despachando:" + msg);
            ReportBESA.trace("Adm Despachando puerto:" + admLocal.getMulticastPort());

            //Envia por el puerto
            out.println(msg);

            /*
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),
            admLocal.getMulticastInetAddr(),
            admLocal.getMulticastPort());
            admLocal.getMulticastSocket().send(packet);
             */
            /*
            } catch (IOException e) {
            RobocoopDirectoryNameGenerator.error("[PingThread::send] " + "Adm-IOException:" +  e.getMessage());
            }*/
        }//fin if
    }

    /**
     *
     * @param msgType
     * @param agId
     */
    @Override
    public void updateAgentbyMulticast(String msgType, String agId, String alias) {
        //Notese que este metodo es s�lo para anunciar cambios en los agentes
        // caso mensajes ciclo vida del agente
        if (msgType.equals(RemoteAdmBESA.MSG_MULTICAST_AGENT_CREATE) || msgType.equals(RemoteAdmBESA.MSG_MULTICAST_AGENT_DESTROY)) {
            // construir mensaje a enviar
            //0. MsgType, 1.admAlias, 2. agId, 3. admId y 4.msgStamp
            AdmHandlerBESA admHandler = admLocal.getAdmHandler();
            String msg = new String(msgType + RemoteAdmBESA.MULTICAST_DELIMITER
                    + alias + RemoteAdmBESA.MULTICAST_DELIMITER
                    + agId + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getAdmId() + RemoteAdmBESA.MULTICAST_DELIMITER
                    + (msgStamp++));
            //try {
            ReportBESA.trace("Agent MULTICAST Despachando:" + msg);
            ReportBESA.trace("Agent Despachando puerto:" + admLocal.getMulticastPort());

            out.println(msg);

            /*
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),
            admLocal.getMulticastInetAddr(),
            admLocal.getMulticastPort());
            admLocal.getMulticastSocket().send(packet);
             */
            /*
            } catch (IOException e) {
            RobocoopDirectoryNameGenerator.error("[PingThread::send] " + "Agent-IOException:" +  e.getMessage());
            }*/
        }//fin if
    }

    /**
     *
     * @param msgType
     * @param agId
     * @param aliasAdmR
     */
    public void updateAgentbyMulticastMove(String msgType, String agId, String aliasAdmR) {
        //Notese que este metodo es s�lo para anunciar cambios en los agentes
        // caso mensajes ciclo vida del agente
        if (msgType.equals(RemoteAdmBESA.MSG_MULTICAST_AGENT_MOVE)) {
            // construir mensaje a enviar
            //0. MsgType, 1.agAlias, 2. agId, 3. admId, 4 AdmRemoteHandler y 5.msgStamp
            AdmHandlerBESA admHandler = admLocal.getAdmHandler();
            String msg = new String(msgType + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admLocal.getAliasByAid(agId) + RemoteAdmBESA.MULTICAST_DELIMITER
                    + agId + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getAdmId() + RemoteAdmBESA.MULTICAST_DELIMITER
                    + aliasAdmR + RemoteAdmBESA.MULTICAST_DELIMITER
                    + (msgStamp++));

            //try {
            ReportBESA.trace("Agent MULTICAST Despachando:" + msg);
            ReportBESA.trace("Agent Despachando puerto:" + admLocal.getMulticastPort());

            out.println(msg);
            /*
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),
            admLocal.getMulticastInetAddr(),
            admLocal.getMulticastPort());
            admLocal.getMulticastSocket().send(packet);
            } catch (IOException e) {
            RobocoopDirectoryNameGenerator.error("[PingThread::send] " + "Agent-IOException:" +  e.getMessage());
            }*/
        }//fin if
    }

    /**
     *
     * @param msgType
     * @param agId
     */
    public void updateStateAgent(String msgType, String agId) {
        //Notese que este metodo es s�lo para anunciar cambios en los agentes
        // caso mensajes ciclo vida del agente
        if (msgType.equals(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_MOVE)
                || msgType.equals(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_ALIVE)) {
            // construir mensaje a enviar
            //0. MsgType, 1.agId,y 2.msgStamp
            //TODO: AdmHandler admHandler = admLocal.getAdmHandler();
            String msg = new String(msgType + RemoteAdmBESA.MULTICAST_DELIMITER
                    + agId + RemoteAdmBESA.MULTICAST_DELIMITER
                    + (msgStamp++));
            //try {
            ReportBESA.trace("Agent MULTICAST Despachando:" + msg);
            ReportBESA.trace("Agent Despachando puerto:" + admLocal.getMulticastPort());

            out.println(msg);

            /*
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),
            admLocal.getMulticastInetAddr(),
            admLocal.getMulticastPort());
            admLocal.getMulticastSocket().send(packet);
            } catch (IOException e) {
            RobocoopDirectoryNameGenerator.error("[PingThread::send] " + "Agent-IOException:" +  e.getMessage());
            }*/
        }//fin if
    }

    /**
     * run responsable de enviar los mensajes de broadcast
     * NOTA:
     *    Deber�a enviar la informaci�n apropiada.
     */
    public void run() {
        // ciclo infinito quasiperiodico- DETECTAR DALLAS PENDIENTE
//        while (admLocal.isAlive()) {
        // enviar mensaje PING
        this.sendMulticast(RemoteAdmBESA.MSG_MULTICAST_ADM_ALIVE);
        ReportBESA.trace("Envio multicast:" + RemoteAdmBESA.MSG_MULTICAST_ADM_ALIVE);
    }//fin run
}
