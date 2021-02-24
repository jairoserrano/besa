package holamundo;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;

public class Agente extends AgentBESA {

    public Agente(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernellAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
        EstadoAgente estado = (EstadoAgente) this.getState();
        estado.setEdad(4);
    }

    @Override
    public void shutdownAgent() {
    }
    
}
