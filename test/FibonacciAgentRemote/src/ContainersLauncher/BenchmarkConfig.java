/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

/**
 *
 * @author jairo
 */
public final class BenchmarkConfig {

    private int NumberOfAgentsPerContainer;
    private int FiboToCalculate;
    private int NumberOfContainers;

    public BenchmarkConfig() {
        this.setNumberOfAgentsPerContainer(100);
        this.setFiboToCalculate(40);
        this.setNumberOfContainers(5);
    }

    public int getNumberOfContainers() {
        return NumberOfContainers;
    }

    public void setNumberOfContainers(int NumberOfContainers) {
        this.NumberOfContainers = NumberOfContainers;
    }

    public String getFiboToCalculate() {
        return String.valueOf(FiboToCalculate);
    }

    public void setFiboToCalculate(int FiboToCalculate) {
        this.FiboToCalculate = FiboToCalculate;
    }

    public int getNumberOfAgentsPerContainer() {
        return NumberOfAgentsPerContainer;
    }

    public void setNumberOfAgentsPerContainer(int NumberOfAgentsPerContainer) {
        this.NumberOfAgentsPerContainer = NumberOfAgentsPerContainer;
    }

}
