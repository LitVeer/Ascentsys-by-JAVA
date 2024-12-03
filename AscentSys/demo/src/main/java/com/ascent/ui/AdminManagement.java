package com.ascent.ui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import com.ascent.bean.User;
import com.ascent.service.UserDataAccessorImpl;

public class AdminManagement extends JFrame {
    private JTable usersTable;
    private JButton addUserButton;
    private JButton delUserButton;
    private UserDataAccessorImpl userDataAccessor = new UserDataAccessorImpl();
    

    public AdminManagement() {
        setTitle("用户管理面板");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeWindow();
            }
        });
        setLayout(new BorderLayout());

        initializeusersTable();
        initializeButtons();
    }

    private void initializeusersTable() {
        String[] columnNames = {"User Name", "Password", "Authenority"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        usersTable = new JTable(model);
        usersTable.setFillsViewportHeight(true);

        // Add TableModelListener to detect changes in the table
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    updateUserInfo(row);
                }
            }
        });

        updateusersTable();
        add(new JScrollPane(usersTable), BorderLayout.CENTER);
    }

    private void initializeButtons() {

        addUserButton = new JButton("添加用户");
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddUserEvent();
            }
        });

        delUserButton = new JButton("删除该用户");
        delUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelUserEvent();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addUserButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // 添加水平间隔
        buttonPanel.add(delUserButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleAddUserEvent() {
        String username = JOptionPane.showInputDialog(this, "请输入新用户的用户名:");
        String password = JOptionPane.showInputDialog(this, "请输入新用户的密码:");
        if (username != null && password != null) {
            User user = new User(username, password, 0);
            userDataAccessor.addUser(user);
            updateusersTable();
        }
    }

    private void handleDelUserEvent() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow != -1) {
            User user = getUserByRow(selectedRow);
            if (user != null) {
                userDataAccessor.deleteUser(user.getUsername());
                updateusersTable();
            }
        }
    }

    private void updateusersTable() {
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        model.setRowCount(0);
        ArrayList<User> users = new ArrayList<>(userDataAccessor.getUsers().values());

        for (User user : users) {
            model.addRow(new Object[] {
                user.getUsername(),
                user.getPassword(),
                user.getAuthority()
            });
        }
    }

    private void updateUserInfo(int row) {
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        Object usernameObj = model.getValueAt(row, 0);
        Object passwordObj = model.getValueAt(row, 1);
        Object authenorityObj = model.getValueAt(row, 2);
        try {
            String newUsername = usernameObj.toString();
            String newPassword = passwordObj.toString();
            int newAuthenority = Integer.parseInt(authenorityObj.toString());
            User User = getUserByRow(row);
            if (User != null && newAuthenority >= 0 && newAuthenority < 2) {
                User.setUsername(newUsername);
                User.setPassword(newPassword);
                User.setAuthority(newAuthenority);
                userDataAccessor.updateUser(User);
                JOptionPane.showMessageDialog(this, "用户信息已更新", "成功", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的权限值", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private User getUserByRow(int row) {
        ArrayList<User> Users = new ArrayList<>(userDataAccessor.getUsers().values());
        if (row >= 0 && row < Users.size()) {
            return Users.get(row);
        }
        return null;
    }

    private void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminManagement().setVisible(true);
            }
        });
    }
}
