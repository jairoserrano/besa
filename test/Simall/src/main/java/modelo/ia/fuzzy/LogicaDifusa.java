/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.ia.fuzzy;

import SIMALL.modelo.ia.fuzzy.pares.BloqueDeReglas;
import SIMALL.modelo.util.Punto;
import java.util.ArrayList;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 *
 * @author dsvalencia
 */
public class LogicaDifusa {
    private String nombre;
    ArrayList<VariableDeEntrada> variablesDeEntrada;
    ArrayList<VariableDeSalida> variablesDeSalida;
    BloqueDeReglas matrizDeReglas;
    FIS fis;
    

    public LogicaDifusa(String nombre, ArrayList<VariableDeEntrada> variablesDeEntrada, ArrayList<VariableDeSalida> variablesDeSalida, BloqueDeReglas matrizDeReglas) {
        this.nombre = nombre;
        this.variablesDeEntrada = variablesDeEntrada;
        this.variablesDeSalida = variablesDeSalida;
        this.matrizDeReglas = matrizDeReglas;
        fis=new FIS();
        configurar();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<VariableDeEntrada> getVariablesDeEntrada() {
        return variablesDeEntrada;
    }

    public void setVariablesDeEntrada(ArrayList<VariableDeEntrada> variablesDeEntrada) {
        this.variablesDeEntrada = variablesDeEntrada;
    }

    public ArrayList<VariableDeSalida> getVariablesDeSalida() {
        return variablesDeSalida;
    }

    public void setVariablesDeSalida(ArrayList<VariableDeSalida> variablesDeSalida) {
        this.variablesDeSalida = variablesDeSalida;
    }

    public BloqueDeReglas getMatrizDeReglas() {
        return matrizDeReglas;
    }

    public void setMatrizDeReglas(BloqueDeReglas matrizDeReglas) {
        this.matrizDeReglas = matrizDeReglas;
    }

    private void configurar() {
        String configuracion=generarConfiguracion();
        try{
            fis= FIS.createFromString(configuracion,true);
        }catch(Exception e){
            System.err.println("Error configurando los parámetros de lógica difusa."+e.getMessage());
        }
    }

    private String generarConfiguracion() {
        String configuracion="FUNCTION_BLOCK "+this.nombre+"\n";
        configuracion+="VAR_INPUT\n";
        for(VariableDeEntrada variableDeEntrada:variablesDeEntrada){
            configuracion+=variableDeEntrada.getNombre()+":REAL;\n";
        }
        configuracion+="END_VAR\n";
        configuracion+="VAR_OUTPUT\n";
        for(VariableDeSalida variableDeSalida:variablesDeSalida){
            configuracion+=variableDeSalida.getNombre()+":REAL;\n";
        }
        configuracion+="END_VAR\n";
        
        for(VariableDeEntrada variableDeEntrada:variablesDeEntrada){
            configuracion+="FUZZIFY "+variableDeEntrada.getNombre()+"\n";
            for(TerminoLinguistico terminoLinguistico:variableDeEntrada.getTerminosLinguisticos()){
                configuracion+="TERM "+terminoLinguistico.getNombre()+":=";
                for(Punto punto:terminoLinguistico.getPuntos()){
                    configuracion+="("+punto.getX()+","+punto.getY()+")";
                }
                configuracion+=";\n";
            }
            configuracion+="END_FUZZIFY\n";
        }
        
        for(VariableDeSalida variableDeSalida:variablesDeSalida){
            configuracion+="DEFUZZIFY "+variableDeSalida.getNombre()+"\n";
            for(TerminoLinguistico terminoLinguistico:variableDeSalida.getTerminosLinguisticos()){
                configuracion+="TERM "+terminoLinguistico.getNombre()+":=";
                for(Punto punto:terminoLinguistico.getPuntos()){
                    configuracion+="("+punto.getX()+","+punto.getY()+")";
                }
                configuracion+=";\n";
            }
            configuracion+="METHOD:"+variableDeSalida.getMetodo()+";\n";
            configuracion+="DEFAULT:="+variableDeSalida.getDefecto()+";\n";
            configuracion+="END_DEFUZZIFY\n";
        }
        
        if(matrizDeReglas!=null){
            configuracion+="RULEBLOCK MATRIZ_DE_REGLAS\n";
            configuracion+="AND:"+matrizDeReglas.getAnd()+";\n";
            configuracion+="ACT:"+matrizDeReglas.getAct()+";\n";
            configuracion+="ACCU:"+matrizDeReglas.getAccu()+";\n";
            int i=0;
            for(Condicion condicion:matrizDeReglas.getCondiciones()){
                configuracion+="RULE "+i+": IF "+condicion.getVariableDeEntrada1().getNombre()+" IS "+condicion.getTerminoLinguisticoVariableDeEntrada1().getNombre()+" AND "+condicion.getVariableDeEntrada2().getNombre()+" IS "+condicion.getTerminoLinguisticoVariableDeEntrada2().getNombre()+" THEN "+condicion.getVariableDeSalida().getNombre()+" IS "+condicion.getTerminoLinguisticoVariableDeSalida().getNombre()+";\n";
                i++;
            }
        }
        
        configuracion+="END_RULEBLOCK\n";
        configuracion+="END_FUNCTION_BLOCK\n";
        return configuracion;
        
    }

    public void mostrar() {
        FunctionBlock functionBlock=fis.getFunctionBlock(nombre);
        JFuzzyChart.get().chart(functionBlock);
    }
    
    public void setVariableDeEntrada(String variableDeEntrada, double valor){
        fis.setVariable(variableDeEntrada, valor);
    }
    
    public double getVariableDeSalida(String variableDeSalida){
        return fis.getVariable(variableDeSalida).getValue();
    }
    
    public void evaluar(){
        fis.evaluate();
    }

    public void mostrarVariableDeSalida(String variableDeSalida) {
        Variable variable = fis.getFunctionBlock(nombre).getVariable(variableDeSalida);
        JFuzzyChart.get().chart(variable, variable.getDefuzzifier(), true);
    }
    
    @Override
    public String toString(){
        return fis.toString();
    }

    public double getMiembroDeVariable(String nombreVariable, String miembro) {
        Variable variable=fis.getVariable(nombreVariable);
        if(variable!=null){
            return variable.getMembership(miembro);
        }
        System.err.println("Se presentó error al cargar la variable "+nombreVariable+ " y miembro de "+miembro);
        System.exit(0);
        return -1;
    }
}
