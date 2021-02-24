/*
 * @(#)DistributedInitBESA.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Remote;

import BESA.Remote.RemoteAdmBESA;
import BESA.Remote.AdmRemoteImpBESA;
import BESA.Remote.AdmRemoteInterfaceBESA;
import BESA.Remote.DistributedExceptionBESA;
import BESA.ExceptionBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.System.Directory.AdmHandlerBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Log.ReportBESA;
import BESA.Remote.Directory.RemoteAdmHandlerBESA;
import java.net.MalformedURLException;

/**
 * This class provides methods that allow initialize the associated parameters
 * to handling of remote BESA containers.
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class DistributedInitBESA {

    /**
     * The constructor of this class initializes the associated remote access
     * parameters to a BESA container.
     *
     * @param admBesa The BESA administrator to be initialized.
     * @param admHandler The BESA administrator handler.
     * @param portRmiRegistry The container RPC associated port.
     * @param multicastAddr The container multicast associated address.
     * @param multicastPort The container multicast associated port.
     */
    public DistributedInitBESA(RemoteAdmBESA admBesa, RemoteAdmHandlerBESA admHandler, int portRmiRegistry, String multicastAddr, int multicastPort) throws DistributedExceptionBESA {
        boolean isMulticastAddress = false;
        try {
            isMulticastAddress = InetAddress.getByName(multicastAddr).isMulticastAddress();
        } catch (Exception e) {
            ReportBESA.error("Couldn't check the multicast address");
            throw new DistributedExceptionBESA("Couldn't check the multicast address: " + e.toString());
        }
        if (isMulticastAddress) {
            initWithMulticast(admBesa, admHandler, portRmiRegistry, multicastAddr, multicastPort);
        } else {
            intWithSocketsMulticast(admBesa, admHandler, portRmiRegistry, multicastAddr, multicastPort);
        }
    }

    /**
     * Initializes the container remote parameters if the multicast provided
     * address is valid.
     *
     * @param admBesa The BESA administrator to be initialized.
     * @param admHandler The BESA administrator handler.
     * @param portRmiRegistry The container RPC associated port.
     * @param multicastAddr The container multicast associated address.
     * @param multicastPort The container multicast associated port.
     */
    private static void initWithMulticast(RemoteAdmBESA admBesa, RemoteAdmHandlerBESA admHandler, int portRmiRegistry, String multicastaddr, int multicastport) throws DistributedExceptionBESA {
        // Init de multicast - puerto, dir_inet y socket
        admBesa.setMulticastPort(multicastport);
        try {
            admBesa.setMulticastInetAddr(InetAddress.getByName(multicastaddr));
            admBesa.setMulticastSocket(new MulticastSocket(multicastport));
            admBesa.getMulticastSocket().joinGroup(admBesa.getMulticastInetAddr());
        } catch (UnknownHostException e) {
            ReportBESA.error("Couldn't get the multicast address");
            throw new DistributedExceptionBESA("Couldn't get the multicast address: " + e.toString());
        } catch (IOException e) {
            ReportBESA.error("Couldn't start the socket multicast");
            throw new DistributedExceptionBESA("Couldn't start the socket multicast: " + e.toString());
        }
        ReportBESA.trace("BESA ha inicializado socket multicast: "
                + admBesa.getMulticastInetAddr() + ":"
                + admBesa.getMulticastPort());

        // Init de la variable estatica admLocal compartida por todos los agentes/administradores
        AgentBESA.initAdmLocal(admBesa);
        AdmRemoteImpBESA.initAdmLocal(admBesa);

        // Registro RMI para servicios de administracion remota
        startRMIService(portRmiRegistry, admBesa);

        String rmiName = admHandler.generateRmiUrl();

        AdmRemoteInterfaceBESA server = null;
        try {
            server = new AdmRemoteImpBESA();
            Naming.rebind(rmiName, server);
        } catch (RemoteException ex) {
            ReportBESA.error("Couldn't create the remote interface");
            throw new DistributedExceptionBESA("Couldn't create the remote interface: " + ex.toString());
        } catch (MalformedURLException ex) {
            ReportBESA.error("Couldn't register with RMI registry");
            throw new DistributedExceptionBESA("Couldn't register with RMI registry: " + ex.toString());
        }

        ReportBESA.trace("BESA ha registrado a: " + rmiName);

        // Lanzar hilo para recibir/escuchar multicast

        admBesa.setPong(new PongThread(admBesa));
        admBesa.getPong().start();
        // Lanzar hilo para enviar multicast
        admBesa.setPing(new PingThread(admBesa));
        admBesa.getPing().start();
    }

    /**
     * Initializes the container remote parameters if the multicast provided
     * address isn't valid.
     *
     * @param admBesa The BESA administrator to be initialized.
     * @param admHandler The BESA administrator handler.
     * @param portRmiRegistry The container RPC associated port.
     * @param multicastAddr The container multicast associated address.
     * @param multicastPort The container multicast associated port.
     */
    private static void intWithSocketsMulticast(RemoteAdmBESA admBesa, RemoteAdmHandlerBESA admHandler, int portRmiRegistry, String multicastaddr, int multicastport) throws DistributedExceptionBESA {
        // Init de multicast - puerto, dir_inet y socket
        admBesa.setMulticastPort(multicastport);
        try {
            admBesa.setMulticastInetAddr(InetAddress.getByName(multicastaddr));
        } catch (UnknownHostException ex) {
            ReportBESA.error("[DistributedInitBESA::intWithSocketsMulticast] Couldn't get the multicast address");
            throw new DistributedExceptionBESA("Couldn't get the multicast address: " + ex.toString());
        }

        //1. Identificar si el ServerSocket esta corriendo en el puerto
        if (admBesa.getMulticastInetAddr().isLoopbackAddress()) {
            //La direcci�n es local por lo que debo iniciar el servidor
            //Si ya existe, solo sale un mensaje de depuracion
            SocketServer ss = new SocketServer(admBesa,
                    admBesa.getMulticastPort());
            //Inicia el puerto
            if (ss.startServerSocket()) {
                //Inicia recepcion
                ss.start();
            }
        }

        //2. Conectarse al serverSocket
        Socket socket;
        try {
            socket = new Socket(admBesa.getMulticastInetAddr(),
                    admBesa.getMulticastPort());
            admBesa.setSocketPingPong(socket);
        } catch (UnknownHostException e) {
            ReportBESA.error("Don't know about host: " + multicastaddr);
            throw new DistributedExceptionBESA("Don't know about host: " + multicastaddr + ": " + e.toString());

        } catch (IOException e) {
            ReportBESA.error("Couldn't get I/O for " + "the connection to: " + multicastaddr);
            throw new DistributedExceptionBESA("Couldn't get I/O for " + "the connection to: " + multicastaddr + ": " + e.toString());
        }

        /*
        admBesa.setMulticastSocket(new MulticastSocket(multicastport));
        admBesa.getMulticastSocket().joinGroup(admBesa.getMulticastInetAddr());
         */
        ReportBESA.trace("BESA ha inicializado socket multicast: "
                + admBesa.getMulticastInetAddr() + ":"
                + admBesa.getMulticastPort());

        // Init de la variable estatica admLocal compartida por todos los agentes/administradores
        AgentBESA.initAdmLocal(admBesa);
        AdmRemoteImpBESA.initAdmLocal(admBesa);

        // Registro RMI para servicios de administracion remota
        //Si ya existe simplemente lo agarra
        startRMIService(portRmiRegistry, admBesa);
        String rmiName = null;
        try {
            rmiName = admHandler.generateRmiUrl();
            AdmRemoteInterfaceBESA server = new AdmRemoteImpBESA();
            Naming.rebind(rmiName, server);
        } catch (RemoteException ex) {
            ReportBESA.error("Couldn't create the remote interface");
            throw new DistributedExceptionBESA("Couldn't create the remote interface: " + ex.toString());
        } catch (MalformedURLException ex) {
            ReportBESA.error("Couldn't register with RMI registry");
            throw new DistributedExceptionBESA("Couldn't register with RMI registry: " + ex.toString());
        }
        ReportBESA.trace("BESA ha registrado a: " + rmiName);

        // Lanzar hilo para recibir/escuchar multicast
        admBesa.setPong(new PongSocketThread(admBesa));
        admBesa.getPong().start();
        try {
            // Lanzar hilo para enviar multicast
            admBesa.setPing(new PingSocketThread(admBesa));
        } catch (SystemExceptionBESA ex) {
            ex.printStackTrace();//TODO
        }
        admBesa.getPing().start();

    }

    /**
     * Creates the RPC service.
     *
     * @param portRmiRegistry The container RPC associated port.
     * @param admBesa The BESA container.
     */
    private static void startRMIService(int portRmiRegistry, RemoteAdmBESA admBesa) throws DistributedExceptionBESA {
        @SuppressWarnings("unused")
        Registry rmiRegistry;
        rmiRegistry = null;
        try {
            try {
                rmiRegistry = LocateRegistry.createRegistry(portRmiRegistry);
                Thread.sleep(admBesa.getConfigBESA().getRMITimeout());
            } catch (RemoteException e1) {
                rmiRegistry = LocateRegistry.getRegistry(portRmiRegistry);
                Thread.sleep(admBesa.getConfigBESA().getRMITimeout());
            }
            ((RemoteAdmHandlerBESA)admBesa.getAdmHandler()).setRmiRegistry(rmiRegistry);
        } catch (Exception e) {
            ReportBESA.error("Could not Create RMI service or it is already running");
            throw new DistributedExceptionBESA("Could not Create RMI service or it is already running: " + e.toString());
        }
    }
}
