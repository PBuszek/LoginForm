package org.example.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.dao.DatabaseConnection;
import org.example.dao.UserDAO;
import org.example.models.User;

import java.io.IOException;
import java.util.ArrayList;

public class LogoutHandler implements HttpHandler {

    UserDAO userDao;
    ArrayList<User> userList;

    public LogoutHandler(DatabaseConnection databaseConnection) throws ClassNotFoundException {
        this.userDao = new UserDAO(databaseConnection);
        this.userList = userDao.getUserList();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestURI = exchange.getRequestURI().toString();
        System.out.println(requestURI);

        String cookieValue = exchange.getRequestHeaders().getFirst("Cookie")
                .replace("\"", "")
                .replace("sessionId=", "");
        System.out.println(cookieValue);
        int sessionId = Integer.parseInt(cookieValue);
        System.out.println(sessionId);
        userDao.getSessionUserMap().remove(sessionId);
        String cookie = exchange.getRequestHeaders().getFirst("Cookie") + ";Max-age=0";
        exchange.getResponseHeaders().set("Set-Cookie", cookie);
        System.out.println("removed cookie");

        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.add("Location", "login");
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }
}

