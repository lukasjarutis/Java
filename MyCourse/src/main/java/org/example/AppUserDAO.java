package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.foodapp.AppUserRow;

public class AppUserDAO {

    public List<AppUserRow> findAll() throws SQLException {
        String sql = "SELECT id, username, password_hash, full_name, email, phone, role FROM app_user";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<AppUserRow> result = new ArrayList<>();
            while (rs.next()) {
                AppUserRow u = new AppUserRow();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setRole(rs.getString("role"));
                result.add(u);
            }
            return result;
        }
    }

    public void insert(AppUserRow user) throws SQLException {
        String sql = "INSERT INTO app_user " +
                "(username, password_hash, full_name, email, phone, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getRole());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
            }
        }
    }

    public void update(AppUserRow user) throws SQLException {
        String sql = "UPDATE app_user SET " +
                "username = ?, password_hash = ?, full_name = ?, email = ?, phone = ?, role = ? " +
                "WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getRole());
            ps.setLong(7, user.getId());

            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM app_user WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
