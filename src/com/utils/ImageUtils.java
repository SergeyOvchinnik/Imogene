package com.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageUtils {

    static double centerWeight = 0.2;
    static double edgeWeight = 0.1;

    public static BitMapImage smoothFilter(BitMapImage image) {
        int[][][] rgb = image.getRgb();
        int[][][] adjusted = new int[image.getHeight()][image.getWidth()][3];
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                for(int c = 0; c < 3; c++) {
                    double weightSum = 0.0;
                    double weightedColourSum = 0.0;
                    for(int i = Math.max(0, y - 1); i <= Math.min(image.getHeight() - 1, y + 1); i++) {
                        for(int j = Math.max(0, x - 1); j <= Math.min(image.getWidth() - 1, x + 1); j++) {
                            if(y == i && x == j) {
                                weightSum += centerWeight;
                                weightedColourSum += centerWeight * rgb[y][x][c];
                            }
                            else {
                                weightSum += edgeWeight;
                                weightedColourSum += edgeWeight * rgb[i][j][c];
                            }
                        }
                    }
                    int newValue = (int) Math.round(weightedColourSum / weightSum);
                    adjusted[y][x][c] = newValue;
                }
            }
        }
        return new BitMapImage(adjusted);
    }


//    public static BitMapImage adjustBrightness(BitMapImage image, double brightness) {
//        int[][][] rgb = image.getRgb();
//        int[][][] adjusted = new int[image.getHeight()][image.getWidth()][3];
//
//        for (int y = 0; y < image.getHeight(); y++) {
//            for (int x = 0; x < image.getWidth(); x++) {
//                for (int c = 0; c < 3; c++) {
//                    int adjustedValue = (int) Math.round(rgb[y][x][c] * (1.0 - brightness) + 255 * brightness);
//                    if (adjustedValue < 0) adjustedValue = 0;
//                    if (adjustedValue > 255) adjustedValue = 255;
//
//                    adjusted[y][x][c] = adjustedValue;
//                }
//            }
//        }
//
//        return new BitMapImage(adjusted);
//    }

    // Assumes the input image is grayscale
    public static BitMapImage spectrumMaping(BitMapImage image, int[][] colours) {
        int[][][] rgb = image.getRgb();
        int[][][] adjusted = new int[image.getHeight()][image.getWidth()][3];
        int[] thresholds = new int[colours.length];

        int lowestValue = 255;
        int highestValue = 0;
        for(int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int value = rgb[y][x][0]; // Assumes green and blue are same values as red
                if(value < lowestValue) {
                    lowestValue = value;
                }
                if(value > highestValue) {
                    highestValue = value;
                }
            }
        }

        for(int t = 0; t < thresholds.length; t++) {
            thresholds[t] = lowestValue + t * (highestValue - lowestValue) / (colours.length - 1);
        }

        System.out.println("thresholds: " + Arrays.toString(thresholds));

        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                int value = rgb[y][x][0]; // Assumes green and blue are same values as red
                for(int t = 0; t < thresholds.length; t++) {
                    if(value > thresholds[t])
                        continue;
                    if(value == thresholds[t]) {
                        adjusted[y][x][0] = colours[t][0];
                        adjusted[y][x][1] = colours[t][1];
                        adjusted[y][x][2] = colours[t][2];
                        break;
                    }
                    int t1 = thresholds[t - 1];
                    int t2 = thresholds[t];
                    int[] colour1 = colours[t - 1];
                    int[] colour2 = colours[t];
                    double distance1 = Math.abs(t1 - value);
                    double distance2 = Math.abs(t2 - value);
                    double ratio1 = distance2 / (distance1 + distance2);
                    double ratio2 = distance1 / (distance1 + distance2);
                    //System.out.println(ratio1 + " " + ratio2);
                    for(int c = 0; c < 3; c++) {
                        adjusted[y][x][c] = (int) Math.round((0.0 + colour1[c]) * ratio1 + (0.0 + colour2[c]) * ratio2);
                    }
                    break;
                }
            }
        }
        return new BitMapImage(adjusted);
    }

    public static BitMapImage rgbBalancing(BitMapImage image, double rWeight, double gWeight, double bWeight) {
        int[][][] rgb = image.getRgb();
        int[][][] adjusted = new int[image.getHeight()][image.getWidth()][3];
        double[] rgbWeights = new double[] {rWeight, gWeight, bWeight};
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                for(int c = 0; c < 3; c++) {
                    adjusted[y][x][c] = (int) Math.round(rgb[y][x][c] * rgbWeights[c]);
                }
            }
        }
        return new BitMapImage(adjusted);
    }

    public static BitMapImage weightedGrayscale(BitMapImage image, double rWeight, double gWeight, double bWeight) {
        int[][][] rgb = image.getRgb();
        int[][][] gray = new int[image.getHeight()][image.getWidth()][3];
        double[] rgbWeights = new double[] {rWeight, gWeight, bWeight};
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                double total = 0;
                for(int c = 0; c < 3; c++) {
                    total += rgb[y][x][c] * rgbWeights[c];
                }
                double average = total / (rWeight + gWeight + bWeight);
                for(int c = 0; c < 3; c++) {
                    gray[y][x][c] = (int) Math.round(average);
                }
            }
        }
        return new BitMapImage(gray);
    }

    public static BitMapImage grayscale(BitMapImage image) {
        int[][][] rgb = image.getRgb();
        int[][][] gray = new int[image.getHeight()][image.getWidth()][3];
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                int total = 0;
                for(int c = 0; c < 3; c++) {
                    total += rgb[y][x][c];
                }
                int average = total / 3;
                for(int c = 0; c < 3; c++) {
                    gray[y][x][c] = average;
                }
            }
        }
        return new BitMapImage(gray);
    }

    /**
     * TODO: Untested AI-generated implementation
     */
    public static BitMapImage resize(BitMapImage image, int newHeight, int newWidth) {
        int oldHeight = image.getHeight();
        int oldWidth = image.getWidth();
        int[][][] rgb = image.getRgb();

        int[][][] output = new int[newHeight][newWidth][3];

        double scaleY = (double) oldHeight / newHeight;
        double scaleX = (double) oldWidth / newWidth;

        for (int y = 0; y < newHeight; y++) {
            double srcY = y * scaleY;
            int y0 = (int) Math.floor(srcY);
            int y1 = Math.min(y0 + 1, oldHeight - 1);
            double wy = srcY - y0;

            for (int x = 0; x < newWidth; x++) {
                double srcX = x * scaleX;
                int x0 = (int) Math.floor(srcX);
                int x1 = Math.min(x0 + 1, oldWidth - 1);
                double wx = srcX - x0;

                // For each channel: R, G, B
                for (int c = 0; c < 3; c++) {
                    int topLeft     = rgb[y0][x0][c];
                    int topRight    = rgb[y0][x1][c];
                    int bottomLeft  = rgb[y1][x0][c];
                    int bottomRight = rgb[y1][x1][c];

                    // Bilinear interpolation
                    double top    = topLeft * (1 - wx) + topRight * wx;
                    double bottom = bottomLeft * (1 - wx) + bottomRight * wx;
                    double value  = top * (1 - wy) + bottom * wy;

                    output[y][x][c] = (int) Math.round(value);
                }
            }
        }

        return new BitMapImage(output);
    }


    public static BitMapImage blendImages(BitMapImage image1, BitMapImage image2) {
        int[][][] rgb1 = image1.getRgb();
        int[][][] rgb2 = image2.getRgb();
        int[][][] rgb = new int[image1.getRgb().length][image1.getRgb()[0].length][3];

        for(int y = 0; y < rgb.length; y++) {
            for(int x = 0; x < rgb[0].length; x++) {
                rgb[y][x][0] = (rgb1[y][x][0] + rgb2[y][x][0]) / 2;
                rgb[y][x][1] = (rgb1[y][x][1] + rgb2[y][x][1]) / 2;
                rgb[y][x][2] = (rgb1[y][x][2] + rgb2[y][x][2]) / 2;
            }
        }

        return new BitMapImage(rgb);
    }

    public static BitMapImage blendImagesWeighted(BitMapImage image1, BitMapImage image2, double weight1, double weight2) {
        int[][][] rgb1 = image1.getRgb();
        int[][][] rgb2 = image2.getRgb();
        int[][][] rgb = new int[image1.getRgb().length][image1.getRgb()[0].length][3];

        double proportion1 = weight1 / (weight1 + weight2);
        double proportion2 = weight2 / (weight1 + weight2);

        for(int y = 0; y < rgb.length; y++) {
            for(int x = 0; x < rgb[0].length; x++) {
                Double r = ((0.0 + rgb1[y][x][0]) * proportion1) + ((0.0 + rgb2[y][x][0])  * proportion2);
                Double g = ((0.0 + rgb1[y][x][1]) * proportion1) + ((0.0 + rgb2[y][x][1])  * proportion2);
                Double b = ((0.0 + rgb1[y][x][2]) * proportion1) + ((0.0 + rgb2[y][x][2])  * proportion2);
                rgb[y][x][0] = r.intValue();
                rgb[y][x][1] = g.intValue();
                rgb[y][x][2] = b.intValue();
            }
        }

        return new BitMapImage(rgb);
    }

    public static BitMapImage rotateCW(BitMapImage image) {
        int[][][] rgb = new int[image.getWidth()][image.getHeight()][3];
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                rgb[x][image.getHeight() - y - 1][0] = image.getRgb()[y][x][0];
                rgb[x][image.getHeight() - y - 1][1] = image.getRgb()[y][x][1];
                rgb[x][image.getHeight() - y - 1][2] = image.getRgb()[y][x][2];
            }
        }
        return new BitMapImage(rgb);
    }

    public static BitMapImage rotateCCW(BitMapImage image) {
        int[][][] rgb = new int[image.getWidth()][image.getHeight()][3];
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                rgb[x][y][0] = image.getRgb()[y][x][0];
                rgb[x][y][1] = image.getRgb()[y][x][1];
                rgb[x][y][2] = image.getRgb()[y][x][2];
            }
        }
        return new BitMapImage(rgb);
    }

    public static BitMapImage paintCan(BitMapImage image, int x, int y, int r, int  g, int b) {
        int[][][] rgb = image.getRgb();
        int height = image.getHeight();
        int width = image.getWidth();
        boolean[][] searched = new boolean[image.getHeight()][image.getWidth()];
        searched[y][x] = true;
        ArrayList<Integer> xSet = new ArrayList<Integer>();
        ArrayList<Integer> ySet = new ArrayList<Integer>();
        // Add the coordinate of the first pixel
        xSet.add(x);
        ySet.add(y);
        // Repeatedly check pixels until no more pixels are searchable

        for(int nextSearchIndex = 0; nextSearchIndex < xSet.size(); nextSearchIndex++) {
            // Extract the coordinates of the next searchable pixel
            int xSearch = xSet.get(nextSearchIndex);
            int ySearch = ySet.get(nextSearchIndex);
            //System.out.println("Searching coordinate " + xSearch + ", " + ySearch);
            // Search the pixel to the left of the current pixel
            if (xSearch > 0) {
                // Skip it if it has already been searched
                if (!searched[ySearch][xSearch - 1])
                    // Skip  it if it's the wrong colour
                    if (rgb[ySearch][xSearch - 1][0] == rgb[y][x][0])
                        if (rgb[ySearch][xSearch - 1][1] == rgb[y][x][1])
                            if (rgb[ySearch][xSearch - 1][2] == rgb[y][x][2]) {
                                // If it's an unexplored pixel of the right colour, add it to the search set
                                xSet.add(xSearch - 1);
                                ySet.add(ySearch);
                                searched[ySearch][xSearch - 1] = true;
                            }
            }
            // Search the pixel to the right of the current pixel
            if (xSearch < width - 1) {
                // Skip it if it has already been searched
                if (!searched[ySearch][xSearch + 1])
                    // Skip  it if it's the wrong colour
                    if (rgb[ySearch][xSearch + 1][0] == rgb[y][x][0])
                        if (rgb[ySearch][xSearch + 1][1] == rgb[y][x][1])
                            if (rgb[ySearch][xSearch + 1][2] == rgb[y][x][2]) {
                                // If it's an unexplored pixel of the right colour, add it to the search set
                                xSet.add(xSearch + 1);
                                ySet.add(ySearch);
                                searched[ySearch][xSearch + 1] = true;
                            }
            }
            // Search the pixel above the current pixel
            if (ySearch > 0) {
                // Skip it if it has already been searched
                if (!searched[ySearch - 1][xSearch])
                    // Skip  it if it's the wrong colour
                    if (rgb[ySearch - 1][xSearch][0] == rgb[y][x][0])
                        if (rgb[ySearch - 1][xSearch][1] == rgb[y][x][1])
                            if (rgb[ySearch - 1][xSearch][2] == rgb[y][x][2]) {
                                // If it's an unexplored pixel of the right colour, add it to the search set
                                xSet.add(xSearch);
                                ySet.add(ySearch - 1);
                                searched[ySearch - 1][xSearch] = true;
                            }
            }
            // Search the pixel below the current pixel
            if (ySearch < height - 1) {
                // Skip it if it has already been searched
                if (!searched[ySearch + 1][xSearch])
                    // Skip  it if it's the wrong colour
                    if (rgb[ySearch + 1][xSearch][0] == rgb[y][x][0])
                        if (rgb[ySearch + 1][xSearch][1] == rgb[y][x][1])
                            if (rgb[ySearch + 1][xSearch][2] == rgb[y][x][2]) {
                                // If it's an unexplored pixel of the right colour, add it to the search set
                                xSet.add(xSearch);
                                ySet.add(ySearch + 1);
                                searched[ySearch + 1][xSearch] = true;
                            }
            }
        }

        int[][][] rgbOut = new int[height][width][3];
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++) {
                if(searched[i][j]) {
                    rgbOut[i][j][0] = r;
                    rgbOut[i][j][1] = g;
                    rgbOut[i][j][2] = b;
                }
                else {
                    rgbOut[i][j][0] = rgb[i][j][0];
                    rgbOut[i][j][1] = rgb[i][j][1];
                    rgbOut[i][j][2] = rgb[i][j][2];
                }
            }

        return new BitMapImage(rgbOut);
    }

    /**
     * Finds the coordinates of all pixels within the largest single-coloured area of the image
     * @return a 2d boolean array, true values correspond to the selected area
     *
     * TODO: does not work because of incorrect logic with continue; statements
     */
    public static boolean[][] largestColourArea(BitMapImage image) {
        // Extract the image pixels and its dimensions
        int height = image.getHeight();
        int width = image.getWidth();
        int[][][] rgb = new int[height][width][3];

        // Initialise search variables
        boolean[][] searched = new boolean[height][width];
        int largestArea = 0;
        boolean[][] bestArea = new boolean[height][width];

        // Iterate through every pixel on the  image
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                // Skip pixels that were already searched
                if(searched[y][x]) continue;
                // Extract the colour of the current pixel
                int r = rgb[y][x][0];
                int g = rgb[y][x][1];
                int b = rgb[y][x][2];
                // Create a set of all coordinates that belong to the area
                ArrayList<Integer> xSet = new ArrayList<Integer>();
                ArrayList<Integer> ySet = new ArrayList<Integer>();
                // Add the coordinate of the first pixel
                xSet.add(x);
                ySet.add(y);
                // Repeatedly check pixels until no more pixels are searchable
                int nextSearchIndex = 0;
                while(nextSearchIndex < xSet.size()) {
                    // Extract the coordinates of the next searchable pixel
                    int xSearch = xSet.get(nextSearchIndex);
                    int ySearch = ySet.get(nextSearchIndex);
                    // Search the pixel to the left of the current pixel
                    if(xSearch > 0) {
                        // Skip it if it has already been searched
                        if(searched[ySearch][xSearch - 1]) continue; // TODO: wrong use of continue in this method
                        // Skip  it if it's the wrong colour
                        if(rgb[ySearch][xSearch - 1][0] != r) continue;
                        if(rgb[ySearch][xSearch - 1][1] != g) continue;
                        if(rgb[ySearch][xSearch - 1][2] != b) continue;
                        // If it's an unexplored pixel of the right colour, add it to the search set
                        xSet.add(xSearch - 1);
                        ySet.add(ySearch);
                    }
                    // Search the pixel to the right of the current pixel
                    if(xSearch < width - 1) {
                        // Skip it if it has already been searched
                        if(searched[ySearch][xSearch + 1]) continue;
                        // Skip  it if it's the wrong colour
                        if(rgb[ySearch][xSearch + 1][0] != r) continue;
                        if(rgb[ySearch][xSearch + 1][1] != g) continue;
                        if(rgb[ySearch][xSearch + 1][2] != b) continue;
                        // If it's an unexplored pixel of the right colour, add it to the search set
                        xSet.add(xSearch + 1);
                        ySet.add(ySearch);
                    }
                    // Search the pixel above the current pixel
                    if(ySearch > 0) {
                        // Skip it if it has already been searched
                        if(searched[ySearch - 1][xSearch]) continue;
                        // Skip  it if it's the wrong colour
                        if(rgb[ySearch - 1][xSearch][0] != r) continue;
                        if(rgb[ySearch - 1][xSearch][1] != g) continue;
                        if(rgb[ySearch - 1][xSearch][2] != b) continue;
                        // If it's an unexplored pixel of the right colour, add it to the search set
                        xSet.add(xSearch);
                        ySet.add(ySearch - 1);
                    }
                    // Search the pixel below the current pixel
                    if(ySearch < height - 1) {
                        // Skip it if it has already been searched
                        if(searched[ySearch + 1][xSearch]) continue;
                        // Skip  it if it's the wrong colour
                        if(rgb[ySearch + 1][xSearch][0] != r) continue;
                        if(rgb[ySearch + 1][xSearch][1] != g) continue;
                        if(rgb[ySearch + 1][xSearch][2] != b) continue;
                        // If it's an unexplored pixel of the right colour, add it to the search set
                        xSet.add(xSearch);
                        ySet.add(ySearch + 1);
                    }
                    // Flag the current pixel as already searched
                    searched[ySearch][xSearch] = true;
                    // Advance to the next pixel in the search set
                    nextSearchIndex++;
                }
                int size = xSet.size();
                if(size > largestArea) {
                    largestArea = size;
                    bestArea = new boolean[height][width];
                    for(int i = 0; i < size; i++) {
                        bestArea[ySet.get(i)][xSet.get(i)] = true;
                    }
                }
            }
        }
        return bestArea;
    }


}
