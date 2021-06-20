/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Benchmark;

import BESA.Kernel.System.AdmBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author jairo
 */
public class BenchmarkServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AdmBESA adminBesa
                = AdmBESA.getInstance(
                        "config/Container_" + args[0] + ".xml"
                );
        ReportBESA.info("Container " + args[0] + " started");

    }

}
