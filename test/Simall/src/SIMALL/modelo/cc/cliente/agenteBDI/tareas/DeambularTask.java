/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIMALL.modelo.cc.cliente.agenteBDI.tareas;

import rational.mapping.Believes;
import rational.mapping.Task;

/**
 *
 * @author Daniel
 */
public class DeambularTask extends Task {

    private boolean ejecutada;
    public DeambularTask() {
        this.ejecutada=false;
    }
    @Override
    public void executeTask(Believes blvs) {
        this.ejecutada=false;
        System.out.println( "Deambular Task .." );
        this.ejecutada=true;
    }
    @Override
    public void interruptTask(Believes blvs) {
        System.out.println("Tarea Interrumpida");
    }
    @Override
    public void cancelTask(Believes blvs) {
        System.out.println("Tarea Cancelada");
    }
    public boolean isExecuted() {
        if(this.ejecutada && this.taskState == STATE.WAITING_FOR_EXECUTION){
            this.ejecutada=false;
        }
        return ejecutada;
    }
    @Override
    public boolean checkFinish(Believes blvs) {
        return  isExecuted();
    }
}
