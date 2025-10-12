package com.backend;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class BackendApplication {

    public static void main(String[] args) throws IOException {
        int portNumber = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);

        // Set up a context (a route) and its handler
        server.createContext("/", new RootHandler());

        // Start the server
        server.setExecutor(null); // creates a default executor
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

//    static class DieHandler implements HttpHandler {
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//            String response = "Shutting down...";
//            exchange.sendResponseHeaders(200, response.getBytes().length);
//            System.out.println("Shutting down...");
//            System.exit(0);
//        }
//    }

}
