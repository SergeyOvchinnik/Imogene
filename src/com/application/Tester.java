package com.application;

import com.utils.BitMapImage;
import com.utils.ImageRW;
import com.utils.ImageUtils;

import java.io.IOException;
import java.util.Arrays;

public class Tester {

    public static void main(String[] args) {

        BitMapImage image = null;
        try {
            image = ImageRW.readImage("testy1.png");
            image = ImageUtils.smoothFilter(image);
            ImageRW.writeImage(image, "png", "testy1Smooth.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
