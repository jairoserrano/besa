/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import BESA.Kernel.Agent.Event.DataBESA;

/**
 *
 * @author jairo
 */
public class ExperimentUnitMessage extends DataBESA {

    private BenchmarkExperimentUnit experiment;
    private String agentRef;

    public ExperimentUnitMessage(BenchmarkExperimentUnit experiment, String agentDest) {
        setExperiment(experiment);
    }

    public BenchmarkExperimentUnit getExperiment() {
        return experiment;
    }

    public void setExperiment(BenchmarkExperimentUnit experiment) {
        this.experiment = experiment;
    }

    public String getAgentRef() {
        return agentRef;
    }

    public void setAgentRef(String agentDest) {
        this.agentRef = agentDest;
    }

}
