package com.application;

import com.GA.ImageGenerator;
import com.GA.IndividualImage;
import com.GA.fitness.EnsembleFitnessFunction;
import com.GA.fitness.FitnessFunction;
import com.GA.fitness.ImageLikenessFitness;
import com.utils.BitMapImage;
import com.utils.ImageRW;
import com.utils.ImageUtils;

import java.io.IOException;
import java.util.Arrays;

public class Tester {

    public static void main(String[] args) {

        BitMapImage cans = new BitMapImage(new int[1][1][3]);
        try {
            cans = ImageRW.readImage("benchmarkingImages/cans1.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FitnessFunction fitnessFunction1 = new ImageLikenessFitness(cans, cans.getHeight(), cans.getWidth(), false);
        fitnessFunction1.makeNormalised(cans.getHeight(), cans.getWidth());
        EnsembleFitnessFunction fitnessFunction = new EnsembleFitnessFunction();
        fitnessFunction.addFunction(fitnessFunction1, 1.0);
        double fitness = fitnessFunction.fitness(new IndividualImage(cans));
        System.out.println(fitness);
        double fitness2 = fitnessFunction.fitness(new IndividualImage(ImageGenerator.randomPixels(cans.getHeight(), cans.getWidth())));
        System.out.println(fitness2);



    }

}
