package serverHandler;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class MainFunction {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLocalHost(), 7070), 0);

        server.createContext("/", new indexHandler());
        server.createContext("/index", new indexHandler()); // Now the handler is attached to root
        server.createContext("/Page1", new page1Handler()); // Whenever in the browser it says name.com/Page1 -> it calls that handler 
        server.createContext("/Page2", new page2Handler());
        server.createContext("/Gallery", new GalleryHandler2());
        server.setExecutor(null); // The default executor
        server.start();
        System.out.println("The server was startet");
        System.out.println("IpV4-adress: " + InetAddress.getLocalHost());
        System.out.println("Port: " + 7070);
        // server.stop(10); // The arguments means delay in seconds

    }

    static class indexHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String response = "<!DOCTYPE html>\n"
                    + "<head>\n"
                    + "	<title>\n"
                    + "		Page1\n"
                    + "	</title>\n"
                    + "	<meta charset='UTF-8'>\n"
                    + "	\n"
                    + "	</head>\n"
                    + "<body>\n"
                    + "<h1>Index</h1>\n"
                    + "<p>Click <a href='/Page1'>here</a> to get to Page1</p>\n"
                    + "<p>Click <a href='/Page2'>here</a> to get to Page2</p>\n"
                    + "<p>Click <a href='/Gallery'>here</a> to get to the Gallery</p>\n"
                    + "</body>\n"
                    + "\n"
                    + "<html>";

            Headers h = he.getResponseHeaders(); // Then the browser knows what the content is
            h.set("Content-Type", "text/html");  // This info is send in the HTTP-rsponse-header

            he.sendResponseHeaders(200, response.length());
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }

        }
    }

    static class page1Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String response = "<!DOCTYPE html>\n"
                    + "<head>\n"
                    + "	<title>\n"
                    + "		Page1\n"
                    + "	</title>\n"
                    + "	<meta charset='UTF-8'>\n"
                    + "	\n"
                    + "	</head>\n"
                    + "<body>\n"
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

    static class galleryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String root = "HTML";
            URI uri = he.getRequestURI();
            System.out.println(root + uri.getPath());
            File file = new File(root + uri.getPath()).getCanonicalFile();

            if (!file.getPath().startsWith(root)) {
                // Suspected path traversal attack:send back a 403-error
                String response = "403 (Forbidden)\n";
                he.sendResponseHeaders(403, response.length());
                try (OutputStream os = he.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else if (!file.isFile()) { // The does not exists
                String response = "404 (Not found)\n";
                he.sendResponseHeaders(404, response.length());
                try (OutputStream os = he.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else { // Object exists and is a file: we respond with a 200 message
                he.sendResponseHeaders(200, 0);
                OutputStream os = he.getResponseBody();
                FileInputStream fs = new FileInputStream(file);
                byte[] buffer = new byte[0x10000];
                int count = 0;
                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }

            }

        }

    }

    static class GalleryHandler2 implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String path = "HTML/";
            
            File file = new File(path + "Gallery.html");
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        }

    }

}
