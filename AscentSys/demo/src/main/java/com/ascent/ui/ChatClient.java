package com.ascent.ui;

import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ChatClient {
    private JFrame frame;
    private JButton connectButton, sendButton;
    private JTextArea chatArea, inputArea;
    private JTextField ipField, portField;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private Thread receiveThread;

    public ChatClient() {
        frame = new JFrame("Chat Client");
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 上部连接设置区域
        JPanel connectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        ipField = new JTextField("127.0.0.1", 10);
        portField = new JTextField("4700", 5);
        connectButton = new JButton("Connect");
        connectPanel.add(new JLabel("IP:"));
        connectPanel.add(ipField);
        connectPanel.add(new JLabel("Port:"));
        connectPanel.add(portField);
        connectPanel.add(connectButton);

        // 中间聊天记录区域
        chatArea = new JTextArea(15, 30);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        // 底部输入区域
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputArea = new JTextArea(3, 20);
        inputArea.setLineWrap(true);
        inputArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        sendButton = new JButton("Send");
        sendButton.setEnabled(false); // 初始状态下禁用
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // 添加组件到主框架
        frame.add(connectPanel, BorderLayout.NORTH);
        frame.add(chatScrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // 设置事件监听
        connectButton.addActionListener(e -> connectToServer());
        sendButton.addActionListener(e -> sendMessage());

        // 显示窗口
        frame.setVisible(true);
    }

    private void connectToServer() {
        String ip = ipField.getText().trim();
        int port = Integer.parseInt(portField.getText().trim());

        try {
            socket = new Socket(ip, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            chatArea.append("Connected to server at " + ip + ":" + port + "\n");
            sendButton.setEnabled(true);
            connectButton.setEnabled(false);

            // 启动接收消息的线程
            receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();
        } catch (IOException e) {
            chatArea.append("Failed to connect to server: " + e.getMessage() + "\n");
        }
    }

    private void sendMessage() {
        String message = inputArea.getText().trim();
        if (!message.isEmpty()) {
            output.println(message); // 发送到服务器
            chatArea.append("Me: " + message + "\n");
            inputArea.setText(""); // 清空输入区域
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = input.readLine()) != null) {
                chatArea.append("Server: " + message + "\n");
            }
        } catch (IOException e) {
            chatArea.append("Connection closed: " + e.getMessage() + "\n");
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException ignored) {}
        }
    }

    public static void main(String[] args) {
        new ChatClient();
    }
}