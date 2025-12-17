package org.foodapp.repository;

import org.foodapp.AppUserRow;
import org.foodapp.UserRole;
import org.foodapp.exception.AuthenticationException;
import org.foodapp.exception.NotFoundException;
import org.foodapp.security.PasswordUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<AppUserRow> mapper = (rs, rowNum) -> new AppUserRow(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("full_name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("role"),
            rs.getString("password_hash")
    );

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<AppUserRow> findById(long id) {
        String sql = "SELECT id, username, full_name, email, phone, role, password_hash FROM app_user WHERE id = ?";
        List<AppUserRow> users = jdbcTemplate.query(sql, mapper, id);
        return users.stream().findFirst();
    }

    public Optional<AppUserRow> findByUsername(String username) {
        String sql = "SELECT id, username, full_name, email, phone, role, password_hash FROM app_user WHERE username = ?";
        List<AppUserRow> users = jdbcTemplate.query(sql, mapper, username);
        return users.stream().findFirst();
    }

    public AppUserRow authenticate(String username, String rawPassword) {
        AppUserRow user = findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!PasswordUtil.checkPassword(rawPassword, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password");
        }
        return user;
    }

    public AppUserRow createUser(String username,
                                 String rawPassword,
                                 String fullName,
                                 String email,
                                 String phone,
                                 UserRole role) {
        String sql = "INSERT INTO app_user (username, full_name, email, phone, role, password_hash) VALUES (?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String passwordHash = PasswordUtil.hashPassword(rawPassword);

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, fullName);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, role != null ? role.name() : UserRole.CUSTOMER.name());
            ps.setString(6, passwordHash);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        return new AppUserRow(id, username, fullName, email, phone,
                role != null ? role.name() : UserRole.CUSTOMER.name(), passwordHash);
    }

    public AppUserRow updateContactInfo(long id, String fullName, String email, String phone) {
        AppUserRow existing = findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        jdbcTemplate.update(
                "UPDATE app_user SET full_name = ?, email = ?, phone = ? WHERE id = ?",
                fullName,
                email,
                phone,
                id
        );

        existing.setFullName(fullName);
        existing.setEmail(email);
        existing.setPhone(phone);
        return existing;
    }
}
