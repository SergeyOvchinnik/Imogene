package com.GA.crossover;

import com.GA.IndividualImage;
import com.GA.generation.GenerationFunction;
import com.utils.BitMapImage;
import com.utils.Util;

/**
 * TODO: weighted and greedy options are handled poorly, find a better solution
 */

public class BitwiseCrossOver extends CrossOverFunction {

    @Override
    public IndividualImage crossover(IndividualImage image1, IndividualImage image2) {
        if(greedy) {
            IndividualImage out = crossOverAttempt(image1, image2);
            double bestFitness = fitnessFunction.fitness(out);
            for(int i = 1; i < attempts; i++) {
                IndividualImage child = crossOverAttempt(image1, image2);
                double fitness = fitnessFunction.fitness(child);
                if(fitness > bestFitness) {
                    bestFitness = fitness;
                    out = child;
                }
            }
            return out;
        }
        else {
            return crossOverAttempt(image1, image2);
        }
    }

    @Override
    public IndividualImage weightedCrossover(IndividualImage image1, IndividualImage image2) {
        if(greedy) {
            IndividualImage out = crossOverAttempt(image1, image2);
            double bestFitness = fitnessFunction.fitness(out);
            for(int i = 1; i < attempts; i++) {
                IndividualImage child = crossOverAttempt(image1, image2);
                double fitness = fitnessFunction.fitness(child);
                if(fitness > bestFitness) {
                    bestFitness = fitness;
                    out = child;
                }
            }
            return out;
        }
        else {
            return crossOverAttempt(image1, image2);
        }
    }

    public IndividualImage crossOverAttempt(IndividualImage image1, IndividualImage image2) {
        if(weighted) {
            double fitness1 = image1.getFitness();
            double fitness2 = image2.getFitness();
            double p1 = fitness1 / (fitness1 + fitness2);
            double p2 = fitness2 / (fitness1 + fitness2);
            return crossOverAttemptWeighted(image1, image2, p1, p2);
        }
        else {
            return crossOverAttemptWeighted(image1, image2, 0.5, 0.5);
        }
    }

    public IndividualImage crossOverAttemptWeighted(IndividualImage image1, IndividualImage image2, double p1, double p2) {
        int[][][] rgb1 = image1.getImage().getRgb();
        int[][][] rgb2 = image2.getImage().getRgb();
        int[][][] rgbChild = new int[rgb1.length][rgb1[0].length][3];
        for(int y = 0; y < rgb1.length; y++) {
            for(int x = 0; x < rgb1[0].length; x++) {
                if(Util.rng.nextDouble(p1 + p2) < p1) {
                    rgbChild[y][x][0] = rgb1[y][x][0];
                    rgbChild[y][x][1] = rgb1[y][x][1];
                    rgbChild[y][x][2] = rgb1[y][x][2];
                } else {
                    rgbChild[y][x][0] = rgb2[y][x][0];
                    rgbChild[y][x][1] = rgb2[y][x][1];
                    rgbChild[y][x][2] = rgb2[y][x][2];
                }
            }
        }
        IndividualImage child = new IndividualImage(new BitMapImage(rgbChild));
        return child;
    }

}
