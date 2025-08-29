package com.GA.fitness;

import com.GA.IndividualImage;
import com.utils.BitMapImage;
import com.utils.ImageUtils;

public class ImageLikenessFitness extends FitnessFunction {

    BitMapImage targetImage;

    public ImageLikenessFitness(BitMapImage targetImage, int height,  int width) {
        this.targetImage = ImageUtils.resize(targetImage, height, width);
    }

    public double fitness(IndividualImage image) {
        double fitness = 0.0;
        int[][][] rgbTarget = targetImage.getRgb();
        int[][][] rgbImage = image.getImage().getRgb();
        for(int y = 0; y < rgbTarget.length; y++) {
            for(int x = 0; x < rgbTarget[0].length; x++) {
                fitness += 765.0;
                fitness -= 0.0 + Math.abs(rgbTarget[y][x][0] - rgbImage[y][x][0]);
                fitness -= 0.0 + Math.abs(rgbTarget[y][x][1] - rgbImage[y][x][1]);
                fitness -= 0.0 + Math.abs(rgbTarget[y][x][2] - rgbImage[y][x][2]);
            }
        }
        return fitness;
    }
}
