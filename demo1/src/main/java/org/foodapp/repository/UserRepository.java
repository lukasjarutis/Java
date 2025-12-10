package org.foodapp.repository;

import org.foodapp.AppUserRow;
import org.foodapp.exception.AuthenticationException;
import org.foodapp.security.PasswordUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
}
