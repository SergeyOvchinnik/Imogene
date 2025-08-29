package com.GA.mutation;

import com.GA.IndividualImage;
import com.GA.fitness.FitnessFunction;

import java.util.ArrayList;

public class EnsembleMutation extends MutationFunction {

    ArrayList<MutationFunction> functions;
    ArrayList<Double> weights;

    FitnessFunction fitnessFunction;

    public EnsembleMutation(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
        functions = new ArrayList<MutationFunction>();
        weights = new ArrayList<Double>();
    }

    public void addFunction(MutationFunction function, double weight) {
        functions.add(function);
        weights.add(weight); // TODO: weights are currently unused, the mutate function just runs all of them and picks the best results, also does no harm
    }

    @Override
    public IndividualImage mutate(IndividualImage individualImage) {
        double bestFitness = fitnessFunction.fitness(individualImage); // TODO: look into using the already calculated fitnesses only
        IndividualImage bestImage = individualImage;
        for(MutationFunction function : functions) {
            IndividualImage newImage = function.mutate(individualImage);
            double fitness = fitnessFunction.fitness(newImage);
            if(fitness > bestFitness) continue;
            bestFitness = fitness;
            bestImage = newImage;
        }
        return bestImage;
    }
}
