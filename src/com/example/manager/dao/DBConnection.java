package com.example.manager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Đăng ký Driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Chuỗi kết nối đến Database train_management
                String dbUrl = "jdbc:mysql://localhost:3306/train_management?useSSL=false&characterEncoding=UTF-8";
                String user = "root"; // Thay bằng username MySQL của bạn
                String password = "123456"; // Thay bằng mật khẩu MySQL của bạn

                // Mở kết nối
                connection = DriverManager.getConnection(dbUrl, user, password);
                System.out.println("Kết nối cơ sở dữ liệu thành công!");

            } catch (ClassNotFoundException e) {
                System.err.println("Không tìm thấy Driver MySQL: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Đã đóng kết nối cơ sở dữ liệu.");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }
}
