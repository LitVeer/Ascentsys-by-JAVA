package com.ascent.repository;

import java.sql.*;

import com.ascent.config;

public class DatabaseConnection {

    // 数据库连接信息
    private static final String URL = config.DB_URL;
    private static final String USER = config.DB_USER;
    private static final String PASSWORD = config.DB_PASSWORD;
    public static Connection connection;

    public DatabaseConnection() {
    }

    // 获取数据库连接
    public static Connection getConnection() {
        try {
            // 加载 MySQL JDBC 驱动
            Class.forName(config.DB_DRIVER);
            // 获取数据库连接
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("数据库连接成功！");
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("数据库连接失败！");
            return null;
        }
    }

    // 关闭数据库连接
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("数据库连接已关闭！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("关闭数据库连接时出错！");
        }
    }

    // public static void main(String[] args) {
    //     DatabaseConnect dbConnect = new DatabaseConnect();
    //     dbConnect.closeConnection();
    // }
}
