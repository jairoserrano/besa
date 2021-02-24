/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mastertest;

import BESA.Kernel.System.AdmBESA;

/**
 *
 * @author fabianjose
 */
public class MasterTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String name = "DE";
        System.out.println("THIS IS " + name + " \n\n\n");
        AdmBESA.getInstance("confbesaB.xml");
    }
}
