package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;

import com.ascent.bean.*;
import com.ascent.service.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("登录");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // 创建主面板，设置为垂直布局
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距

        // 添加用户名部分
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("用户名:"));
        usernameField = new JTextField(15);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        // 添加间隔
        mainPanel.add(Box.createVerticalStrut(10)); // 添加垂直间隔

        // 添加密码部分
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(new JLabel("    密码:"));
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        // 添加间隔
        mainPanel.add(Box.createVerticalStrut(10));

        // 添加登录按钮
        loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoginEvent();
            }
        });
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 按钮居中
        mainPanel.add(loginButton);

        // 添加主面板到窗口
        add(mainPanel);

        setVisible(true);
    }

    private void handleLoginEvent() {
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
                        new MainFrame().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "用户名或密码错误");
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
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
