/*//TODO Copia.
 * @(#)SocketServerThread.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Remote;

import BESA.Remote.SocketServer;
import BESA.Log.ReportBESA;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

/**
 * This class represents 
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class SocketServerThread extends Thread {

    /**
     *
     */
    private Socket socket = null;
    /**
     * 
     */
    private SocketServer socketServer = null;
    /**
     * 
     */
    private PrintWriter out = null;
    /**
     * 
     */
    private BufferedReader in = null;

    /**
     * Creates a new instance of SocketServerThread
     * @param socketServer
     * @param socket
     */
    public SocketServerThread(SocketServer socketServer, Socket socket) {
        super("SocketServerThread");
        this.socket = socket;
        this.socketServer = socketServer;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(
                    socket.getInputStream()));
        } catch (IOException e) {
            ReportBESA.error(e.toString());
        }
    }

    /**
     *
     */
    public void run() {
        try {
            String inputLine;
            @SuppressWarnings("unused")
            String outputLine;
            outputLine = this.processInput(null);
            //out.println(outputLine);
            while ((inputLine = in.readLine()) != null) {
                outputLine = this.processInput(inputLine);
                //out.println(outputLine);
                /*
                if (outputLine.equals("Bye"))
                break;
                 */
            }

        } catch (IOException e) {
            ReportBESA.trace(e.toString());
        }
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            ReportBESA.trace(e.toString());
        }
    }

    /**
     *
     * @param theInput
     * @return
     */
    public String processInput(String theInput) {
        if (theInput == null) {
            //Al inicio
            return null;
        } else {
            //Reenviar a todos los otros sockets, para ello debo asegurarme de
            //que el pool de conexiones no sea alterado mientras lo hago
            //Es un esquema criticable por todo lado, pero dado que el objetivo
            //es permitir que varios contenedores en la misma m�quina puedan
            //comunicarse, esta bien
            synchronized (socketServer) {
                for (Iterator it = socketServer.getConexiones(); it.hasNext();) {
                    SocketServerThread sst = (SocketServerThread) it.next();
                    sst.getOut().println(theInput);
                }
            }
            return theInput;
        }
    }

    /**
     *
     * @return
     */
    public PrintWriter getOut() {
        return out;
    }

    /**
     *
     * @param out
     */
    public void setOut(PrintWriter out) {
        this.out = out;
    }

    /**
     *
     * @return
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     *
     * @param in
     */
    public void setIn(BufferedReader in) {
        this.in = in;
    }
}
