/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkTools;

/**
 *
 * @author jairo
 */
public final class BenchmarkData {

    private int contenedor;
    private String alias;
    private boolean ready;
    private int agentid;

    public BenchmarkData(String alias, int contenedor, int agentid) {
        this.setContenedor(contenedor);
        this.setAgentid(agentid);
        this.setReady(false);
        this.setAlias(alias);
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

    public int getContenedor() {
        return contenedor;
    }

    public void setContenedor(int contenedor) {
        this.contenedor = contenedor;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

}
