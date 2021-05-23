/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

/**
 *
 * @author Jairo Serrano <jaserrano@javeriana.edu.co>
 *
 */
public final class BenchmarkConfig {

    private static BenchmarkConfig instance = null;

    private final int NumberOfAgentsPerContainer;
    private final int NumberOfContainers;
    private final int SmallLoads;
    private final int MediumLoads;
    private final int HighLoads;
    private final int InOrder;
    private final int Cooperation;
    private final int BalancerOn;
    private final int BackupOn;

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
        this.NumberOfAgentsPerContainer = Integer.parseInt(config[1]);
        this.SmallLoads = Integer.parseInt(config[2]);
        this.MediumLoads = Integer.parseInt(config[3]);
        this.HighLoads = Integer.parseInt(config[4]);
        this.InOrder = Integer.parseInt(config[5]);
        this.Cooperation = Integer.parseInt(config[6]);
        this.BalancerOn = Integer.parseInt(config[7]);
        this.BackupOn = Integer.parseInt(config[8]);

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

    public int getInOrder() {
        return InOrder;
    }

    public int getCooperation() {
        return Cooperation;
    }

    public int getBalancerOn() {
        return BalancerOn;
    }

    public int getBackupOn() {
        return BackupOn;
    }

    public int getNumberOfContainers() {
        return NumberOfContainers;
    }

    public int getNumberOfAgentsPerContainer() {
        return NumberOfAgentsPerContainer;
    }

}
