package org.example.helpers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;

public class Static implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String path = httpExchange.getRequestURI().getPath();
        System.out.println(path);
        URL fileURL = getClass().getClassLoader().getResource(
                path.equals("/login")?"/index.twig":"."+path
        );
        System.out.println(fileURL);
        sendFile(httpExchange, fileURL);

    }

    private void sendFile(HttpExchange httpExchange, URL fileURL) throws IOException {

        File file = new File(fileURL.getFile());
        String mimeType = Files.probeContentType(file.toPath());

        httpExchange.getResponseHeaders().set("Content-type", mimeType);
        httpExchange.sendResponseHeaders(200, 0);
        OutputStream os = httpExchange.getResponseBody();
        FileInputStream fs = new FileInputStream(file);

        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while((count = fs.read(buffer)) >= 0) {
            os.write(buffer,0,count);
        }
        os.close();
    }

}
