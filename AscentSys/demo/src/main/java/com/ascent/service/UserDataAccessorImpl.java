package com.ascent.service;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ascent.bean.User;
import com.ascent.repository.DatabaseConnection;
import com.ascent.repository.UserDataAccessor;

public class UserDataAccessorImpl implements UserDataAccessor {

    private Connection connection = null;
    
    // 构造函数：初始化连接数据库
    public UserDataAccessorImpl() {
        connection = DatabaseConnection.getConnection();
    }

    // 添加用户
    public boolean addUser(User user) {
        try {
            String sql = "INSERT INTO users (username, password, authority) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getAuthority());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            if (rowsAffected > 0) {
                System.out.println("添加用户成功");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("添加用户失败");
            e.printStackTrace();
        }
        return false;
    }

    // 删除用户
    public boolean deleteUser(String username) {
        try {
            String sql = "DELETE FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            int rowsAffected = statement.executeUpdate();
            statement.close();
            if (rowsAffected > 0) {
                System.out.println("删除用户成功");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("删除用户失败");
            e.printStackTrace();
        }
        return false;
    }

    // 更新用户密码权限
    public boolean updateUser(User user) {
        try {
            String sql = "UPDATE users SET password = ?, authority = ? WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(3, user.getUsername());
            statement.setString(1, user.getPassword());
            statement.setInt(2, user.getAuthority());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            if (rowsAffected > 0) {
                System.out.println("更新用户信息成功");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("更新用户信息失败");
            e.printStackTrace();
        }
        return false;
    }

    // 获取所有用户信息
    public HashMap<String, User> getUsers() {
        try {
            String sql = "SELECT * FROM users";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            HashMap<String, User> users = new HashMap<>();
            while (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setAuthority(resultSet.getInt("authority"));
                users.put(user.getUsername(), user);
            }
            statement.close();
            resultSet.close();
            System.out.println("获取用户信息成功");
            return users;
        } catch (SQLException e) {
            System.out.println("获取用户信息失败");
            e.printStackTrace();
        }
        return null;
    }

    // public static void main(String[] args) {
    //     UserDataAccessorImpl UserDataAccessorImpl = new UserDataAccessorImpl();
    //     UserDataAccessorImpl.updateUser(new User("admin", "admin", 0));
    //     HashMap<String, User> users = UserDataAccessorImpl.getUsers();
    //     System.out.println(users.toString());
    // }
}
