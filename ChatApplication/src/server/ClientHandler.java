package server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.models.User;
import server.dao.UserDAO;
import server.dao.MessageDAO;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Server server;
    private User user;
    private UserDAO userDAO;
    private MessageDAO messageDAO;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.userDAO = new UserDAO();
        this.messageDAO = new MessageDAO();
    }

    @Override
public void run() {
    try {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Yêu cầu mật khẩu từ client
        out.println("Please enter server password:");

        // Đọc mật khẩu từ client
        String enteredPassword = in.readLine();

        // Kiểm tra mật khẩu
        if (!enteredPassword.equals("your_server_password_here")) { // Thay bằng mật khẩu thực của server
            out.println("Authentication failed. Invalid server password.");
            socket.close();
            return;
        }

        // Đăng nhập thành công
        out.println("Authentication successful. Please enter your username:");

        // Đọc username từ client
        String username = in.readLine();

        // Đăng nhập thành công, thông báo client
        out.println("Please enter your password:");

        // Đọc password từ client
        String password = in.readLine();

        if (userDAO.authenticate(username, password)) {
            // Xác thực thành công, thực hiện các thao tác tiếp theo
            this.user = new User(1, username, password, "user"); // Thay thế bằng dữ liệu người dùng thực tế
            out.println("Authentication successful");
        } else {
            // Xác thực thất bại
            out.println("Authentication failed");
            socket.close();
            return;
        }

        // Xử lý tin nhắn từ client và gửi lại
        String message;
        while ((message = in.readLine()) != null) {
            messageDAO.saveMessage(user.getId(), 0, message); // Thay 0 bằng receiverId thực tế
            server.broadcastMessage(message, this); // Gửi tin nhắn đến tất cả client khác
        }
    } catch (IOException | SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


    public void sendMessage(String message) {
        out.println(message);
    }
}
