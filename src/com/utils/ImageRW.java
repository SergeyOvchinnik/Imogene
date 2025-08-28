package com.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageRW {

    public static BitMapImage readImage(String path) throws IOException {
        BufferedImage img = ImageIO.read(new File(path));
        int height = img.getHeight();
        int width = img.getWidth();
        int[][][] pixels = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                pixels[y][x][0] = (rgb >> 16) & 0xFF; // Red
                pixels[y][x][1] = (rgb >> 8) & 0xFF;  // Green
                pixels[y][x][2] = rgb & 0xFF;         // Blue
            }
        }

        return new BitMapImage(pixels);
    }

    public static void writeImage(BitMapImage image, String format, String outputPath) throws IOException {
        int[][][] pixels = image.getRgb();

        int height = pixels.length;
        int width = pixels[0].length;

        // Create a BufferedImage
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Fill in the pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = pixels[y][x][0];
                int g = pixels[y][x][1];
                int b = pixels[y][x][2];
                int rgb = (r << 16) | (g << 8) | b;
                img.setRGB(x, y, rgb);
            }
        }

        // Write to file (format can be "png", "bmp", or "jpg")
        ImageIO.write(img, format, new File(outputPath));
    }

}
