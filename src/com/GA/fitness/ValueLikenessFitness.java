package com.GA.fitness;

import com.GA.IndividualImage;
import com.utils.BitMapImage;
import com.utils.ImageUtils;

public class ValueLikenessFitness extends FitnessFunction {

    // Maximum possible value distances that each pixel can have from the target colour
    private int[][] vDiffs;

    private int[][] imageValues;

    public ValueLikenessFitness(BitMapImage targetImage, int height, int width) {
        BitMapImage image = ImageUtils.resize(targetImage, height, width);

        int[][][] rgb = image.getRgb();
        vDiffs = new int[height][width];
        imageValues = new int[height][width];

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixelValue = Math.max(rgb[y][x][0], Math.max(rgb[y][x][1], rgb[y][x][2]));
                imageValues[y][x] = pixelValue;
                vDiffs[y][x] = Math.max(pixelValue, 255 - pixelValue);
            }
        }
    }

    @Override
    protected double fitnessCalculation(IndividualImage image) {
        double fitness = 0.0;
        int[][][] rgbImage = image.getImage().getRgb();
        for(int y = 0; y < rgbImage.length; y++) {
            for(int x = 0; x < rgbImage[0].length; x++) {
                int pixelValue = Math.max(rgbImage[y][x][0], Math.max(rgbImage[y][x][1], rgbImage[y][x][2]));
                fitness += vDiffs[y][x] - Math.abs(imageValues[y][x] - pixelValue);
                //fitness += 255 - Math.abs(imageValues[y][x] - pixelValue); // TODO: experimental, revert to original later
            }
        }
        return fitness;
    }

    @Override
    protected void calculateMaxFitness(int height, int width) {
        double maxFitness = 0;
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                maxFitness += vDiffs[y][x];
            }
        }
        this.theoreticalMaximumFitness = maxFitness;
    }
}
