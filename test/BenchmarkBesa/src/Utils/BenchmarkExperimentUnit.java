/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author jairo
 */
public class BenchmarkExperimentUnit {

    private final String ExperimentRaw;
    private final int ExperimentID;
    private int NumberOfAgents;
    private final int NumberOfContainers;
    private final int SmallLoads;
    private final int MediumLoads;
    private final int HighLoads;
    private final boolean CooperationOn;
    private final boolean BalancerOn;
    private final boolean BackupOn;
    private final int BackupTime;

    public String getExperimentRaw() {
        return ExperimentRaw;
    }    
    
    public int getExperimentID() {
        return ExperimentID;
    }

    public int getNumberOfAgents() {
        return NumberOfAgents;
    }

    public int getNumberOfContainers() {
        return NumberOfContainers;
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

    public boolean isCooperationOn() {
        return CooperationOn;
    }

    public boolean isBalancerOn() {
        return BalancerOn;
    }

    public boolean isBackupOn() {
        return BackupOn;
    }

    public int getBackupTime() {
        return BackupTime;
    }

    public int getNumberOfTotalTasks() {
        return SmallLoads + MediumLoads + HighLoads;
    }

    public int getAgentsByContainer() {
        return NumberOfAgents / NumberOfContainers;
    }

    @Override
    public String toString() {
        return "BenchmarkConfig{"
                + "NumberOfAgents=" + NumberOfAgents
                + ", NumberOfContainers=" + NumberOfContainers
                + ", SmallLoads=" + SmallLoads
                + ", MediumLoads=" + MediumLoads
                + ", HighLoads=" + HighLoads
                + ", CooperationOn=" + CooperationOn
                + ", BalancerOn=" + BalancerOn
                + ", BackupOn=" + BackupOn
                + '}';
    }

    public BenchmarkExperimentUnit(String data) {
        
        this.ExperimentRaw = data;

        String[] config = data.split(",");

        this.ExperimentID = Integer.parseInt(config[0]);
        this.NumberOfContainers = Integer.parseInt(config[1]);
        this.NumberOfAgents = Integer.parseInt(config[2]);
        this.SmallLoads = Integer.parseInt(config[3]);
        this.MediumLoads = Integer.parseInt(config[4]);
        this.HighLoads = Integer.parseInt(config[5]);
        this.CooperationOn = "1".equals(config[6]);
        this.BalancerOn = "1".equals(config[7]);
        if (Integer.parseInt(config[8]) > 0) {
            this.BackupOn = true;
            this.BackupTime = Integer.parseInt(config[8]);
        } else {
            this.BackupOn = false;
            this.BackupTime = 0;
        }
        
        System.out.print(this);
        
    }

}
