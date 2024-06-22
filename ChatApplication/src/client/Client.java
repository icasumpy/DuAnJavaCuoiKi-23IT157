package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        initComponents();
        connectToServer();
    }

    private void initComponents() {
        setTitle("Chat Client");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Đặt cửa sổ giữa màn hình

        // Panel chứa chatArea và messageField
        JPanel mainPanel = new JPanel(new BorderLayout());

        // TextArea để hiển thị các tin nhắn
        chatArea = new JTextArea();
        chatArea.setEditable(false); // Không cho phép chỉnh sửa
        JScrollPane scrollPane = new JScrollPane(chatArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel chứa messageField và sendButton
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);
        sendButton = new JButton("Send");
        inputPanel.add(sendButton, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Sự kiện khi nhấn nút Send
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    messageField.setText("");
                }
            }
        });

        getContentPane().add(mainPanel);

        setVisible(true);
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 11111); // Kết nối đến localhost và port 12345
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Đăng nhập
            String username = JOptionPane.showInputDialog("Enter username:");
            out.println(username);
            String password = JOptionPane.showInputDialog("Enter password:");
            out.println(password);

            // Nhận tin nhắn từ server và hiển thị trên chatArea
            String message;
            while ((message = in.readLine()) != null) {
                chatArea.append(message + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        out.println(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });
    }
}
