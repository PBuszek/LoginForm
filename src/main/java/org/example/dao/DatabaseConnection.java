package org.example.dao;

import java.sql.*;
import java.sql.Connection;

public class DatabaseConnection {

    private String user = "ignacy"; //TODO please provide your user password and connection string
    private String password = "ignacy";
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/Login_form";


    public DatabaseConnection() {
    }

    Connection databaseConnection;

    public Connection connect() throws ClassNotFoundException {
        databaseConnection = null;
        try {
            Class.forName("org.postgresql.Driver");
            databaseConnection = DriverManager.getConnection(CONNECTION_STRING, user, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Database connection successfully !");
        return databaseConnection;
    }

    public Connection getDatabaseConnection() throws ClassNotFoundException {
        connect();
        return databaseConnection;
    }
}