/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Extern;

/**
 *
 * @author SAR
 */
public class Referred {

    private byte referred;
    private String bapDesAdd;
    private int bapDesPort;
    private String agAlias;

    public Referred(byte referred, String bapDesAdd, int bapDesPort, String agAlias) {
        this.referred = referred;
        this.bapDesAdd = bapDesAdd;
        this.bapDesPort = bapDesPort;
        this.agAlias = agAlias;
    }

    public String getAgAlias() {
        return agAlias;
    }

    public void setAgAlias(String agAlias) {
        this.agAlias = agAlias;
    }

    public String getBapDesAdd() {
        return bapDesAdd;
    }

    public void setBapDesAdd(String bapDesAdd) {
        this.bapDesAdd = bapDesAdd;
    }

    public int getBapDesPort() {
        return bapDesPort;
    }

    public void setBapDesPort(int bapDesPort) {
        this.bapDesPort = bapDesPort;
    }

    public byte getReferred() {
        return referred;
    }

    public void setReferred(byte referred) {
        this.referred = referred;
    }
}
