/*//TODO Copia.
 * @(#)SocketServer.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Remote;

import BESA.Kernel.System.AdmBESA;
import BESA.Remote.RemoteAdmBESA;
import BESA.Log.ReportBESA;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents 
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class SocketServer extends Thread {

    /**
     *
     */
    private AdmBESA admLocal;
    /**
     * 
     */
    @SuppressWarnings("unused")
    private final int MULTICAST_ADM_TOKEN_QUANTITY = 5;
    /**
     * 
     */
    @SuppressWarnings("unused")
    private final int MULTICAST_AGENT_TOKEN_QUANTITY = 5;
    /**
     *
     */
    @SuppressWarnings("unused")
    private final int MULTICAST_AGENT_SET_STATE = 3;
    /**
     *
     */
    @SuppressWarnings("unused")
    private final int MULTICAST_MOVE_AGENT_TOKEN_QUANTITY = 6;
    /**
     * Coleccion sincronizada de conexiones para enviar
     */
    private List<SocketServerThread> conexiones = Collections.synchronizedList(new ArrayList<SocketServerThread>());
    /**
     *
     */
    private int port;
    /**
     *
     */
    private ServerSocket serverSocket = null;

    /**
     * Creates a new instance of SocketServer
     * @param admLocal
     * @param port
     */
    public SocketServer(RemoteAdmBESA admLocal, int port) {
        super("SocketServer");
        this.admLocal = admLocal;
        this.port = port;
    }

    /**
     *
     * @return
     */
    public Iterator getConexiones() {
        return conexiones.iterator();
    }

    /**
     *
     * @return
     */
    public boolean startServerSocket() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            ReportBESA.trace("Could not init server on port: " + port);
            return false;
        }
        return true;
    }

    /**
     * 
     */
    public void run() {
        try {
            while (admLocal.isAlive()) {
                SocketServerThread sst = new SocketServerThread(
                        this, serverSocket.accept());
                synchronized (this) {
                    conexiones.add(sst);
                }
                sst.start();
            }
        } catch (IOException e) {
            ReportBESA.error("Could not accept on port: " + port);
        }


        try {
            serverSocket.close();
        } catch (IOException e) {
            ReportBESA.error("Could not close on port: " + port);
        }
    }
}
