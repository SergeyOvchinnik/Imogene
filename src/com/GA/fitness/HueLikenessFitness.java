package com.GA.fitness;

import com.GA.IndividualImage;
import com.utils.BitMapImage;
import com.utils.ImageUtils;

public class HueLikenessFitness extends FitnessFunction {

    private double[][] imageHues;

    public HueLikenessFitness(BitMapImage targetImage, int height, int width) {
        BitMapImage image = ImageUtils.resize(targetImage, height, width);

        int[][][] rgb = image.getRgb();
        imageHues = new double[height][width];

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixelValue = Math.max(rgb[y][x][0], Math.max(rgb[y][x][1], rgb[y][x][2]));
                int pixelChroma = pixelValue - Math.min(rgb[y][x][0], Math.min(rgb[y][x][1], rgb[y][x][2]));
                double pixelSaturation = 0.0;
                if(pixelValue > 0)
                    pixelSaturation = (0.0 + pixelChroma) / (0.0 + pixelValue);
                double pixelHue = 0.0;
                if(pixelChroma > 0) {
                    if(pixelValue == rgb[y][x][0]) {
                        pixelHue = 60.0 * (((0.0 + rgb[y][x][1] - rgb[y][x][2]) / pixelChroma) % 6.0);
                    }
                    else if(pixelValue == rgb[y][x][1]) {
                        pixelHue = 60.0 * (((0.0 + rgb[y][x][2] - rgb[y][x][0]) / pixelChroma) + 2);
                    }
                    else if(pixelValue == rgb[y][x][2]) {
                        pixelHue = 60.0 * (((0.0 + rgb[y][x][0] - rgb[y][x][1]) / pixelChroma) + 4);
                    }
                }
                imageHues[y][x] = pixelHue;
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
                int pixelChroma = pixelValue - Math.min(rgbImage[y][x][0], Math.min(rgbImage[y][x][1], rgbImage[y][x][2]));
                double pixelHue = 0.0;
                if(pixelChroma > 0) {
                    if(pixelValue == rgbImage[y][x][0]) {
                        pixelHue = 60.0 * (((0.0 + rgbImage[y][x][1] - rgbImage[y][x][2]) / pixelChroma) % 6.0);
                    }
                    else if(pixelValue == rgbImage[y][x][1]) {
                        pixelHue = 60.0 * (((0.0 + rgbImage[y][x][2] - rgbImage[y][x][0]) / pixelChroma) + 2);
                    }
                    else if(pixelValue == rgbImage[y][x][2]) {
                        pixelHue = 60.0 * (((0.0 + rgbImage[y][x][0] - rgbImage[y][x][1]) / pixelChroma) + 4);
                    }
                }
                fitness += 180.0 - Math.min(Math.abs(pixelHue - imageHues[y][x]), 360.0 - Math.abs(imageHues[y][x] - pixelHue));
            }
        }
        return fitness;
    }

    @Override
    protected void calculateMaxFitness(int height, int width) {
        theoreticalMaximumFitness = 180.0 * height * width;
    }
}