package org.example;

import org.foodapp.Admin;
import org.foodapp.Customer;
import org.foodapp.Driver;
import org.foodapp.DriverStatus;
import org.foodapp.RestaurantOwner;
import org.foodapp.User;
import org.foodapp.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User authenticate(String username, String rawPassword) throws SQLException {
        String sql = "SELECT id, username, password_hash, full_name, email, phone, role " +
                "FROM app_user WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null; // tokių vartotojų nėra
                }

                String storedHash = rs.getString("password_hash");
                if (!PasswordUtil.checkPassword(rawPassword, storedHash)) {
                    return null; // slaptažodis neteisingas
                }

                long id = rs.getLong("id");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String roleStr = rs.getString("role");
                UserRole role = UserRole.valueOf(roleStr);

                return switch (role) {
                    case ADMIN -> new Admin(id, username, storedHash, fullName, email, phone, 10);
                    case OWNER -> new RestaurantOwner(id, username, storedHash, fullName, email, phone, "UAB Default");
                    case DRIVER -> new Driver(id, username, storedHash, fullName, email, phone, "ABC123", DriverStatus.AVAILABLE);
                    case CUSTOMER -> new Customer(id, username, storedHash, fullName, email, phone, 0, "Nenurodytas adresas");
                };
            }
        }
    }
}
