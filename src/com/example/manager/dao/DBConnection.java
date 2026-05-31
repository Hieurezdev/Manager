package com.example.manager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/quanlytauhoa?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "maitiendat190705";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            // Ghi log lỗi kết nối nhưng không làm sập luồng của Mock
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
