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
    ArrayList<String> TaskList;
    private int TaskListID = 0;
    private int TaskListDone = 0;
    private ArrayList<String> WorkerContainerAlias;
    private ArrayList<String> WorkerContainerIP;

    public ClientAgentState() {
        super();
    }

    public void UpdateAgentState(BenchmarkExperimentUnit Experiment, ArrayList<String> WorkerContainerAlias, ArrayList<String> WorkerContainerIP) {

        this.TaskList = new ArrayList<>();
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
    // @TODO: REVISAR A DONDE SE VA A MOVER
    public String getCurrentContainerAlias(){
        return "MAS_00_01";
    }

    public ArrayList<String> getWorkerContainerAlias() {
        return WorkerContainerAlias;
    }

    public ArrayList<String> getWorkerContainerIP() {
        return WorkerContainerIP;
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

}
