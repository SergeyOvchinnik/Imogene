package com.utils;

import java.util.Random;

public class Util {

    public static Random rng = new Random();

    public static int[] randomIndexes(int n, int size) {
        int[] indexes = new int[n];
        for(int i = 0; i < n; i++) {
           indexes[i] = rng.nextInt(size);
           boolean alreadyUsed = false;
           for(int j = 0; j < i; j++) {
               if(indexes[i] == indexes[j]) {
                   i--;
                   alreadyUsed = true;
                   break;
               }
           }
           if(alreadyUsed) {
               i--;
               continue;
           }
        }
        return indexes;
    }


}
