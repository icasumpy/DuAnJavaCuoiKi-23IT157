package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final String SERVER_PASSWORD = "my_real_server_password";
    private Set<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(11111)) { // Port 12345
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public Set<ClientHandler> getClients() {
        return clients;
    }
}
