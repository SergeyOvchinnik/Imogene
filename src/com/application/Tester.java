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
            image = ImageRW.readImage("paint.png");
            image = ImageUtils.paintCan(image, 482, 287, 255, 0, 0);
            ImageRW.writeImage(image, "png", "paint2.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
