package serverHandler;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class MainFunction {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 80), 7); 
        server.createContext("/", new indexHandler()); // Now the handler is attached to root
        server.createContext("/Page1", new page1Handler()); // Whenever in the browser it says name.com/Page1 -> it calls that handler 
        server.createContext("/Page2", new page2Handler());
        server.setExecutor(null); // The default executor
        server.start();
        System.out.println("The server was startet");
        System.out.println("IpV4-adress: " + InetAddress.getLocalHost());
        // server.stop(10); // The arguments means delay in seconds
        
    }

    static class indexHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            
            URI uri = he.getRequestURI(); 
            String response = "Path: " + uri.getPath();
            
            he.sendResponseHeaders(200, response.length());
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class page1Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            URI uri = he.getRequestURI();
            
            String response = "<!DOCTYPE html>\n"
                    + "<head>\n"
                    + "	<title>\n"
                    + "		Page1\n"
                    + "	</title>\n"
                    + "	<meta charset='UTF-8'>\n"
                    + "	\n"
                    + "	</head>\n"
                    + "<body>\n"
                    + "<p>" + uri.getPath() + "</p>\n"
                    + "<h1>This is my heading on Page1</h1>\n"
                    + "<p>Click <a href='localhost/'>here</a> to get to the front page!</p>\n"
                    + "</body>\n"
                    + "\n"
                    + "<html>";

            he.sendResponseHeaders(200, response.length()); // First we send the response header
            try (OutputStream os = he.getResponseBody()) {  // Then we send the response body
                os.write(response.getBytes());
            }

        }

    }

    static class page2Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String response = "<!DOCTYPE html>\n"
                    + "<head>\n"
                    + "	<title>\n"
                    + "		Page2\n"
                    + "	</title>\n"
                    + "	<meta charset='UTF-8'>\n"
                    + "	\n"
                    + "	</head>\n"
                    + "<body>\n"
                    + "<h1>This is my heading on page2</h1>\n"
                    + "<p>Click <a href='localhost/'>here</a> to get to the front page!</p>\n"
                    + "<p>Click <a href='Page1'>here</a> to get to page 1!</p>\n"
                    + "</body>\n"
                    + "\n"
                    + "<html>";

            he.sendResponseHeaders(200, response.length());
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }

        }

    }

}
