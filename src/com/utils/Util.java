package com.utils;

import java.util.HashMap;
import java.util.Map;
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

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }
        }
        return result;
    }

    public static String arrayToJson(int[][][] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int y = 0; y < array.length; y++) {
            sb.append("[");
            for (int x = 0; x < array[y].length; x++) {
                sb.append("[")
                        .append(array[y][x][0]).append(",")
                        .append(array[y][x][1]).append(",")
                        .append(array[y][x][2]).append("]");
                if (x < array[y].length - 1) sb.append(",");
            }
            sb.append("]");
            if (y < array.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public static int[][][] parse3DArray(String json) {
        // Remove outer brackets
        json = json.trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        }

        // Split into rows
        String[] rows = json.split("\\],\\["); // splits on "],["
        int height = rows.length;
        int width = rows[0].split("\\],\\[|\\],\\[").length; // approximate width
        int[][][] array = new int[height][][];

        for (int y = 0; y < height; y++) {
            String row = rows[y].replaceAll("\\[|\\]", ""); // remove brackets
            String[] pixels = row.split("\\],\\[|\\],|,\\[|\\],"); // split pixels
            int pixelCount = pixels.length / 3;
            array[y] = new int[pixelCount][3];
            for (int x = 0; x < pixelCount; x++) {
                array[y][x][0] = Integer.parseInt(pixels[x * 3].trim());
                array[y][x][1] = Integer.parseInt(pixels[x * 3 + 1].trim());
                array[y][x][2] = Integer.parseInt(pixels[x * 3 + 2].trim());
            }
        }

        return array;
    }

}
