/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BESA.Remote.Balancer;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.BehaviorBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Una vez llega el nuevo agente se actualiza la lista y se alade el nuevo agente
 * @author bitas
 */
public class Update_Guard extends GuardBESA {

     /**
     * Actualiza los datos del agente balanceador para incluir al agente que llega
     */
    @Override
    public void funcExecGuard(EventBESA event) {

        AdmBESA localAdmin = AdmBESA.getInstance();
        AgHandlerBESA agl;
        Ag_Balancer_State estado = (Ag_Balancer_State) this.agent.getState();
        Data_Agent dataAgent = (Data_Agent) event.getData();
        ArrayList<Long> ids = null;
        HashMap<String, ArrayList<Long>> mapIds = (HashMap<String, ArrayList<Long>>) estado.getThreadsIDs();

        try {
            agl = localAdmin.getHandlerByAlias(dataAgent.getAlias());
            ids = new ArrayList<>();
            //Se agregan los hilos de sus comportamientos a la lista
            for (BehaviorBESA behavior : agl.getAg().getBehaviors()) {
                ids.add(behavior.getThread().getId());
            }
            //Se relacionan los id's de los hilos con el alias del agente
            mapIds.put(agl.getAlias(), ids);
        } catch (ExceptionBESA ex) {
            Logger.getLogger(BalancerBESA.class.getName()).log(Level.SEVERE, null, ex);
        }

        estado.setThreadsIDs(mapIds); //Se agrega el agente nuevo a los datos
    }

}
