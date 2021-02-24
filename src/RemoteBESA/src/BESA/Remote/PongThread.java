/*//TODO Copia.
 * @(#)PongThread.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Remote;

import BESA.ExceptionBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Kernel.System.Directory.SystemDirectoryExceptionBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Log.ReportBESA;
import BESA.Remote.Directory.DistributedDirectoryExceptionBESA;
import BESA.Remote.Directory.RemoteAdmHandlerBESA;
import java.io.IOException;
import java.net.DatagramPacket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents
 * PongThread manejo de la recepcion y tratamiento de los mensajes de multicast
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class PongThread extends Thread {

    /**
     *
     */
    protected RemoteAdmBESA admLocal;
    /**
     * 
     */
    protected final int MULTICAST_ADM_TOKEN_QUANTITY = 6;
    /**
     * 
     */
    protected final int MULTICAST_AGENT_TOKEN_QUANTITY = 5;
    /**
     * 
     */
    protected final int MULTICAST_AGENT_SET_STATE = 3;
    /**
     * 
     */
    protected final int MULTICAST_MOVE_AGENT_TOKEN_QUANTITY = 6;

    /**
     * MulticastClientThread constructor que inicializa direccion y puerto de multicast
     * @param admLocal administrador local al cual pertenece el cliente
     */
    public PongThread(RemoteAdmBESA admLocal) {
        super("PongThread");
        this.admLocal = admLocal;
    }

    /**
     * run responsable de escuchar los mensajes de broadcast.
     * NOTA:
     *    Deber�a invocar a un m�todo de ASMA (asma) que tome alguna acci�n.
     */
    public void run() {
        try {
            // ciclo infinito de recepcion
            while (admLocal.isAlive()) {
                // recibir mensaje
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                ReportBESA.trace("Antes de Recibir Multicast");

                try {

                    if (admLocal.getMulticastSocket() != null) {
                        admLocal.getMulticastSocket().receive(packet);
                    } else {

                        System.out.println("Ajjaa" + new String(packet.getData()).trim());
                        return;
                    }

                } catch (Exception ee) {
                    System.err.println("-> " + new String(packet.getData()).trim());
                }

                // procesar mensaje - destokenizar y pasar a un vector
                String receivedMsg = new String(packet.getData()).trim();
                ReportBESA.trace("MULTICAST Recibido:" + receivedMsg);
                String delimiter = new String(RemoteAdmBESA.MULTICAST_DELIMITER);
                StringTokenizer stk = new StringTokenizer(receivedMsg, delimiter);
                ArrayList tokens = new ArrayList();
                while (stk.hasMoreTokens()) {
                    String token = new String(stk.nextToken());
                    ReportBESA.trace("Tokenizado:" + token);
                    tokens.add(token);
                }
                //PENDIENTE VALIDAR MSG_STAMP
                String msgType = new String((String) tokens.get(0)/*tokens.firstElement()*/);
                
                
                
                
                        
                /*String admId = admAlias + AdmHandlerBESA.ID_DELIMITER
                + ipRmiRegistry + AdmHandlerBESA.ID_DELIMITER
                + Integer.toString(portRmiRegistry);*/
                
                // caso ADM ALIVE
                if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_ADM_ALIVE)) {
                    
                    String admAlias = (String) tokens.get(1); 
                String ipRmiRegistry = (String) tokens.get(2);
                int portRmiRegistry = Integer.parseInt((String) tokens.get(3));
                String admId = (String) tokens.get(5);
                
                    // validar numero de tokens en el mensaje
                    if (tokens.size() == MULTICAST_ADM_TOKEN_QUANTITY) {
                        // analizar y responder/actuar segun el tipo de mensaje
                        // 0.msgType 1.admAlias 2.ipAddress 3.rmiPort 4.msgStamp
                        //PENDIENTE VALIDAR DOBLE REGISTRO DE UN MISMO CONTENEDOR REMOTO YA EXISTENTE
                        // Si se cayo y volvio a subir -> resincronizar como si primer arranque - purgar dirs
                        // registro en los dos adms, admRemoto y admLocal
                        // registro del admRemoto en el admLocal
                        
                        
                        //if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase((String) tokens.get(3))) {
                        if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase(admId)) {
                            
                            
                            
                            try {
                                 RemoteAdmHandlerBESA remoteAdmHandler =
                                        admLocal.registerRemoteAdm( admId,
                                        admAlias, //1.admAlias
                                        ipRmiRegistry, //2.ipRmiAddress
                                        portRmiRegistry //3.rmiPort
                                        );

                                ReportBESA.trace("ALIVE: AdmRemoteHandler = " + remoteAdmHandler);
                                if (remoteAdmHandler != null) {
                                    // si OK -> registro del admLocal en el admRemoto - via rmi
                                    RemoteAdmHandlerBESA admLocalHandler = (RemoteAdmHandlerBESA) this.admLocal.getAdmHandler();
                                    remoteAdmHandler.getAdmRemote().registerRemoteAdm(admLocalHandler.getAdmId(),
                                            admLocalHandler.getAlias(),
                                            admLocalHandler.getIpRmiRegistry(),
                                            admLocalHandler.getPortRmiRegistry());
                                    ReportBESA.trace("ALIVE: AdmLocalHandler = " + admLocalHandler);
                                    ArrayList idList = new ArrayList();
                                    ArrayList aliasList = new ArrayList();
                                    this.admLocal.geLocalDirectoryList(idList, aliasList);
                                    remoteAdmHandler.getAdmRemote().synchronizeRemoteAgentDirectory((this.admLocal.getAdmHandler()).getAdmId(), idList, aliasList);
                                }//fin caso no es un ECO
                            }//try
                            catch (RemoteException e) {
                                ReportBESA.warn("Registro Lookup:" + e.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                                ReportBESA.error("Registro Rebind: 333 " + e.getMessage());
                            }
                        }
                    }//fin if token.size
                }//fin caso ADM ALIVE
                // caso ADM KILL
                else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_ADM_KILL)) {
                    // validar numero de tokens en el mensaje
                    ReportBESA.trace("KILL");
                    if (tokens.size() == MULTICAST_ADM_TOKEN_QUANTITY) {
                        try {
                            // eliminar registro del admRemoto en el admLocal
                            admLocal.unregisterRemoteAdm((String) tokens.get(1)); //1.admAlias
                        } catch (SystemExceptionBESA ex) {
                            ex.printStackTrace();//TODO LOG.
                        }



                        // purgar directorios de agentes del contenedor "muerto"
                        //PENDIENTE
                        //admLocal.getMulticastSocket().leaveGroup(admLocal.getMulticastInetAddr());
                        //admLocal.getMulticastSocket().close();
                        //----------------------------------------------------//
                        // Checks if the ADM is the current ADM for exit of   //
                        // the system.                                        //
                        //----------------------------------------------------//
                        if (admLocal.getAdmHandler().getAlias().equalsIgnoreCase((String) tokens.get(1))) {
                            if (!admLocal.getMulticastSocket().isClosed()) {
                                admLocal.getMulticastSocket().leaveGroup(admLocal.getMulticastInetAddr());
                                admLocal.getMulticastSocket().close();
                                admLocal.setMulticastSocket(null);
                                admLocal.setMulticastInetAddr(null);
                                admLocal.setAdmHandler(null);
                            }
                        }
                    }
                }//fin caso ADM KILL
                //caso AGENT CREATE
                else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_AGENT_CREATE)) {
                    // validar numero de tokens en el mensaje
                    ReportBESA.trace("AGENT_CREATE-antes del if");
                    if (tokens.size() == MULTICAST_AGENT_TOKEN_QUANTITY) { //es normal que esta cantidad cambia
                        // Adicionar agente remoto al directorio local
                        ReportBESA.trace("AGENT_CREATE_REMOTE");
                        //PENDIENTE
                        //Se verifica que no sea un eco de un mensaje de creaci�n de un gente
                        //if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase((String) tokens.get(3))) {
                        if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase((String) tokens.get(3))) {    
                            try {
                                //0. MsgType, 1.adAlias, 2. adId, 3. admId y 4.msgStamp
                                this.admLocal.registerRemoteAgent((String) tokens.get(1), (String) tokens.get(2), (String) tokens.get(3)); // 3.admId
                                //Se hace la reflection del agente
                            } catch (DistributedDirectoryExceptionBESA ex) {
                                Logger.getLogger(PongThread.class.getName()).log(Level.SEVERE, null, ex);//TODO
                            }
                        }
                    }
                }//fin caso AGENT CREATE
                //caso AGENT DESTROY
                else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_AGENT_DESTROY)) {
                    // validar numero de tokens en el mensaje
                    ReportBESA.trace("DESTROY-antes del if");
                    if (tokens.size() == MULTICAST_AGENT_TOKEN_QUANTITY) {
                        //Se verifica que no sea un eco de un mensaje de creaci�n de un gente
                        //if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase((String) tokens.get(3))) {
                            if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase((String) tokens.get(3))) {
                            // eliminar agente remoto al directorio local
                            ReportBESA.trace("DESTROY-entre el if");
                            try {
                                // 2. agId
                                this.admLocal.unregisterAgent((String) tokens.get(2));     // 2. agId
                            } catch (SystemExceptionBESA ex) {
                                Logger.getLogger(PongThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }//fin caso AGENT DESTROY
                //caso AGENT MOVE
                else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_AGENT_MOVE)) {
                    // validar numero de tokens en el mensaje
                    ReportBESA.trace("MOVE-Begin");
                    if (tokens.size() == this.MULTICAST_MOVE_AGENT_TOKEN_QUANTITY) {
                        //Se verifica que no sea un eco o un mensaje en el contenedor donde se va a mover el Agente
                        if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase((String) tokens.get(3))//3
                                && !(this.admLocal.getAdmHandler().getAlias()).equalsIgnoreCase((String) tokens.get(4))) {
                            try {
                                // eliminar agente remoto al directorio local
                                // 2. agId y 4. admRemoteHandler
                                this.admLocal.updateUnregisterRemoteAgent((String) tokens.get(1), (String) tokens.get(4)); // 2. agId y 4. admRemoteHandler
                            } catch (SystemExceptionBESA ex) {
                                ex.printStackTrace();//TODO LOG.
                            }


                            ReportBESA.trace("MOVE-End");
                        }
                    }
                }//fin caso AGENT MOVE
                //caso SET STATE AGENT (MOVE)
                else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_MOVE)
                        || msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_ALIVE)) {
                    // validar numero de tokens en el mensaje
                    ReportBESA.trace("SET-STATE-MOVE-Begin");
                    if (tokens.size() == this.MULTICAST_AGENT_SET_STATE) {
                        // Actualiza el estado del agente para informar que se esta moviendo y no se puedne enviar eventos
                        //0. MsgType, 1.agId,y 2.msgStamp
                        if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_MOVE)) {
                            try {
                                this.admLocal.updateStateAgent((String) tokens.get(0), (String) tokens.get(1));     // 0. msgType y 1. agId
                            } catch (SystemExceptionBESA ex) {
                                Logger.getLogger(PongThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_ALIVE)) {
                            //le cambia el estado y lo despierta
                            AgHandlerBESA agh = null;
                            try {
                                agh = this.admLocal.getHandlerByAid((String) tokens.get(1));
                            } catch (ExceptionBESA ex) {
                                Logger.getLogger(PongThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                //agh.notify();
                                agh.notificar();
                            } catch (SystemDirectoryExceptionBESA ex) {
                                ex.printStackTrace();//TODO LOG.
                            }
                        }
                        ReportBESA.trace("SET-STATE-MOVE-End");
                    }
                }//fin caso SET STATE AGENT (MOVE)
            }//fin while alive
        }//try principal
        catch (IOException ioe) {
            ReportBESA.error(ioe.toString());
        }
    }//fin run
}
