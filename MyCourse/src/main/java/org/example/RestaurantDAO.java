package org.example;

import org.foodapp.Restaurant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {

    public List<Restaurant> findAll() throws SQLException {
        List<Restaurant> result = new ArrayList<>();
        String sql = "SELECT id, name, address, open, popularity_score FROM restaurant";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Restaurant r = new Restaurant();
                r.setId(rs.getLong("id"));
                r.setName(rs.getString("name"));
                r.setAddress(rs.getString("address"));
                r.setOpen(rs.getBoolean("open"));
                r.setPopularityScore(rs.getDouble("popularity_score"));
                result.add(r);
            }
        }
        return result;
    }

    public List<Restaurant> findByName(String nameFilter) throws SQLException {
        List<Restaurant> result = new ArrayList<>();
        String sql = "SELECT id, name, address, open, popularity_score " +
                "FROM restaurant WHERE name LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nameFilter + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Restaurant r = new Restaurant();
                    r.setId(rs.getLong("id"));
                    r.setName(rs.getString("name"));
                    r.setAddress(rs.getString("address"));
                    r.setOpen(rs.getBoolean("open"));
                    r.setPopularityScore(rs.getDouble("popularity_score"));
                    result.add(r);
                }
            }
        }
        return result;
    }

    public List<Restaurant> findFiltered(String nameFilter,
                                         Boolean openOnly,
                                         Double minPopularity) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id, name, address, open, popularity_score FROM restaurant WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (nameFilter != null && !nameFilter.isBlank()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + nameFilter + "%");
        }

        if (openOnly != null && openOnly) {
            sql.append(" AND open = TRUE");
        }

        if (minPopularity != null) {
            sql.append(" AND popularity_score >= ?");
            params.add(minPopularity);
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            List<Restaurant> result = new ArrayList<>();

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Restaurant r = new Restaurant();
                    r.setId(rs.getLong("id"));
                    r.setName(rs.getString("name"));
                    r.setAddress(rs.getString("address"));
                    r.setOpen(rs.getBoolean("open"));
                    r.setPopularityScore(rs.getDouble("popularity_score"));
                    result.add(r);
                }
            }

            return result;
        }
    }

    public void insert(Restaurant restaurant) throws SQLException {
        String sql = "INSERT INTO restaurant (name, address, open, popularity_score) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getAddress());
            ps.setBoolean(3, restaurant.isOpen());
            ps.setDouble(4, restaurant.getPopularityScore());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    restaurant.setId(keys.getLong(1));
                }
            }
        }
    }

    public void update(Restaurant restaurant) throws SQLException {
        String sql = "UPDATE restaurant " +
                "SET name = ?, address = ?, open = ?, popularity_score = ? " +
                "WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getAddress());
            ps.setBoolean(3, restaurant.isOpen());
            ps.setDouble(4, restaurant.getPopularityScore());
            ps.setLong(5, restaurant.getId());

            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM restaurant WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
