package com.GA.mutation;

import com.GA.IndividualImage;
import com.application.Application;
import com.utils.BitMapImage;
import com.utils.Util;

public class RandomPixelAdjustmentsMutation extends MutationFunction {

    private int maxAdjustmentPerColour;
    private double mutationProbability;
    private double mutationPixelProbability;

    public RandomPixelAdjustmentsMutation(int maxAdjustmentPerColour, double mutationProbability, double mutationPixelProbability) {
        this.mutationProbability = mutationProbability;
        this.mutationPixelProbability = mutationPixelProbability;this.maxAdjustmentPerColour = maxAdjustmentPerColour;
    }

    @Override
    public IndividualImage mutate(IndividualImage individualImage) {
        int[][][] rgb = individualImage.getImage().getRgb();
        int[][][] rgbMutated = individualImage.copy().getImage().getRgb(); // TODO: strange implementation

        if(Util.rng.nextDouble(1.0) > mutationProbability)
            return new IndividualImage(new BitMapImage(rgbMutated));

        for(int y = 0; y < rgb.length; y++) {
            for(int x = 0; x < rgb[0].length; x++) {
                if(Application.rng.nextDouble(100.0) < mutationPixelProbability) {
                    for(int c = 0; c < 3; c++) {
                        rgbMutated[y][x][c] += (int) Math.round(maxAdjustmentPerColour - maxAdjustmentPerColour * Util.rng.nextDouble(2.0));
                        if(rgbMutated[y][x][c] < 0)
                            rgbMutated[y][x][c] = 0;
                        if(rgbMutated[y][x][c] > 255)
                            rgbMutated[y][x][c] = 255;
                    }
                }
            }
        }
        return new  IndividualImage(new BitMapImage(rgbMutated));
    }

}
