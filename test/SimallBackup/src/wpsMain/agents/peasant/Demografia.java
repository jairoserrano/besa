/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpsMain.agents.peasant;


/**
 *
 * @author dsvalencia
 */
public class Demografia {
    
    private MercadoObjetivo mercadoObjetivo;
    private NivelEconomico nivelEconomico;

    public PresupuestoGasto getPresupuestoGasto() {
        return presupuestoGasto;
    }

    public void setPresupuestoGasto(PresupuestoGasto presupuestoGasto) {
        this.presupuestoGasto = presupuestoGasto;
    }
    private PresupuestoGasto presupuestoGasto;

    public NivelEconomico getNivelEconomico() {
        return nivelEconomico;
    }

    public void setNivelEconomico(NivelEconomico nivelEconomico) {
        this.nivelEconomico = nivelEconomico;
    }

    public ApetitoEndeudamiento getApetitoEndeudamiento() {
        return apetitoEndeudamiento;
    }

    public void setApetitoEndeudamiento(ApetitoEndeudamiento apetitoEndeudamiento) {
        this.apetitoEndeudamiento = apetitoEndeudamiento;
    }
    private ApetitoEndeudamiento apetitoEndeudamiento;

    public MercadoObjetivo getMercadoObjetivo() {
        return mercadoObjetivo;
    }

    public void setMercadoObjetivo(MercadoObjetivo mercadoObjetivo) {
        this.mercadoObjetivo = mercadoObjetivo;
    }

    public Demografia(MercadoObjetivo mercadoObjetivo, NivelEconomico nivelEconomico, ApetitoEndeudamiento apetitoEndeudamiento, PresupuestoGasto presupuestoGasto) {
        this.mercadoObjetivo = mercadoObjetivo;
        this.nivelEconomico = nivelEconomico;
        this.apetitoEndeudamiento = apetitoEndeudamiento;
        this.presupuestoGasto=presupuestoGasto;
    }
    
    public Demografia() {
        this.mercadoObjetivo = new MercadoObjetivo();
    }
    
    @Override
    public String toString(){
        String cadena="Demografia:\n\n";
        cadena+=mercadoObjetivo;
        cadena+=nivelEconomico;
        return cadena;
    }
}
