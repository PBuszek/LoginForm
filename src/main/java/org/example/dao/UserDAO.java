package org.example.dao;

import org.example.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    private final DatabaseConnection databaseConnection;
    private final ArrayList<User> userList;
    public final Map<Integer, User> sessionUserMap;

    public UserDAO(DatabaseConnection databaseConnection) throws ClassNotFoundException {
        this.databaseConnection = databaseConnection;
        this.userList = new ArrayList<>();
        this.sessionUserMap = new HashMap<>();
    }

    User newUser;

    public ArrayList<User> getAllUsers(ArrayList<User> userList) {
        try {
            Statement st = databaseConnection.getDatabaseConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM login_form.users");
            while (rs.next()) {
                String userName = rs.getString("login");
                String pass = rs.getString("password");

                newUser = new User(userName, pass)
                        .setUserName(userName)
                        .setPassword(pass);
                userList.add(newUser);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public ArrayList<User> getUserList() {
        getAllUsers(userList);
        return this.userList;
    }

    public User createUser(String login, String password) {
        try {
            Statement st = databaseConnection.getDatabaseConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM login_form.users WHERE login = '" + login + "' AND password = '" + password + "';");
            while (rs.next()) {
                String userName = rs.getString("login");
                String pass = rs.getString("password");

                newUser = new User(userName, pass)
                        .setUserName(userName)
                        .setPassword(pass);

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newUser;
    }

    public Map<Integer, User> getSessionUserMap() {
        return this.sessionUserMap;
    }

    public User getUserBySessionId(int sessionId) {
        System.out.println(sessionUserMap);
        return this.sessionUserMap.get(sessionId);
    }

    public User getUserByProvidedName(String providedName, ArrayList<User> userList) {
        for (User user : userList) {
            if (user.getUserName().equals(providedName)) {
                return user;
            }
        }
        return null;
    }
}