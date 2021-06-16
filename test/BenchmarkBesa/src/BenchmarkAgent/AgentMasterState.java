/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchmarkAgent;

import BESA.Kernel.Agent.StateBESA;
import BESA.Log.ReportBESA;
import Utils.BenchmarkConfig;
import Utils.BenchmarkExperimentUnit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author jairo
 */
public final class AgentMasterState extends StateBESA implements Serializable {

    BenchmarkConfig config;
    ArrayList<String> TaskList = new ArrayList<>();
    private int TaskListID = 0;
    private int TaskListDone = 0;

    public AgentMasterState(BenchmarkExperimentUnit Experiment) {
        super();

        ReportBESA.info("Generación de tareas iniciada");

        for (int i = 0; i < Experiment.getSmallLoads(); i++) {
            this.TaskList.add("Small");
        }
        for (int i = 0; i < Experiment.getMediumLoads(); i++) {
            this.TaskList.add("Medium");
        }
        for (int i = 0; i < Experiment.getHighLoads(); i++) {
            this.TaskList.add("High");
        }

        if (Experiment.isOrderOn()) {
            ReportBESA.info("Tareas en orden");
            Collections.shuffle(this.TaskList);
        } else {
            ReportBESA.info("Tareas en desorden");
        }

    }

    public synchronized int getTaskListDone() {
        return TaskListDone;
    }

    public synchronized void TaskDone() {
        this.TaskListDone++;
    }

    public synchronized int getTaskListID() {
        return TaskListID;
    }

    public synchronized int Tasks() {
        return this.TaskList.size();
    }

    public synchronized String getTask() {

        String Task;
        try {
            Task = this.TaskList.get(this.TaskListID);
            this.TaskListID++;
        } catch (Exception ex) {
            Task = null;
        }
        return Task;
    }

    /**
     * Return true or false
     *
     * @return boolean true if simulation is running
     */
    public synchronized boolean SimulationRunning() {
        if (this.TaskListID != this.TaskListDone) {
            return true;
        } else {
            return false;
        }
    }

}
