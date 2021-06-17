/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientAgent;

import BESA.Kernel.Agent.StateBESA;
import BESA.Log.ReportBESA;
import Utils.BenchmarkExperimentUnit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author jairo
 */
public final class ClientAgentState extends StateBESA implements Serializable {

    public BenchmarkExperimentUnit CurrentExperiment;
    ArrayList<String> TaskList = new ArrayList<>();
    private int TaskListID = 0;
    private int TaskListDone = 0;
    private String WorkerContainerAlias;
    private String WorkerContainerIP;

    public ClientAgentState(BenchmarkExperimentUnit Experiment, String WorkerContainerAlias, String WorkerContainerIP) {
        super();

        this.WorkerContainerAlias = WorkerContainerAlias;
        this.WorkerContainerIP = WorkerContainerIP;
        this.CurrentExperiment = Experiment;
        this.TaskListDone = 0;
        this.TaskListID = 0;

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
            ReportBESA.info("inOrder Tasks");
        } else {
            Collections.shuffle(TaskList);
            ReportBESA.info("Shuffle Tasks");
        }
        
        ReportBESA.info("Generated tasks: " + getTotalTasks());

    }

    public String getWorkerContainerAlias() {
        return WorkerContainerAlias;
    }

    public void setWorkerContainerAlias(String WorkerContainerAlias) {
        this.WorkerContainerAlias = WorkerContainerAlias;
    }

    public String getWorkerContainerIP() {
        return WorkerContainerIP;
    }

    public void setWorkerContainerIP(String WorkerContainerIP) {
        this.WorkerContainerIP = WorkerContainerIP;
    }

    public synchronized int getTaskListDone() {
        return TaskListDone;
    }

    public synchronized void TaskDone() {
        TaskListDone++;
    }

    public synchronized int getTaskListID() {
        return TaskListID;
    }

    public synchronized int getTotalTasks() {
        return TaskList.size();
    }

    public synchronized String getTask() {

        String Task;
        try {
            Task = TaskList.get(TaskListID);
            TaskListID++;
            //ReportBESA.info("Tareas por enviar " + (this.getTotalTasks() - this.getTaskListDone()));
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
        return TaskListID != TaskListDone;
    }

}
