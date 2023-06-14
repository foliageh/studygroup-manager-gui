package com.labproject.utils;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    public static Connection connection;
    private DatabaseConnection(){}
    public static Connection getInstance() {
        if (connection != null) return connection;
        try {
            Properties props = new Properties();
            props.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties"));
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
