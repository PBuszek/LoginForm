package org.example.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CookieHelper {
    private static final String SESSION_COOKIE_NAME = "CookieName";

    public void createCookie(HttpExchange httpExchange, String name, String value) {
        Optional<HttpCookie> cookie;
        cookie = Optional.of(new HttpCookie(name, value));
        cookie.get().setPath("/login/");
        String cookieString = cookie.get().toString();
        httpExchange.getResponseHeaders().add("Set-Cookie", cookieString);
    }

    public static List<HttpCookie> parseCookies(String cookieString) {
        List<HttpCookie> cookies = new ArrayList<>();
        if (cookieString == null || cookieString.isEmpty()) {
            return cookies;
        }
        for (String cookie : cookieString.split(";")) {
            int indexOfEq = cookie.indexOf('=');
            String cookieName = cookie.substring(0, indexOfEq);
            String cookieValue = cookie.substring(indexOfEq + 1, cookie.length());
            cookies.add(new HttpCookie(cookieName, cookieValue));
        }
        return cookies;
    }

    public static Optional<HttpCookie> findCookieByName(String name, List<HttpCookie> cookies) {
        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals(name))
                return Optional.of(cookie);
        }
        return Optional.empty();
    }

    public static Optional<HttpCookie> getSessionIdCookie(HttpExchange httpExchange, String name) {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        List<HttpCookie> cookies = parseCookies(cookieStr);
        return findCookieByName(name, cookies);
    }

    public static int getSessionIdFromCookie(HttpCookie cookie) {
        String sessionId = "";
        sessionId = cookie.getValue().replace("\"", "");
        System.out.println(sessionId);
        return Integer.parseInt(sessionId);
    }

    public static void deleteCookie(HttpExchange httpExchange) {
        String cookie = httpExchange.getRequestHeaders().getFirst("Cookie") + ";Max-age=0";
        httpExchange.getResponseHeaders().set("Set-Cookie", cookie);
        System.out.println("deleted cookie");
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}