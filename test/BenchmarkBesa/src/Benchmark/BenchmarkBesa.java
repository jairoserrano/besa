/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Benchmark;

import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;
import Utils.BenchmarkConfig;
import Utils.BenchmarkExperimentUnit;
import Utils.BenchmarkLauncher;

/**
 *
 * @author jairo
 */
public class BenchmarkBesa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create Config Instance
        BenchmarkConfig config = BenchmarkConfig.getConfig(args);
        // Create Experiment Data from file
        BenchmarkExperimentUnit ExperimentData = config.getCurrentExperiment();
        BenchmarkLauncher Experiment;

        ReportBESA.info("Iniciando Contenedor " + config.getContainerID());

        AdmBESA adminBesa = AdmBESA.getInstance("config/Container_0"
                + config.getContainerID()
                + ".xml");

        while (ExperimentData != null) {
            ReportBESA.info("Iniciando experimento,"+ config.getCurrentExperimentID() + "," + ExperimentData);
            Experiment = new BenchmarkLauncher(ExperimentData, adminBesa);
            Experiment.runSimulation();
            Experiment.stopSimulation();
            ExperimentData = config.getCurrentExperiment();

        }

    }

}
