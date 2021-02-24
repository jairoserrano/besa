/*
 * @(#)SocketServerBAP.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern.InputBAP;

import BESA.Extern.ExternAdmBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the access point for others BAP.
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class SocketServerBAP extends Thread {

    /**
     * Reference to BESA administrator.
     */
    private ExternAdmBESA admLocal;
    /**
     * BAP port.
     */
    private int bapPort;
    /**
     * Connections list.
     */
    private List<SocketBAP> conexionesBAP = new ArrayList<SocketBAP>();
    /**
     * Server socket.
     */
    private ServerSocket socketServerBAP = null;

    /**
     * Creates a new instance.
     *
     * @param admLocal BESA administrator.
     * @param bapPort BAP port.
     */
    public SocketServerBAP(ExternAdmBESA admLocal, int bapPort) {
        this.admLocal = admLocal;
        this.bapPort = bapPort;
    }

    /**
     * Gets the BAP connections.
     *
     * @return BAP connections.
     */
    public List<SocketBAP> getConexionesBAP() {
        return this.conexionesBAP;
    }

    /**
     * Gets the BESA administrator.
     * 
     * @return BESA administrator.
     */
    public ExternAdmBESA getAdmLocal() {
        return this.admLocal;
    }

    /**
     * Starts the BAP socket server.
     *
     * @return true if the socket server started with success or false in other
     * case.
     */
    public boolean startSocketServerBAP() throws ExternInputExceptionBESA {
        try {
            this.socketServerBAP = new ServerSocket(this.bapPort);
            return true;
        } catch (IOException ex) {
            ReportBESA.debug("[SocketServerBAP::startSocketServerBAP] Couldn't start the BAP server socket in the port: " + this.bapPort + ":" + ex.toString());
            throw new ExternInputExceptionBESA("Couldn't start the BAP server socket in the port: " + this.bapPort + ":" + ex.toString());
        }
    }

    /**
     * Accepts the connections of the BAP clients and adds the new connections
     * to connections list.
     */
    public void run() {
        try {
            while (this.admLocal.isAlive()) {                                   //Checks if the local administrator is active.
                ReportBESA.debug("Esperando peticion de BAP");
                SocketBAP sb = new SocketBAP(admLocal, this.socketServerBAP.accept());//Accepts BAP clients.
                ReportBESA.debug("Se ha conectado un BAP");
                /*synchronized (this) {
                    this.conexionesBAP.add(sb);                                 //Add the new connection to connections list.
                }*/
                sb.start();                                                     //Starts the attention of the BAP client.
            }
        } catch (IOException ex) {
            ReportBESA.debug("[SocketServerBAP::run] Couldn't accept connections in the port: " + this.bapPort + ":" + ex.toString());
            try {
                throw new ExternInputExceptionBESA("Couldn't accept connections in the port: " + this.bapPort + ":" + ex.toString());
            } catch (ExternInputExceptionBESA ex1) {
                ex1.printStackTrace();
            }
        } catch (ExternInputExceptionBESA e) {
            ReportBESA.error(e.toString());
            e.printStackTrace();
        }
        try {
            this.socketServerBAP.close();
        } catch (IOException ex) {
            ReportBESA.debug("Couldn't stop the connections on port: " + this.bapPort + ":" + ex.toString());
            try {
                throw new ExternInputExceptionBESA("Couldn't stop the connections on port: " + this.bapPort + ":" + ex.toString());
            } catch (ExternInputExceptionBESA ex1) {
                ex1.printStackTrace();
            }
        }
    }
}
