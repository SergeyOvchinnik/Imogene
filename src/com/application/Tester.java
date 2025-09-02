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
            image = ImageRW.readImage("benchmarkingImages/cans1.png");
            image = ImageUtils.resize(image, 100, 133);
            ImageRW.writeImage(image, "png", "cansSmall.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
