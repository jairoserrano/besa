/*//TODO Copia.
 * @(#)PongSocketThread.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Remote;

import BESA.ExceptionBESA;
import BESA.Remote.RemoteAdmBESA;
import BESA.Remote.Directory.DistributedDirectoryExceptionBESA;
import BESA.Kernel.System.Directory.SystemDirectoryExceptionBESA;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import BESA.Remote.Directory.RemoteAdmHandlerBESA;
import BESA.Kernel.System.Directory.AdmHandlerBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Log.ReportBESA;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents 
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class PongSocketThread extends PongThread {

    /**
     *
     */
    BufferedReader in = null;

    /** 
     * Creates a new instance of PongSocketThread
     */
    public PongSocketThread(RemoteAdmBESA admLocal) {
        super(admLocal);
        try {
            in = new BufferedReader(new InputStreamReader(
                    admLocal.getSocketPingPong().getInputStream()));
        } catch (IOException e) {
            ReportBESA.error(e.toString());
        }
    }

    /**
     * 
     */
    public void run() {
        try {
            // ciclo infinito de recepcion
            while (admLocal.isAlive()) {
System.out.println("PongSocketThread");
                // recibir mensaje
                /*
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                RobocoopDirectoryNameGenerator.debug(2,"[PongThread::run] " + "Antes de Recibir Multicast");
                admLocal.getMulticastSocket().receive(packet);
                 */
                // procesar mensaje - destokenizar y pasar a un vector
                //String receivedMsg = new String(packet.getData()).trim();

                String receivedMsg = in.readLine();

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

                // caso ADM ALIVE
                if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_ADM_ALIVE)) {
                    // validar numero de tokens en el mensaje
                    if (tokens.size() == MULTICAST_ADM_TOKEN_QUANTITY) {
                        // analizar y responder/actuar segun el tipo de mensaje
                        // 0.msgType 1.admAlias 2.ipAddress 3.rmiPort 4.msgStamp
                        //PENDIENTE VALIDAR DOBLE REGISTRO DE UN MISMO CONTENEDOR REMOTO YA EXISTENTE
                        // Si se cayo y volvio a subir -> resincronizar como si primer arranque - purgar dirs
                        // registro en los dos adms, admRemoto y admLocal
                        // registro del admRemoto en el admLocal
                        try {
                            
                            String admAlias = (String) tokens.get(1); 
                String ipRmiRegistry = (String) tokens.get(2);
                int portRmiRegistry = Integer.parseInt((String) tokens.get(3));
                
                        
                String admId = admAlias + RemoteAdmHandlerBESA.ID_DELIMITER
                + ipRmiRegistry + RemoteAdmHandlerBESA.ID_DELIMITER
                + Integer.toString(portRmiRegistry);
                
                            RemoteAdmHandlerBESA remoteAdmHandler =
                                    admLocal.registerRemoteAdm( admId,
                                    (String) tokens.get(1), //1.admAlias
                                    (String) tokens.get(2), //2.ipRmiAddress
                                    Integer.parseInt((String) tokens.get(3)) //3.rmiPort
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
                            ReportBESA.error("Registro Lookup:" + e.getMessage());
                        } catch (Exception e) {
                            ReportBESA.error("Registro Rebind:" + e.getMessage());
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
                            // purgar directorios de agentes del contenedor "muerto"
                            //PENDIENTE
                            //admLocal.getMulticastSocket().close();
                            //admLocal.getMulticastSocket().close();
                        } catch (SystemExceptionBESA ex) {
                            ReportBESA.error(ex.toString());
                            ex.printStackTrace();
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
                        if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase((String) tokens.get(3))) {
                            //0. MsgType, 1.adAlias, 2. adId, 3. admId y 4.msgStamp

                            ReportBESA.trace("33333333333333");
                            try {
                                this.admLocal.registerRemoteAgent((String) tokens.get(1), (String) tokens.get(2), (String) tokens.get(3)); // 3.admId
                                //Se hace la reflection del agente
                            } catch (DistributedDirectoryExceptionBESA ex) {
                                Logger.getLogger(PongSocketThread.class.getName()).log(Level.SEVERE, null, ex);//TODO
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
                        if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase((String) tokens.get(3))) {
                            // eliminar agente remoto al directorio local
                            ReportBESA.trace("DESTROY-entre el if");
                            try {
                                // 2. agId
                                this.admLocal.unregisterAgent((String) tokens.get(2));     // 2. agId
                            } catch (SystemExceptionBESA ex) {
                                Logger.getLogger(PongSocketThread.class.getName()).log(Level.SEVERE, null, ex);
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
                        if (!(this.admLocal.getAdmHandler().getAdmId()).equalsIgnoreCase((String) tokens.get(3)) &&
                                !(this.admLocal.getAdmHandler().getAlias()).equalsIgnoreCase((String) tokens.get(4))) {
                            try {
                                // eliminar agente remoto al directorio local
                                //0. MsgType, 1.agAlias, 2. agId, 3. admId, 4 AliasAdmRemoteHandler y 5.msgStamp
                                this.admLocal.updateUnregisterRemoteAgent((String) tokens.get(1), (String) tokens.get(4)); // 2. agId y 4. admRemoteHandler
                            } catch (SystemExceptionBESA ex) {
                                Logger.getLogger(PongSocketThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            


                            ReportBESA.trace("MOVE-End");
                        }
                    }
                }//fin caso AGENT MOVE
                //caso SET STATE AGENT (MOVE)
                else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_MOVE) ||
                        msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_ALIVE)) {
                    // validar numero de tokens en el mensaje
                    ReportBESA.trace("SET-STATE-MOVE-Begin");
                    if (tokens.size() == this.MULTICAST_AGENT_SET_STATE) {
                        // Actualiza el estado del agente para informar que se esta moviendo y no se puedne enviar eventos
                        //0. MsgType, 1.agId,y 2.msgStamp
                        if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_MOVE)) {
                            try {
                                this.admLocal.updateStateAgent((String) tokens.get(0), (String) tokens.get(1));     // 0. msgType y 1. agId
                            } catch (SystemExceptionBESA ex) {
                                Logger.getLogger(PongSocketThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (msgType.equalsIgnoreCase(RemoteAdmBESA.MSG_MULTICAST_SET_STATE_AGENT_ALIVE)) {
                            //le cambia el estado y lo despierta
                            AgHandlerBESA agh = null;
                            try {
                                agh = this.admLocal.getHandlerByAid((String) tokens.get(1));
                            } catch (ExceptionBESA ex) {
                                Logger.getLogger(PongSocketThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
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
