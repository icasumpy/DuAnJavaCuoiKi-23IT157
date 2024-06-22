package client.dao;

import client.models.Message;
import client.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public void saveMessage(int senderId, int receiverId, String message) throws SQLException {
        String sql = "INSERT INTO messages (sender_id, receiver_id, message, timestamp) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, message);
            stmt.executeUpdate();
        }
    }

    public Message getMessageById(int id) throws SQLException {
        String sql = "SELECT * FROM messages WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Message(rs.getInt("id"), rs.getInt("sender_id"), rs.getInt("receiver_id"), rs.getString("message"), rs.getString("timestamp"));
                }
            }
        }
        return null;
    }

    public List<Message> getAllMessages() throws SQLException {
        String sql = "SELECT * FROM messages";
        List<Message> messages = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                messages.add(new Message(rs.getInt("id"), rs.getInt("sender_id"), rs.getInt("receiver_id"), rs.getString("message"), rs.getString("timestamp")));
            }
        }
        return messages;
    }

    public List<Message> getMessagesByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM messages WHERE sender_id = ? OR receiver_id = ?";
        List<Message> messages = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(new Message(rs.getInt("id"), rs.getInt("sender_id"), rs.getInt("receiver_id"), rs.getString("message"), rs.getString("timestamp")));
                }
            }
        }
        return messages;
    }

}
