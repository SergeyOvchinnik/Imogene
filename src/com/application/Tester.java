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
import com.utils.Util;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class Tester {

    public static void main(String[] args) {

        long n = 1423l * 1423l * 1423l * 1423l;
        System.out.println("n = " + n);


//        String json = "[[[185,80,31],[164,5,74],[29,176,121],[83,203,224],[254,207,252]],[[65,226,47],[243,42,255],[61,39,176],[28,184,73],[94,146,35]],[[133,37,50],[217,8,145],[102,101,194],[62,149,16],[108,137,204]],[[186,204,137],[154,237,55],[64,58,47],[87,13,247],[151,46,101]],[[204,162,90],[194,74,70],[154,218,190],[71,144,20],[146,202,200]]]";
//
//        System.out.println(Arrays.deepToString(Util.parse3DArray(json)));

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
