/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WorkAgent;

import BenchmarkTools.BenchmarkAgentReceiverGuard;
import BenchmarkTools.BenchmarkAgentReceiverMessage;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author jairo
 */
public class WorkAgentGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        WorkAgentMessage mensaje = (WorkAgentMessage) event.getData();
        ReportBESA.info("Mensaje recibido en " + this.agent.getAlias() + ", procesando " + mensaje.getContent());
        
        int mult = 3;

        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            String image_location = "./lib/data/" + mensaje.getContent();
            Mat src = Imgcodecs.imread(image_location);
            Mat dst = new Mat();
            Imgproc.resize(
                    src, dst, 
                    new Size(8000*mult, 8000*mult), 
                    0.1, 0.1, Imgproc.INTER_LANCZOS4
            );
            src = null;
            dst = null;            
            System.gc();
            //Imgcodecs.imwrite("./lib/res_99_" + mensaje.getContent(), dst);

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        ReportBESA.info("Terminada " + this.agent.getAlias() + " " + mensaje.getContent());

        AgHandlerBESA ah;
        try {
            ah = this.agent.getAdmLocal().getHandlerByAlias("BenchmarkAgent");
            EventBESA msj = new EventBESA(
                    BenchmarkAgentReceiverGuard.class.getName(),
                    new BenchmarkAgentReceiverMessage("Cálculo recibido en " + this.agent.getAlias())
            );
            ah.sendEvent(msj);
        } catch (ExceptionBESA ex) {
            Logger.getLogger(WorkAgentGuard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
