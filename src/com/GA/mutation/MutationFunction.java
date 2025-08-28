package com.GA.mutation;

import com.GA.IndividualImage;
import com.GA.fitness.FitnessFunction;

/**
 * Mutation functions are used to make changes in the generated
 * individuals, regardless of their fitness. Conventional
 * mutation functions will have some probabilistic elements,
 * such as the probability that the mutation will be applied,
 * or probability that certain elements will be affected.
 *
 * Greedy option is available. It makes the mutation run not once
 * but several times, returning only the fittest result. This is
 * generally against the spirit of GA although it may be useful
 * in tasks where defining suitable mutation functions is difficult.
 */
public abstract class MutationFunction {

    protected boolean greedy;
    protected int attempts;
    protected FitnessFunction fitnessFunction;

    public void makeGreedy(int attempts, FitnessFunction fitnessFunction) {
        this.greedy = true;
        this.attempts = attempts;
        this.fitnessFunction = fitnessFunction;
    }

    public abstract IndividualImage mutate(IndividualImage individualImage);

}
