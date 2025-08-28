package com.GA.fitness;

import com.GA.IndividualImage;

public class ColourFitness extends FitnessFunction {

    private int r;
    private int g;
    private int b;

    public ColourFitness(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    // TODO: uses manhattan distance instead of euclidean distance
    public double fitness(IndividualImage image) {
        double fitness = 0;
        int[][][] rgb = image.getImage().getRgb();
        for(int y = 0; y < rgb.length; y++) {
            for(int x = 0; x < rgb[0].length; x++) {
                fitness += 255.0 - Math.abs(rgb[y][x][0] - r);
                fitness += 255.0 - Math.abs(rgb[y][x][1] - g);
                fitness += 255.0 - Math.abs(rgb[y][x][2] - b);
            }
        }
        return fitness;
    }

}
