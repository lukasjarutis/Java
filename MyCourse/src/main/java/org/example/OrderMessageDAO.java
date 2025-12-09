package org.example;

import org.foodapp.OrderMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMessageDAO {

    public List<OrderMessage> findByOrderId(long orderId) throws SQLException {
        String sql = """
            SELECT m.id, m.order_id, m.user_id, m.message_text, m.created_at,
                   u.username
            FROM order_message m
            JOIN app_user u ON m.user_id = u.id
            WHERE m.order_id = ?
            ORDER BY m.created_at ASC
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);

            List<OrderMessage> list = new ArrayList<>();

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderMessage msg = new OrderMessage();
                    msg.setId(rs.getLong("id"));
                    msg.setOrderId(rs.getLong("order_id"));
                    msg.setUserId(rs.getLong("user_id"));
                    msg.setUsername(rs.getString("username"));
                    msg.setMessageText(rs.getString("message_text"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    msg.setCreatedAt(ts.toLocalDateTime());
                    list.add(msg);
                }
            }

            return list;
        }
    }

    public void insert(long orderId, long userId, String text) throws SQLException {
        String sql = """
            INSERT INTO order_message (order_id, user_id, message_text)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, userId);
            ps.setString(3, text);
            ps.executeUpdate();
        }
    }
}
