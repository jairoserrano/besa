package holamundo;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;

/**
 *
 * @author fjroldan
 */
public class HablarGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA event) {
        Mensaje mensaje = (Mensaje) event.getData();
        switch(mensaje.getContenido()){
            case "Hola":
                ReportBESA.info("Hola mundo");
                break;
            case "Qué edad tienes?":
                EstadoAgente estado = (EstadoAgente) this.agent.getState();
                ReportBESA.info(String.format("Tengo %d años", estado.getEdad()));
                break;
            default:
                ReportBESA.warn("No te entiendo");                
        }
    }
    
}
