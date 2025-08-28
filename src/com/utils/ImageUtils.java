package com.utils;

import java.util.ArrayList;

public class ImageUtils {

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
