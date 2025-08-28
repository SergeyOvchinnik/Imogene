package com.GA.generation;

import com.GA.IndividualImage;

public class FireMapGeneration extends GenerationFunction {

    int rStart;
    int gStart;
    int bStart;
    int rEnd;
    int gEnd;
    int bEnd;

    public FireMapGeneration(int rStart, int gStart, int bStart, int rEnd, int gEnd, int bEnd) {
        this.rStart = rStart;
        this.gStart = gStart;
        this.bStart = bStart;
        this.rEnd = rEnd;
        this.gEnd = gEnd;
        this.bEnd = bEnd;
    }

    public IndividualImage generate(int height, int width) {
        int[][][] rgb;
        return null; // TODO:fix
    }
}
