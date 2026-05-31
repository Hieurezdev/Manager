package com.example.manager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/manager_db?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "Hch301105#";

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
