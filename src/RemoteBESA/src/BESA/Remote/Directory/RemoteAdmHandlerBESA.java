/*
 * @(#)Main.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Remote.Directory;

import BESA.Config.ConfigBESA;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import BESA.Remote.AdmRemoteInterfaceBESA;
import BESA.Kernel.System.Directory.AdmHandlerBESA;
import BESA.Log.ReportBESA;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides methods to use the remote administrators handlers.
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class RemoteAdmHandlerBESA extends AdmHandlerBESA implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = "AdmRemoteHandler".hashCode();
    /**
     *
     */
    private AdmRemoteInterfaceBESA admRemote;
    /**
     *
     */
    private String ipRmiRegistry;
    /**
     *
     */
    private int portRmiRegistry;
    /**
     *
     */
    private String rmiName;
    /**
     * 
     */
    public static final String ID_DELIMITER = "_";
    /**
     *
     */
    private Registry rmiRegistry;

    public RemoteAdmHandlerBESA(ConfigBESA configBESA) {
        this.alias = configBESA.getAliasContainer();
        
        //this.ipRmiRegistry = configBESA.getIpaddress();
        this.ipRmiRegistry = "127.0.0.1";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            this.ipRmiRegistry = addr.getHostAddress();
        } catch (Exception e) {
            ReportBESA.error(e);
        }
        
        this.portRmiRegistry = configBESA.getRmiPort();
        
        
        /*
        // formato del admId -> 1.alias 2.ip 3. port
        this.admId = alias + ID_DELIMITER
                + ipRmiRegistry + ID_DELIMITER
                + Integer.toString(portRmiRegistry);
        // generar nombre para bind/lookup de RMI
        */
        //this.admId = UUID.randomUUID().toString();
        
        this.admId = alias + ID_DELIMITER
                + ipRmiRegistry + ID_DELIMITER
                + Integer.toString(portRmiRegistry) + ID_DELIMITER
        
        +UUID.randomUUID().toString();
        
        
        this.rmiName = this.generateRmiName();
    }

    
 
    
    /**
     * Builds a new administrator remote handler.
     *
     * @param aliasAdm Alias/name of the remote admnistrador.
     * @param ipRmiAddress The IP address of the machine that contains the
     * BESA administrator.
     * @param rmiPort The port of the machine that contains the administrator.
     * @throws Exception If the reference for the remote object associated with
     * the specified name is not found.
     */
    public RemoteAdmHandlerBESA(String admId, String aliasAdm, String ipRmiRegistry, int portRmiRegistry) throws DistributedDirectoryExceptionBESA {
        // hacer lookup - obtener referencia para servicios por RMI del administrador remoto
        this.alias = aliasAdm;
        
        this.ipRmiRegistry = ipRmiRegistry;
        this.portRmiRegistry = portRmiRegistry;
        
        /*this.admId = alias + ID_DELIMITER
                + ipRmiRegistry + ID_DELIMITER
                + Integer.toString(portRmiRegistry);*/
        
        this.admId = admId;
        
        this.rmiName = this.generateRmiName();
        
        String rmiUrl = generateRmiUrl();
        try {
            //        this.admRemote = null;
            this.admRemote = (AdmRemoteInterfaceBESA) Naming.lookup(rmiUrl);
        } catch (Exception ex) {
            ReportBESA.error("Couldn't get the reference of remote services of the remote container administrator.");
            throw new DistributedDirectoryExceptionBESA("Couldn't get the reference of remote services of the remote container administrator: " + ex.toString());
        }
    }

    
    /**
     *
     * @return
     */
    private String generateRmiName() {
        return "RMI" + ID_DELIMITER + this.admId;
    }
    
    /**
     * Obtains the instance of the remote administrator interface associated.
     *
     * @return The BESA administrator.
     * @throws Exception If the administrator handler has not been initialized.
     */
    public AdmRemoteInterfaceBESA getAdmRemote() throws DistributedDirectoryExceptionBESA {
        if (this.admRemote == null) {
            ReportBESA.error("Uninitialized handler.");
            throw new DistributedDirectoryExceptionBESA("Uninitialized handler.");
        }
        return admRemote;
    }
    
    /**
     *
     * @return
     */
    public String generateRmiUrl() {
        return "//" + ipRmiRegistry + ":" + portRmiRegistry + "/" + rmiName;
    }
    
    /**
     *
     * @return
     */
    public String getIpRmiRegistry() {
        return ipRmiRegistry;
    }

    /**
     *
     * @return
     */
    public int getPortRmiRegistry() {
        return portRmiRegistry;
    }

    /**
     *
     * @return
     */
    public String getRmiName() {
        return rmiName;
    }

    /**
     *
     * @param rmiRegistry
     */
    public void setRmiRegistry(Registry rmiRegistry) {
        this.rmiRegistry = rmiRegistry;
    }

    /**
     * 
     * @return
     */
    public Registry getRmiRegistry() {
        return rmiRegistry;
    }
}
