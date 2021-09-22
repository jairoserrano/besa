/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Jairo Serrano <jaserrano@javeriana.edu.co>
 *
 */
public final class BenchmarkConfig implements Serializable {

    private BenchmarkExperimentUnit experiment;
    private static BenchmarkConfig instance = null;
    private int CurrentExperimentID = 0;
    private String ContainerID = "";

    /**
     *
     * @return
     */
    public static BenchmarkConfig getConfig() {
        return instance;
    }

    /**
     *
     * @param args
     * @return
     */
    public static BenchmarkConfig getConfig(String args[]) {
        if (instance == null) {
            instance = new BenchmarkConfig(args);
        }
        return instance;
    }

    /**
     *
     * @return
     */
    public synchronized String getContainerID() {
        return ContainerID;
    }

    /**
     *
     * @return
     */
    public synchronized int getCurrentExperimentID() {
        return CurrentExperimentID;
    }

    /**
     * 
     * @param Containers
     * @return 
     */
    public ArrayList<String> getContainers(int Containers, int AgentsByContainer){
        
        // @TODO: Sacar a constructor
        Dotenv dotenv = Dotenv.load();
        String MAS_NAME = "MAS_00_0";
        if (dotenv.get("ENV").equals("remote")){
            MAS_NAME = "MAS_01_0";
        }
        
        ArrayList<String> ContainersAlias = new ArrayList<>();
        for (int i = 1; i <= Containers; i++) {
            for (int j = 1; j <= AgentsByContainer; j++) {
                ContainersAlias.add(MAS_NAME + i);
            }
        }
        return ContainersAlias;
    }

    /**
     *
     *
     * @param args
     */
    private BenchmarkConfig(String args[]) {

        // Container ID from Command Line parameters
        if (args[0].length() > 0) {
            ContainerID = args[0];
            this.setExperiment(new BenchmarkExperimentUnit(args[1]));
        } else {
            ContainerID = null;
            experiment = null;
            System.out.println("Falta el primer parametros del contenedorID.");
            System.exit(0);
        }
        
    }

    /**
     * @return the experiment
     */
    public BenchmarkExperimentUnit getExperiment() {
        return experiment;
    }

    /**
     * @param experiment the experiment to set
     */
    public void setExperiment(BenchmarkExperimentUnit experiment) {
        this.experiment = experiment;
    }
}
