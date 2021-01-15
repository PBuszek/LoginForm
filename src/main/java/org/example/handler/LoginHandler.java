package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.dao.DatabaseConnection;
import org.example.dao.UserDAO;
import org.example.helpers.CookieHelper;
import org.example.helpers.DataFormParser;
import org.example.helpers.LoginHelper;
import org.example.models.User;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class LoginHandler implements HttpHandler {

    public UserDAO userDAO;
    private static final String SESSION_COOKIE_NAME = "CookieName";
    private final LoginHelper loginHelper;
    DatabaseConnection databaseConnection;
    private final CookieHelper cookieHelper;
    public static int counter = 0;
    String response = "";
    ArrayList<User> userList;

    public LoginHandler(DatabaseConnection databaseConnection) throws ClassNotFoundException {
        this.databaseConnection = null;
        this.userDAO = new UserDAO(databaseConnection);
        this.cookieHelper = new CookieHelper();
        this.loginHelper = new LoginHelper();
        this.userList = userDAO.getUserList();

    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();

            if (method.equals("GET")) {
                Optional<HttpCookie> optionalCookie = CookieHelper
                        .getSessionIdCookie(httpExchange, SESSION_COOKIE_NAME);
                if (optionalCookie.isPresent()) {
                    int sessionId = cookieHelper.getSessionIdFromCookie
                            (optionalCookie.get());

                    getPageWithSession(httpExchange, sessionId);
                } else {
                    getForm(httpExchange);
                }
            }
            if (method.equals("POST")) {
                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();
                System.out.println(formData);
                Map<String, String> inputs = DataFormParser.parseFormData(formData);
                if (!loginHelper.credentialsValid(inputs)) {
                    loginHelper.sendAlert(httpExchange);
                    return;
                }
                String sessionId = String.valueOf(counter);
                cookieHelper.createCookie(httpExchange, SESSION_COOKIE_NAME, sessionId);
                System.out.println(inputs.get("username"));
                User user = userDAO.getUserByProvidedName(inputs.get("username"), userList);
                System.out.println(counter);
                System.out.println(user.toString());
                userDAO.getSessionUserMap().put(counter, user);
                getHomePage(httpExchange, user);
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getForm(HttpExchange httpExchange) throws IOException {
        String response;
        JtwigTemplate template = JtwigTemplate.classpathTemplate
                ("templates/index.twig");
        JtwigModel model = JtwigModel.newModel();
        response = template.render(model);
        loginHelper.sendResponse(response, httpExchange, 200);
    }

    public void getHomePage(HttpExchange httpExchange, User user) throws IOException {
        String response;
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/user-page.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("username", user.getUserName());
        response = template.render(model);
        System.out.println(response);
        loginHelper.sendResponse(response, httpExchange, 200);
    }

    private void getPageWithSession(HttpExchange httpExchange, int sessionId) throws IOException {
        System.out.println(sessionId);
        User user = userDAO.getUserBySessionId(sessionId);
        System.out.println(user);
        getHomePage(httpExchange, user);
    }
}