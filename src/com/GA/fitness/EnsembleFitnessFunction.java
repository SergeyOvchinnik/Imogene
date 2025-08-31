package com.GA.fitness;

import com.GA.IndividualImage;

import java.util.ArrayList;

public class EnsembleFitnessFunction extends FitnessFunction {

    private ArrayList<FitnessFunction> functions = new ArrayList<FitnessFunction>();
    ArrayList<Double> weights = new ArrayList<Double>();

    public void addFunction(FitnessFunction fitnessFunction, double weight) {
        functions.add(fitnessFunction);
        weights.add(weight);
    }

    @Override
    public double fitness(IndividualImage image) {
        double fitness = 0.0;
        for(int i = 0; i < functions.size(); i++) {
            fitness += functions.get(i).fitness(image) * weights.get(i); // TODO: this does not take into account the individual differences between fitness functions in terms of their magnitude. Additional adjustments must be made when combining fitness functions once it becomes possible to retrieve their max fitness values.
        }
        return fitness;
    }
}
