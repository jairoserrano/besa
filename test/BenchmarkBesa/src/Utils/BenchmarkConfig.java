/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import BESA.Log.ReportBESA;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Jairo Serrano <jaserrano@javeriana.edu.co>
 *
 */
public final class BenchmarkConfig implements Serializable {

    private final ArrayList<BenchmarkExperimentUnit> experiments;
    private BlockingQueue<Boolean> ReadyQueue;
    private static BenchmarkConfig instance = null;
    private int CurrentExperimentID = 0;
    private String ContainerID = "";
    private String ConfigFileName = "";

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
    public boolean isNextExperimentReady() {
        try {
            ReportBESA.info("Esperando para lanzar");
            Boolean nextReady = ReadyQueue.take();
            ReportBESA.info("Listo para lanzar");
            return nextReady;
        } catch (InterruptedException ex) {
            ReportBESA.error(ex);
            return false;
        }
    }

    /**
     *
     */
    public void setNextExperimentReady() {
        try {
            ReadyQueue.put(true);
            ReportBESA.info("setNextExperimentReady");
        } catch (InterruptedException ex) {
            ReportBESA.error(ex);
        }
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
    public BenchmarkExperimentUnit getNextExperiment() {
        BenchmarkExperimentUnit CurrentExperiment;
        try {
            CurrentExperiment = experiments.get(CurrentExperimentID);
            this.CurrentExperimentID++;
        } catch (IndexOutOfBoundsException ex) {
            CurrentExperiment = null;
        }
        return CurrentExperiment;
    }

    /**
     *
     * @return
     */
    public synchronized int getExperimentCount() {
        return experiments.size();
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

        // Arraylist with all experiments
        experiments = new ArrayList<>();
        ReadyQueue = new LinkedBlockingDeque();
        // Container ID from Command Line parameters
        if (args[0].length() > 0) {
            ContainerID = args[0];
        } else {
            ContainerID = "";
            System.out.println("Falta el primer parametros del contenedorID.");
            System.exit(0);
        }

        // Container ID from Command Line parameters
        if (args[1].length() > 0) {
            ConfigFileName = args[1];
            try {
                File rawExperiment = new File(ConfigFileName);
                try (Scanner myReader = new Scanner(rawExperiment)) {
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        experiments.add(new BenchmarkExperimentUnit(data));
                    }
                }
            } catch (FileNotFoundException ex) {
                ReportBESA.error(ex);
            }
            System.out.println("Master mode Started.");
        }
    }
}
