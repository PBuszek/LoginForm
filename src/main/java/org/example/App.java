package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.dao.DatabaseConnection;
import org.example.handler.LoginHandler;
import org.example.handler.LogoutHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        DatabaseConnection connection = new DatabaseConnection();
        connection.getDatabaseConnection();

        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/login", new LoginHandler(connection));
        server.createContext("/logout", new LogoutHandler(connection));
        server.setExecutor(null);
        server.start();
        System.out.println("Server has started on port 8001");
    }
}