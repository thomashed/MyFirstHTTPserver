package serverHandler;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class MainFunction {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 80), 0);
        server.createContext("/", new indexHandler()); // Now the handler is attached to root
        server.start();
        
    }

    static class indexHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String response = "Hello World!";
            he.sendResponseHeaders(200, response.length());
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

    }

}
