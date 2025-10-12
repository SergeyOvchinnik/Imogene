package com.application;

import com.API.GenerationConnector;
import com.GA.ImageGenerator;
import com.GA.IndividualImage;
import com.GA.fitness.EnsembleFitnessFunction;
import com.GA.fitness.FitnessFunction;
import com.GA.fitness.ImageLikenessFitness;
import com.utils.BitMapImage;
import com.utils.ImageRW;
import com.utils.ImageUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

public class Tester {

    public static void main(String[] args) {

//        try {
//            GenerationConnector.requestGeneration();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        //System.out.println(Arrays.toString(ImageUtils.rgbFromHSL(0.0, )));

//        UIDefaults defaults = UIManager.getDefaults();
//        Enumeration<Object> keysEnumeration = defaults.keys();
//        ArrayList<Object> keysList = Collections.list(keysEnumeration);
//        for (Object key : keysList)
//        {
//            if(key.toString().endsWith("font"))
//                System.out.println(key);
//        }

//        BitMapImage cans = new BitMapImage(new int[1][1][3]);
//        try {
//            cans = ImageRW.readImage("benchmarkingImages/cans1.png");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        int[][][] rgb = cans.getRgb();
//        int[][][] out = new int[cans.getHeight()][cans.getWidth()][3];
//        for(int y = 0; y < cans.getHeight(); y++) {
//            for(int x = 0; x < cans.getWidth(); x++) {
//                int saturation = (int) Math.round(255.0 * ImageUtils.saturation(rgb[y][x][0], rgb[y][x][1], rgb[y][x][2]));
//                out[y][x][0] = saturation;
//                out[y][x][1] = saturation;
//                out[y][x][2] = saturation;
//                //System.out.println(saturation);
//            }
//        }
//        BitMapImage sat = new BitMapImage(out);
//        try {
//            ImageRW.writeImage(sat, "png", "sat.png");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


    }

}
