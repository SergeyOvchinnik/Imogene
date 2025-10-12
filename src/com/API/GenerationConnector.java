package com.API;

import com.utils.BitMapImage;
import com.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class GenerationConnector {

    public static BitMapImage requestGeneration(String type, int height, int width) throws IOException, InterruptedException {

        URL url = new URL("http://localhost:8080/getArray?type=" + type + "&height=" + height +"&width=" + width);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream input = conn.getInputStream();
        Scanner scanner = new Scanner(input).useDelimiter("\\A");
        String response = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        System.out.println("Received array JSON length: " + response.length());

        int[][][] rgb = Util.parse3DArray(response);

        return new BitMapImage(rgb);
    }

}
