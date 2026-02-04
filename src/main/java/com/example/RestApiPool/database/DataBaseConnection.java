package com.example.RestApiPool.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DataBaseConnection {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + driverClassName, e);
        }

        return DriverManager.getConnection(dbUrl, username, password);
    }
}