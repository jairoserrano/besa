/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package BESA.Remote.Directory;

import BESA.Kernel.System.Directory.AdmHandlerBESA;
import BESA.Kernel.System.SystemExceptionBESA;
import BESA.Log.ReportBESA;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author fabianjose
 */
public class RemoteDirectoryBESA {

    /**
     * Table of administrators/AdmRemoteHandlers accessible for idAdm.
     */
    protected Hashtable<String, RemoteAdmHandlerBESA> admIdTable;
    /**
     * Table of administrators/AdmRemoteHandlers accessible for alias.
     */
    protected Hashtable<String, RemoteAdmHandlerBESA> admAliasTable;

    public RemoteDirectoryBESA() {
        this.admIdTable = new Hashtable<String, RemoteAdmHandlerBESA>();
        this.admAliasTable = new Hashtable<String, RemoteAdmHandlerBESA>();
    }

    public RemoteAdmHandlerBESA getAdmRemoteHandlerbyId(String admId) {
        return admIdTable.get(admId);
    }

    public void registerAdmRemoteHandler(String admId, RemoteAdmHandlerBESA admHand, String admAlias) {
        this.admIdTable.put(admId, admHand);                             //Puts AdmHandler on the adm Id table.
        this.admAliasTable.put(admAlias, admHand);                       //Puts AdmHandler on the adm Alias table.
    }

    public AdmHandlerBESA getAdmByAlias(String admAlias) {
        return admAliasTable.get(admAlias); //Compare with the table of remote adm.
    }

    public AdmHandlerBESA getAdmById(String admId) {
        return admIdTable.get(admId);
    }

    /**
     * Gets alias Adm by ID.
     *
     * @param admId  Administrator ID.
     * @return Adm alias.
     * @throws Exception NullPionterException because the adm does not find by admId in the table.
     */
    public String getAliasAdmById(String admId) {
        RemoteAdmHandlerBESA adm = (RemoteAdmHandlerBESA) admIdTable.get(admId);
        return adm.getAlias();
    }

    /**
     * Unregister remote Adm from serv Id Table.
     * @param admAlias Adm alias.
     */
    public void unregisterRemoteAdm(String admAlias) throws SystemExceptionBESA {
        try {
            //servIdTable.remove(admAlias);
            admIdTable.remove(this.getAdmByAlias(admAlias).getAdmId());
            admAliasTable.remove(admAlias);
        } catch (Exception ex) {
            ReportBESA.error("Couldn't unregister remote administrator " + admAlias + ": " + ex.toString());
            throw new SystemExceptionBESA("Couldn't unregister remote administrator " + admAlias + ": " + ex.toString());
        }
    }

    /**
     * 
     * @return 
     */
    public Enumeration<String> getAdmAliasList() {
        return admAliasTable.keys();
    }

    /**
     * 
     * @return 
     */
    public boolean thereAreNotAdm() {
        return admAliasTable.isEmpty();
    }
}
