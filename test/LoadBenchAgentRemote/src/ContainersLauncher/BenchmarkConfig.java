/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

import BESA.Log.ReportBESA;

/**
 *
 * @author Jairo Serrano <jaserrano@javeriana.edu.co>
 *
 */
public final class BenchmarkConfig {

    private static BenchmarkConfig instance = null;

    private final int NumberOfAgents;
    private final int NumberOfContainers;
    private final int SmallLoads;
    private final int MediumLoads;
    private final int HighLoads;
    private final boolean OrderOn;
    private final boolean CooperationOn;
    private final boolean BalancerOn;
    private final boolean BackupOn;
    private final int BackupTime;

    public static BenchmarkConfig getConfig() {
        return BenchmarkConfig.instance;
    }

    public static BenchmarkConfig getConfig(String args[]) {
        BenchmarkConfig.instance = new BenchmarkConfig(args);
        return BenchmarkConfig.instance;
    }

    /**
     * Nodos,Agentes X Contenedor,Carga A,Carga B,Carga
     * C,Orden,Cooperación,Balanceo,Backup
     *
     * @param args
     */
    private BenchmarkConfig(String args[]) {

        String[] config = args[0].split(",");

        this.NumberOfContainers = Integer.parseInt(config[0]);
        this.NumberOfAgents = Integer.parseInt(config[1]);
        this.SmallLoads = Integer.parseInt(config[2]);
        this.MediumLoads = Integer.parseInt(config[3]);
        this.HighLoads = Integer.parseInt(config[4]);
        this.OrderOn = "1".equals(config[5]);
        this.CooperationOn = "1".equals(config[6]);
        this.BalancerOn = "1".equals(config[7]);
        if (Integer.parseInt(config[8])>0){
            this.BackupOn = true;
            this.BackupTime = Integer.parseInt(config[8]);
        }else{
            this.BackupOn = false;
            this.BackupTime = 0;
        }
    }

    public int getBackupTime() {
        return BackupTime;
    }
    
    public int getNumberOfTasks() {
        return SmallLoads+MediumLoads+HighLoads;
    }
    
    public int getNumberOfAgents() {
        return NumberOfAgents;
    }

    public int getSmallLoads() {
        return SmallLoads;
    }

    public int getMediumLoads() {
        return MediumLoads;
    }

    public int getHighLoads() {
        return HighLoads;
    }

    public boolean InOrder() {
        return OrderOn;
    }

    public boolean IsCooperationOn() {
        return CooperationOn;
    }

    public boolean IsBalancerOn() {
        return BalancerOn;
    }

    public boolean IsBackupOn() {
        return BackupOn;
    }

    public int getNumberOfContainers() {
        return NumberOfContainers;
    }
    
    public int getAgentsByContainer() {
        return NumberOfAgents/NumberOfContainers;
    }

    @Override
    public String toString() {
        return "BenchmarkConfig{"
                + "NumberOfAgents=" + NumberOfAgents
                + ", NumberOfContainers=" + NumberOfContainers
                + ", SmallLoads=" + SmallLoads
                + ", MediumLoads=" + MediumLoads
                + ", HighLoads=" + HighLoads
                + ", OrderOn=" + OrderOn
                + ", CooperationOn=" + CooperationOn
                + ", BalancerOn=" + BalancerOn
                + ", BackupOn=" + BackupOn
                + '}';
    }

}
