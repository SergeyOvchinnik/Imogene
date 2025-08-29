package com.GA.mutation;

import com.GA.IndividualImage;
import com.GA.fitness.FitnessFunction;
import com.utils.BitMapImage;
import com.utils.ImageUtils;
import com.utils.Util;

public class SmoothMutation extends MutationFunction{

    private double mutationProbability;
    private boolean doNoHarm;
    private FitnessFunction fitnessFunction;

    public SmoothMutation(double mutationProbability, boolean doNoHarm, FitnessFunction fitnessFunction) {
        this.mutationProbability = mutationProbability;
        this.doNoHarm = doNoHarm;
        this.fitnessFunction = fitnessFunction;
    }

    public IndividualImage mutate(IndividualImage image) {

        if(Util.rng.nextDouble(1.0) > mutationProbability)
            return image.copy();

        IndividualImage smoothed = new IndividualImage(ImageUtils.smoothFilter(image.getImage()));

        if(doNoHarm)
            if (fitnessFunction.fitness(smoothed) < fitnessFunction.fitness(image)) // Horrible implementation, rewrite
                return image.copy();
        return smoothed;
    }
}
