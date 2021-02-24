/*
 * @(#)Main.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package BESA.Extern;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import javax.jws.WebService;

/**
 * TODO. 
 * 
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.4
 */
@WebService(endpointInterface = "BESA.Extern.IBAPLocator")
public class BAPLocator implements IBAPLocator {

    private final String LOOK_UP_MSN = "LOOK_UP_MSN";
    private Hashtable BAPTable;
    private Tree BAPLocationTable;
    private Hashtable agCacheTable;
    private List<String> agCacheList;
    private final String BAP_DELIMITER = "%";
    private final int cacheSizeBAPLocator = 10;
    private final String BAP_LOCATOR_DELIMITER = "°";

    /**
     *
     */
    public BAPLocator() {
        if (BAPTable == null) {
            BAPTable = new Hashtable();
            BAPLocationTable = new Tree(-1, "", -1);
        }
        if (agCacheTable == null) {
            agCacheTable = new Hashtable();
            agCacheList = new ArrayList<String>();
        }
        System.out.println("[BAPLocator]:[INFO]: Start [OK].");
    }

    /**
     * 
     */
    public void restore() {
        BAPTable.clear();
        agCacheTable.clear();
        agCacheList.clear();
        BAPTable = new Hashtable();
        agCacheTable = new Hashtable();
        agCacheList = new ArrayList<String>();
        System.out.println("[BAPLocator]:[INFO]: The BAP Locator has restored.");
    }

    /**
     * 
     * @param admId
     * @param bapAdd
     * @param bapPort
     */
    public void registerBAP(String admId, String bapAdd, int bapPort) {
        if (BAPTable.get(admId) == null) {
            BAPTable.put(admId, bapAdd + BAP_DELIMITER + bapPort);
            System.out.println("[BAPLocator]:[INFO]: The BAP " + admId + ":" + bapAdd + ":" + bapPort + " has registered.");

            //----------------------------------------------------------------//
            String delimiter = new String(".");
            StringTokenizer stk = new StringTokenizer(bapAdd, delimiter);
            Tree search = BAPLocationTable;
            Tree level;
            int cont = 0;
            while (stk.hasMoreTokens()) {
                int node = new Integer(stk.nextToken());

                ArrayList<Tree> children = search.getChildren();
                if (children == null) {
                    children = new ArrayList<Tree>();
                    if (cont < 3) {
                        children.add(new Tree(node));
                    } else {
                        children.add(new Tree(node, bapAdd, bapPort));
                    }
                    search.setChildren(children);
                }
                int index = 0;
                do {
                    level = children.get(index);
                    if (level.getId() > node || level.getId() == node) {
                        if (level.getId() == node) {
                            search = level;
                        } else {
                            if (cont < 3) {
                                children.add(index, new Tree(node));
                            } else {
                                children.add(index, new Tree(node, bapAdd, bapPort));
                            }
                        }
                        index = children.size();
                    }
                    index++;
                } while (index < children.size() && level.getId() != node);
                cont++;
            }

            //----------------------------------------------------------------//

        } else {
            System.out.println("[BAPLocator]:[WARN]: The BAP " + admId + ":" + bapAdd + ":" + bapPort + " there is into BAPs register.");
        }
    }

    /**
     * 
     * @param agAlias
     * @param bapAdd
     * @param bapPort
     */
    public void registerAgent(String agAlias, String bapAdd, int bapPort) {
        if (agCacheTable.get(agAlias) == null) {
            if (agCacheTable.size() == cacheSizeBAPLocator) {
                agCacheTable.remove(agCacheList.remove(0));
            }
            agCacheTable.put(agAlias, bapAdd + BAP_DELIMITER + bapPort);
            agCacheList.add(agAlias);
            System.out.println("[BAPLocator]:[INFO]: The agent " + agAlias + " has registered.");
        } else {
            System.out.println("[BAPLocator]:[WARN]: The agent " + agAlias + " there isn't into cache register.");
        }
    }

    public void unRegisterAgent(String agAlias) {
        if (agCacheTable.get(agAlias) == null) {
            agCacheTable.remove(agCacheList.remove(0));
            agCacheList.remove(agAlias);
            System.out.println("[BAPLocator]:[INFO]: The agent " + agAlias + " has unregistered.");
        } else {
            System.out.println("[BAPLocator]:[WARN]: The agent " + agAlias + " there isn't into cache unregister.");
        }
    }

    /**
     * 
     * @param agAlias
     * @return
     */
    public String getAgentLocation(String agAlias) {
        if (agCacheTable.get(agAlias) != null) {
            agCacheList.remove(agAlias);
            agCacheList.add(agAlias);
        }
        System.out.println("[BAPLocator]:[INFO]: Did return the location information of the agent " + agAlias + ".");
        return (String) agCacheTable.get(agAlias);
    }

    public String getBAPsLocation(String bapAdd, int bapPort) {
        String BAPList = "";
        Enumeration elements = BAPTable.elements();
        while (elements.hasMoreElements()) {
            String bapLocation = (String) elements.nextElement();
            if (!bapLocation.equals(bapAdd + BAP_DELIMITER + bapPort)) {
                if (!BAPList.equals("")) {
                    BAPList = BAPList + BAP_LOCATOR_DELIMITER;
                }
                BAPList = BAPList + bapLocation;
            }
        }
        if (BAPList != null) {
            System.out.println("[BAPLocator]:[INFO]: Did return the location information of the next BAPs: " + BAPList.trim() + ".");
        } else {
            System.out.println("[BAPLocator]:[WARN]: Therea weren't BAPs into register.");
        }
        return BAPList;
    }

    @Override
    public String getMasterLocation(String mobileAdd) {
        System.out.println("[BAPLocator]:[INFO]: The  device " + mobileAdd + " send: " + LOOK_UP_MSN);
        String delimiter = new String(".");
        StringTokenizer stk = new StringTokenizer(mobileAdd, delimiter);
        Tree search = BAPLocationTable;
        String nearestBAPAdd = null;
        int nearestBAPPort = 0;
        boolean exit = false;
        while (stk.hasMoreTokens() && !exit) {
            int node = new Integer(stk.nextToken());
            ArrayList<Tree> children = search.getChildren();
            if (children == null) {
                return "ERROR";
            } else {
                int index = 0;
                Tree level;
                do {
                    level = children.get(index);
                    if (level.getId() > node || level.getId() == node) {
                        if (level.getId() == node) {
                            search = level;
                        } else {
                            Tree aux = level.getChildren().get(0);
                            aux = aux.getChildren().get(0);
                            level = aux.getChildren().get(0);
                            nearestBAPAdd = level.getAdd();
                            nearestBAPPort = level.getPort();
                            exit = true;
                        }
                        index = children.size();
                    }
                    index++;
                } while (index < children.size() && level.getId() != node);
            }
        }
        //--------------------------------------------------------------------//
        String receivedMsg = null;
        try {
            Socket socket = new Socket(nearestBAPAdd, nearestBAPPort);//Es Bapadd no getBaplocatoradd.
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            salida.writeUTF(LOOK_UP_MSN + BAP_DELIMITER);
            System.out.println("[BAPLocator]:[INFO]: Send " + LOOK_UP_MSN);
            receivedMsg = entrada.readUTF();
            System.out.println("[BAPLocator]:[INFO]: ACK Recieved of " + LOOK_UP_MSN + ": " + receivedMsg);
            salida.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (receivedMsg != null) {
            return receivedMsg;
        }
        return "ERROR";
    }
}
