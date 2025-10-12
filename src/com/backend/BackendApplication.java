package com.backend;

import com.GA.ImageGenerator;
import com.application.panels.ImageScreen;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.utils.BitMapImage;
import com.utils.ImageUtils;
import com.utils.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

public class BackendApplication {

    public static void main(String[] args) throws IOException {
        int portNumber = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);

        server.createContext("/", new RootHandler());
        server.createContext("/generate", new GenerationHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:" + portNumber);
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <title>Imogene API</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\t<p>\n" +
                    "\t\tWelcome to Imogene API.\n" +
                    "\t</p>\n" +
                    "</body>\n" +
                    "</html>";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class GenerationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if (!"GET".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1); // Method not allowed
                    return;
                }

                // Parse query parameters
                Map<String, String> params = Util.queryToMap(exchange.getRequestURI().getQuery());
                int height = Integer.parseInt(params.getOrDefault("height", "0"));
                int width = Integer.parseInt(params.getOrDefault("width", "0"));
                String type = params.getOrDefault("type", "");

                BitMapImage image = new BitMapImage(width, height);

                if(type.equalsIgnoreCase("randomBitmap"))
                    image = ImageGenerator.randomPixels(height, width);

                if(type.equalsIgnoreCase("randomBitmap"))
                    image = ImageGenerator.randomPixels(height, width);


                String json = Util.arrayToJson(image.getRgb());
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(json.getBytes());
                os.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
