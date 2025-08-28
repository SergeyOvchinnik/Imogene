package com.GA.mutation;

import com.GA.IndividualImage;
import com.application.Application;
import com.utils.BitMapImage;
import com.utils.Util;

/**
 * A simple mutation function that replaces some of the pixels
 * with completely random colours.
 *
 * This mutation requires two parameters. The first parameter is
 * the probability that the mutation will be applied, e.g. if it
 * is set to 10% than only around 10% of the images will be
 * changed by this function. The second parameter is the pixel
 * probability, which determines how likely each individual
 * pixel is to be replaced when the mutation is applied, e.g.
 * if it is set to 10% than around 10% of all pixels will be
 * replaced with random colours.
 */
public class RandomPixelsMutation extends MutationFunction {

    private double mutationProbability;
    private double mutationPixelProbability;

    public RandomPixelsMutation(double mutationProbability, double mutationPixelProbability) {
        this.mutationProbability = mutationProbability;
        this.mutationPixelProbability = mutationPixelProbability;
    }

    public IndividualImage mutate(IndividualImage image) {
        if(greedy) {
                IndividualImage out = mutateAttempt(image);
                double bestFitness = fitnessFunction.fitness(out);
                for(int i = 1; i < attempts; i++) {
                    IndividualImage mutant = mutateAttempt(image);
                    double fitness = fitnessFunction.fitness(mutant);
                    if(fitness > bestFitness) {
                        bestFitness = fitness;
                        out = mutant;
                    }
                }
                return out;
        }
        else {
            return mutate(image);
        }
    }

    public IndividualImage mutateAttempt(IndividualImage individualImage) {
        int[][][] rgb = individualImage.getImage().getRgb();
        int[][][] rgbMutated = individualImage.copy().getImage().getRgb(); // TODO: strange implementation

        if(Util.rng.nextDouble(1.0) > mutationProbability)
            return new IndividualImage(new BitMapImage(rgbMutated));

        for(int y = 0; y < rgb.length; y++) {
            for(int x = 0; x < rgb[0].length; x++) {
                if(Application.rng.nextDouble(100.0) < mutationPixelProbability) {
                    rgbMutated[y][x][0] = Application.rng.nextInt(256);
                    rgbMutated[y][x][1] = Application.rng.nextInt(256);
                    rgbMutated[y][x][2] = Application.rng.nextInt(256);
                }
            }
        }

        return new IndividualImage(new BitMapImage(rgbMutated));
    }
}
