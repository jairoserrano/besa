/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jairo Serrano <jaserrano@javeriana.edu.co>
 *
 */
public final class BenchmarkConfig {

    private static BenchmarkConfig instance = null;
    private final ArrayList<BenchmarkExperimentUnit> experiments;
    private int CurrentExperimentID = 0;

    public int getCurrentExperimentID() {
        return CurrentExperimentID;
    }
    private String ContainerID = "";
    private String ConfigFileName = "";

    public static BenchmarkConfig getConfig() {
        return BenchmarkConfig.instance;
    }

    public static BenchmarkConfig getConfig(String args[]) {
        BenchmarkConfig.instance = new BenchmarkConfig(args);
        return BenchmarkConfig.instance;
    }

    public boolean isMainContainer() {
        return this.getContainerID().equals("0")
                || this.getContainerID().equals("100");
    }

    public String getContainerID() {
        return ContainerID;
    }

    public BenchmarkExperimentUnit getCurrentExperiment() {
        try {
            BenchmarkExperimentUnit CurrentExperiment
                    = experiments.get(this.CurrentExperimentID);
            this.CurrentExperimentID++;
            return CurrentExperiment;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     *
     *
     * @param args
     */
    private BenchmarkConfig(String args[]) {

        // Arraylist with all experiments
        experiments = new ArrayList<>();
        // Container ID from Command Line parameters
        if (args[0].length() > 0) {
            ContainerID = args[0];
        } else {
            ContainerID = "";
            System.out.println("Falta el primer parametros del contenedorID.");
            System.exit(0);
        }

        try {
            // Container ID from Command Line parameters
            if (args[1].length() > 0) {
                ConfigFileName = args[1];
                try {
                    File rawExperiment = new File(ConfigFileName);
                    try ( Scanner myReader = new Scanner(rawExperiment)) {
                        while (myReader.hasNextLine()) {
                            String data = myReader.nextLine();
                            experiments.add(new BenchmarkExperimentUnit(data));
                        }
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BenchmarkConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Master mode Started.");
            }
        } catch (Exception ex) {
            System.out.println("Worker mode Started");
        }
    }
}
