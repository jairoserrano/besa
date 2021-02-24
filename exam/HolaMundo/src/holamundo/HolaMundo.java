package holamundo;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;

public class HolaMundo {

    public static void main(String[] args) {
        try {
            
            AdmBESA adm = AdmBESA.getInstance();
            
            String alias = "Poto";
            
            EstadoAgente estado = new EstadoAgente();
            StructBESA estructura = new StructBESA();
            
            estructura.bindGuard(HablarGuard.class);
            
            double passwdAg = 0.91;
            Agente agente = new Agente(alias, estado, estructura, passwdAg);
            agente.start();
            
            AgHandlerBESA ah = adm.getHandlerByAid(agente.getAid());
            
            Mensaje mensaje = new Mensaje("Hola");
            
            EventBESA msj = new EventBESA(HablarGuard.class.getName(), mensaje);
            
            ah.sendEvent(msj);
            
            System.out.println("Envíe Hola");
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
    
}
