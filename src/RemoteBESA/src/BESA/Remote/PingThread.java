/*
 * @(#)PingThread.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Remote;

import BESA.Remote.RemoteAdmBESA;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import BESA.Kernel.System.Directory.AdmHandlerBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Log.ReportBESA;
import BESA.Remote.Directory.RemoteAdmHandlerBESA;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents
 * PingThread manejo del envio quasi-periodico de los mensajes de multicast
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class PingThread extends Thread {

    /**
     *
     */
    protected RemoteAdmBESA admLocal;
    /**
     * 
     */
    protected DatagramSocket socket;
    /**
     * 
     */
    protected int msgStamp;
    /**
     * 
     */
    protected final long MAX_INTER_MSG_DELAY = (long) 15000;

    /**
     * PingThread construye un hilo que env�a mensajes de multicast
     * @param admLocal referencia al administrador local que crea el thread
     */
    public PingThread(RemoteAdmBESA admLocal) {
        super("PingThread");
        this.admLocal = admLocal;
        this.msgStamp = 0;
    }

    /**
     *
     * @param msgType
     */
    public void sendMulticast(String msgType) throws SystemExceptionBESA {
        //Notese que este metodo es s�lo para anunciar cambios en el contenedor
        // caso mensajes ciclo vida del contenedor
        if (msgType.equals(RemoteAdmBESA.MSG_MULTICAST_ADM_ALIVE) || msgType.equals(RemoteAdmBESA.MSG_MULTICAST_ADM_KILL)) {
            // construir mensaje a enviar
            // 0.msgType 1.admAlias 2.ipAddress 3.rmiPort 4.msgStamp 5. id
            RemoteAdmHandlerBESA admHandler = (RemoteAdmHandlerBESA) admLocal.getAdmHandler();
            String msg = new String(msgType + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getAlias() + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getIpRmiRegistry() + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getPortRmiRegistry() + RemoteAdmBESA.MULTICAST_DELIMITER
                    + (msgStamp++) + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getAdmId()
                    );

            try {
                ReportBESA.trace("Adm MULTICAST Despachando:" + msg);
                ReportBESA.trace("Adm Despachando puerto:" + admLocal.getMulticastPort());
                DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),
                        admLocal.getMulticastInetAddr(),
                        admLocal.getMulticastPort());
                admLocal.getMulticastSocket().send(packet);
            } catch (IOException e) {
                ReportBESA.error("Couldn't send multicast data: " + e.toString());
                throw new SystemExceptionBESA("Couldn't send multicast data: " + e.toString());
            }
        }//fin if
    }

    /**
     *
     * @param msgType
     * @param agId
     */
    public void updateAgentbyMulticast(String msgType, String agId, String alias) throws SystemExceptionBESA {
        //Notese que este metodo es s�lo para anunciar cambios en los agentes
        // caso mensajes ciclo vida del agente
        if (msgType.equals(RemoteAdmBESA.MSG_MULTICAST_AGENT_CREATE) || msgType.equals(RemoteAdmBESA.MSG_MULTICAST_AGENT_DESTROY)) {
            // construir mensaje a enviar
            //0. MsgType, 1.admAlias, 2. agId, 3. admId y 4.msgStamp
            AdmHandlerBESA admHandler = admLocal.getAdmHandler();
            //----------------------------------------------------------------//
            //
            //----------------------------------------------------------------//
            String msg = new String(
                    msgType + RemoteAdmBESA.MULTICAST_DELIMITER
                    + alias + RemoteAdmBESA.MULTICAST_DELIMITER
                    + agId + RemoteAdmBESA.MULTICAST_DELIMITER
                    + admHandler.getAdmId() + RemoteAdmBESA.MULTICAST_DELIMITER
                    + (msgStamp++));
            try {
                ReportBESA.trace("Agent MULTICAST Despachando:" + msg);
                ReportBESA.trace("Agent Despachando puerto:" + admLocal.getMulticastPort());
                DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),
                        admLocal.getMulticastInetAddr(),
                        admLocal.getMulticastPort());
                admLocal.getMulticastSocket().send(packet);
            } catch (IOException e) {
                ReportBESA.error("Couldn't send update agent by multicast message: " + e.toString());
                throw new SystemExceptionBESA("Couldn't send update agent by multicast message: " + e.toString());
            }
            //----------------------------------------------------------------//
            //
            //----------------------------------------------------------------//

        }//fin if
    }

    /**
     *
     * @param msgType
     * @param agId
     * @param aliasAdmR
     */
    public void updateAgentbyMulticastMove(String msgType, String agId, String aliasAdmR) throws SystemExceptionBESA {
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
            try {
                ReportBESA.trace("Agent MULTICAST Despachando:" + msg);
                ReportBESA.trace("Agent Despachando puerto:" + admLocal.getMulticastPort());
                DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),
                        admLocal.getMulticastInetAddr(),
                        admLocal.getMulticastPort());
                admLocal.getMulticastSocket().send(packet);
            } catch (IOException e) {
                ReportBESA.error("Couldn't send update agent by multicast message: " + e.toString());
                throw new SystemExceptionBESA("Couldn't send update agent by multicast move message: " + e.toString());
            }
        }//fin if
    }

    /**
     *
     * @param msgType
     * @param agId
     */
    public void updateStateAgent(String msgType, String agId) throws SystemExceptionBESA {
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
            try {
                ReportBESA.trace("Agent MULTICAST Despachando:" + msg);
                ReportBESA.trace("Agent Despachando puerto:" + admLocal.getMulticastPort());
                DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),
                        admLocal.getMulticastInetAddr(),
                        admLocal.getMulticastPort());
                admLocal.getMulticastSocket().send(packet);
            } catch (IOException e) {
                ReportBESA.error("Couldn't send update state agent message: " + e.toString());
                throw new SystemExceptionBESA("Couldn't send update state agent message: " + e.toString());
            }
        }//fin if
    }

    /**
     * run responsable de enviar los mensajes de broadcast
     * NOTA:
     *    Deber�a enviar la informaci�n apropiada.
     */
    public void run() {
        // ciclo infinito quasiperiodico- DETECTAR DALLAS PENDIENTE
        while (admLocal.isAlive()) {
            try {
                // enviar mensaje PING
                this.sendMulticast(RemoteAdmBESA.MSG_MULTICAST_ADM_ALIVE);
            } catch (SystemExceptionBESA ex) {
                ReportBESA.error("Couldn't send multicast administrator alive message: " + ex.toString());
                ex.printStackTrace();
            }



            ReportBESA.trace("Envio multicast:" + RemoteAdmBESA.MSG_MULTICAST_ADM_ALIVE);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PingThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//fin run
}//fin PingThread

