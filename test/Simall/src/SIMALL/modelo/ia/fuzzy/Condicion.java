/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.ia.fuzzy;

/**
 *
 * @author dsvalencia
 */
public class Condicion {

    public Condicion(VariableDeEntrada variableDeEntrada1, TerminoLinguistico terminoLinguisticoVariableDeEntrada1, VariableDeEntrada variableDeEntrada2, TerminoLinguistico terminoLinguisticoVariableDeEntrada2, VariableDeSalida variableDeSalida, TerminoLinguistico terminoLinguisticoVariableDeSalida) {
        this.variableDeEntrada1 = variableDeEntrada1;
        this.terminoLinguisticoVariableDeEntrada1 = terminoLinguisticoVariableDeEntrada1;
        this.variableDeEntrada2 = variableDeEntrada2;
        this.terminoLinguisticoVariableDeEntrada2 = terminoLinguisticoVariableDeEntrada2;
        this.variableDeSalida = variableDeSalida;
        this.terminoLinguisticoVariableDeSalida = terminoLinguisticoVariableDeSalida;
    }

    
    private VariableDeEntrada variableDeEntrada1;
    private TerminoLinguistico terminoLinguisticoVariableDeEntrada1;
    private VariableDeEntrada variableDeEntrada2;
    private TerminoLinguistico terminoLinguisticoVariableDeEntrada2;
    private VariableDeSalida variableDeSalida;
    private TerminoLinguistico terminoLinguisticoVariableDeSalida;
    
    public VariableDeEntrada getVariableDeEntrada1() {
        return variableDeEntrada1;
    }

    public void setVariableDeEntrada1(VariableDeEntrada variableDeEntrada1) {
        this.variableDeEntrada1 = variableDeEntrada1;
    }

    public TerminoLinguistico getTerminoLinguisticoVariableDeEntrada1() {
        return terminoLinguisticoVariableDeEntrada1;
    }

    public void setTerminoLinguisticoVariableDeEntrada1(TerminoLinguistico terminoLinguisticoVariableDeEntrada1) {
        this.terminoLinguisticoVariableDeEntrada1 = terminoLinguisticoVariableDeEntrada1;
    }

    public VariableDeEntrada getVariableDeEntrada2() {
        return variableDeEntrada2;
    }

    public void setVariableDeEntrada2(VariableDeEntrada variableDeEntrada2) {
        this.variableDeEntrada2 = variableDeEntrada2;
    }

    public TerminoLinguistico getTerminoLinguisticoVariableDeEntrada2() {
        return terminoLinguisticoVariableDeEntrada2;
    }

    public void setTerminoLinguisticoVariableDeEntrada2(TerminoLinguistico terminoLinguisticoVariableDeEntrada2) {
        this.terminoLinguisticoVariableDeEntrada2 = terminoLinguisticoVariableDeEntrada2;
    }

    public VariableDeSalida getVariableDeSalida() {
        return variableDeSalida;
    }

    public void setVariableDeSalida(VariableDeSalida variableDeSalida) {
        this.variableDeSalida = variableDeSalida;
    }

    public TerminoLinguistico getTerminoLinguisticoVariableDeSalida() {
        return terminoLinguisticoVariableDeSalida;
    }

    public void setTerminoLinguisticoVariableDeSalida(TerminoLinguistico terminoLinguisticoVariableDeSalida) {
        this.terminoLinguisticoVariableDeSalida = terminoLinguisticoVariableDeSalida;
    }

    
    
    
}
