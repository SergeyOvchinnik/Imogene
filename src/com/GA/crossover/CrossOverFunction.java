package com.GA.crossover;

import com.GA.IndividualImage;
import com.GA.fitness.FitnessFunction;

public abstract class CrossOverFunction {

    protected boolean weighted;
    protected boolean greedy;
    protected int attempts;
    protected FitnessFunction fitnessFunction;

    public void makeGreedy(int attempts, FitnessFunction fitnessFunction) {
        this.greedy = true;
        this.attempts = attempts;
        this.fitnessFunction = fitnessFunction;
    }

    public void makeWeighted() {
        weighted = true;
    }

    public abstract IndividualImage crossover(IndividualImage image1, IndividualImage image2);

    public abstract IndividualImage weightedCrossover(IndividualImage image1, IndividualImage image2);

}
