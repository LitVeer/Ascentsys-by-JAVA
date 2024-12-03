package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;

import com.ascent.bean.*;
import com.ascent.service.*;

public class AdminAuthenrity extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton userLoginButton;
    private JButton productLoginButton;

    public AdminAuthenrity() {
        setTitle("管理员后台登录");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // 创建主面板，设置为垂直布局
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距

        // 添加用户名部分
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("管理员用户名:"));
        usernameField = new JTextField(15);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        // 添加间隔
        //mainPanel.add(Box.createVerticalStrut(2)); // 添加10像素的垂直间隔

        // 添加密码部分
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(new JLabel("    管理员密码:"));
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        // 添加间隔
        mainPanel.add(Box.createVerticalStrut(10));

        // 添加登录按钮
        JPanel buttonPanel = new JPanel();
        userLoginButton = new JButton("登录");
        userLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoginEvent("user");
            }
        });
        productLoginButton = new JButton("商品管理");
        productLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoginEvent("product");
            }
        });
        buttonPanel.add(userLoginButton);
        mainPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(productLoginButton);
        mainPanel.add(buttonPanel);

        // 添加主面板到窗口
        add(mainPanel);

        setVisible(true);
    }

    private void handleLoginEvent(String loginWindow) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "用户名和密码不能为空");
            return;
        }
        new Thread(() -> {
            try {
                boolean success = login(username, password);
                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        dispose();
                        if (loginWindow == "user") new AdminManagement().setVisible(true);
                        else if (loginWindow == "product") new ProductManagement().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "权限不足");
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "登录时发生错误: " + ex.getMessage());
                });
            }
        }).start();
    }

    private boolean login(String username, String password) {
        UserDataAccessorImpl userDataAccessor = new UserDataAccessorImpl();
        HashMap<String, User> users = userDataAccessor.getUsers();
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.getAuthority() == 1) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminAuthenrity());
    }
}
