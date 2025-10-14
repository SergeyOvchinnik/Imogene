package com.API;

import com.application.panels.ConnectionScreen;
import com.utils.BitMapImage;
import com.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FilterConnector {

    public static final String FILTER_GRAYSCALE = "grayscale";
    public static final String FILTER_INVERT = "invert";

    public static BitMapImage requestGeneration(String type, int height, int width) throws IOException, InterruptedException {

        String remote = ConnectionScreen.getInstance().getRemote();
        URL url = new URL(remote + "/filter?type=" + type);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        InputStream input = conn.getInputStream();
        Scanner scanner = new Scanner(input).useDelimiter("\\A");
        String response = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        System.out.println("Received array JSON length: " + response.length());

        int[][][] rgb = Util.parse3DArray(response);

        return new BitMapImage(rgb);
    }

}
