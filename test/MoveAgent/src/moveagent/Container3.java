/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moveagent;

import BESA.Kernel.System.AdmBESA;

/**
 *
 * @author fabianjose
 */
public class Container3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //--------------------------------------------------------------------//
        // Creates and starts the BESA container.                             //
        //--------------------------------------------------------------------//
        AdmBESA adm = AdmBESA.getInstance("res/Container3.xml"); 
    }
}
