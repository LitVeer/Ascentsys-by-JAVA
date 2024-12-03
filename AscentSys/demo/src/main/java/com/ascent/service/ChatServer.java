package com.ascent.service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatServer {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextArea inputArea;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Thread receiveThread;
    JButton listenButton;

    public ChatServer() {
        frame = new JFrame("Chat Server");
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 聊天记录区域
        chatArea = new JTextArea(15, 30);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        // 输入区域
        inputArea = new JTextArea(4, 30);
        inputArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
        listenButton = new JButton("Listen");
        listenButton.addActionListener(e -> startListening());

        frame.setLayout(new BorderLayout());
        frame.add(chatScrollPane, BorderLayout.CENTER);
        frame.add(inputArea, BorderLayout.SOUTH);
        frame.add(listenButton, BorderLayout.WEST);

        frame.setVisible(true);
    }

    private void startListening() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(4700);
                clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                listenButton.setEnabled(false);
                inputArea.setEditable(true);
                inputArea.setText("Type a message to send to the client...");

                // 启动接收消息的线程
                receiveThread = new Thread(this::receiveMessages);
                receiveThread.start();

                // 设置输入框监听器，发送消息给客户端
            } catch (IOException e) {
                chatArea.append("Server exception: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                chatArea.append("Client: " + message + "\n");
                if ("bye".equalsIgnoreCase(message)) {
                    out.println("Goodbye!");
                    break;
                }
            }
        } catch (IOException e) {
            chatArea.append("Connection error: " + e.getMessage() + "\n");
        } finally {
            try {
                if (clientSocket != null) clientSocket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                chatArea.append("Error closing connection: " + e.getMessage() + "\n");
            }
        }
    }

    private void sendMessage() {
        String message = inputArea.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            chatArea.append("Server: " + message + "\n");
            inputArea.setText("");
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}