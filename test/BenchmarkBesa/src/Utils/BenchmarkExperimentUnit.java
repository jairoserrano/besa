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

    public boolean isOrderOn() {
        return OrderOn;
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
                + ", OrderOn=" + OrderOn
                + ", CooperationOn=" + CooperationOn
                + ", BalancerOn=" + BalancerOn
                + ", BackupOn=" + BackupOn
                + '}';
    }

    public BenchmarkExperimentUnit(String data) {

        String[] config = data.split(",");

        this.NumberOfContainers = Integer.parseInt(config[0]);
        this.NumberOfAgents = Integer.parseInt(config[1]);
        this.SmallLoads = Integer.parseInt(config[2]);
        this.MediumLoads = Integer.parseInt(config[3]);
        this.HighLoads = Integer.parseInt(config[4]);
        this.OrderOn = "1".equals(config[5]);
        this.CooperationOn = "1".equals(config[6]);
        this.BalancerOn = "1".equals(config[7]);
        if (Integer.parseInt(config[8]) > 0) {
            this.BackupOn = true;
            this.BackupTime = Integer.parseInt(config[8]);
        } else {
            this.BackupOn = false;
            this.BackupTime = 0;
        }

    }

}
