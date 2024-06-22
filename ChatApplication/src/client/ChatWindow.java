package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChatWindow extends JFrame {
    private JTextArea messageArea;
    private JTextField inputField;
    private PrintWriter out;

    public ChatWindow(PrintWriter out) {
        this.out = out;
        setTitle("Chat Application");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                out.println(message);
                inputField.setText("");
            }
        });
        add(inputField, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void appendMessage(String message) {
        messageArea.append(message + "\n");
    }
}
