package com.GA.fitness;

import com.GA.IndividualImage;

public class CheckerboardFitness extends FitnessFunction {

    public double fitness(IndividualImage image) {
        int[][][] rgb = image.getImage().getRgb();
        double fitness = 0.0;
        for (int y = 0; y < rgb.length; y++) {
            for (int x = 0; x < rgb[0].length; x++) {
                int diff = 0;
                if (x > 0) {
                    diff += Math.abs(rgb[y][x][0] - rgb[y][x - 1][0]);
                    diff += Math.abs(rgb[y][x][1] - rgb[y][x - 1][1]);
                    diff += Math.abs(rgb[y][x][2] - rgb[y][x - 1][2]);
                }
                if (x < rgb[0].length - 1) {
                    diff += Math.abs(rgb[y][x][0] - rgb[y][x + 1][0]);
                    diff += Math.abs(rgb[y][x][1] - rgb[y][x + 1][1]);
                    diff += Math.abs(rgb[y][x][2] - rgb[y][x + 1][2]);
                }
                if (y > 0) {
                    diff += Math.abs(rgb[y][x][0] - rgb[y - 1][x][0]);
                    diff += Math.abs(rgb[y][x][1] - rgb[y - 1][x][1]);
                    diff += Math.abs(rgb[y][x][2] - rgb[y - 1][x][2]);
                }
                if (y < rgb.length - 1) {
                    diff += Math.abs(rgb[y][x][0] - rgb[y + 1][x][0]);
                    diff += Math.abs(rgb[y][x][1] - rgb[y + 1][x][1]);
                    diff += Math.abs(rgb[y][x][2] - rgb[y + 1][x][2]);
                }
                fitness += 0.0 + diff;
            }
        }
        return fitness;
    }

}
