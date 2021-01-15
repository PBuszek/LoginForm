package org.example.helpers;

import com.sun.net.httpserver.HttpExchange;
import org.example.dao.DatabaseConnection;
import org.example.dao.UserDAO;
import org.example.models.User;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class LoginHelper {
    public UserDAO userDAO;
    DatabaseConnection databaseConnection;

    public LoginHelper() throws ClassNotFoundException {
        this.databaseConnection = new DatabaseConnection();
        this.userDAO = new UserDAO(databaseConnection);
    }

    public void sendAlert(HttpExchange httpExchange) throws IOException {
        String response = "Invalid login data";
        sendResponse(response, httpExchange, 200);
    }

    public void sendResponse(String response, HttpExchange exchange, int status) throws IOException {
        exchange.sendResponseHeaders(status, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public boolean credentialsValid(Map<String, String> inputs) {
        String providedName = inputs.get("username");
        String providedPassword = inputs.get("password");
        User user = userDAO.createUser(providedName, providedPassword);
        System.out.println(user);
        return (user != null) && user.getPassword().equals(providedPassword);
    }
}
