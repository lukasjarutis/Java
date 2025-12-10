package org.example;

import org.foodapp.*;
import org.foodapp.Driver;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public List<Order> findAll() throws SQLException {
        String sql = """
                SELECT o.id, o.restaurant_id, o.customer_id, o.driver_id,
                       o.status, o.total_price, o.created_at,
                       r.name AS restaurant_name,
                       c.username AS customer_username,
                       d.username AS driver_username
                FROM food_order o
                JOIN restaurant r ON o.restaurant_id = r.id
                JOIN app_user c ON o.customer_id = c.id
                LEFT JOIN app_user d ON o.driver_id = d.id
                ORDER BY o.created_at DESC
                """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Order> result = new ArrayList<>();

            while (rs.next()) {
                Order order = mapRow(rs);
                result.add(order);
            }

            return result;
        }
    }

    public List<Order> findFiltered(OrderStatus status,
                                    LocalDateTime from,
                                    LocalDateTime to,
                                    String customer,
                                    String restaurantName) throws SQLException {
        StringBuilder sql = new StringBuilder("""
                SELECT o.id, o.restaurant_id, o.customer_id, o.driver_id,
                       o.status, o.total_price, o.created_at,
                       r.name AS restaurant_name,
                       c.username AS customer_username,
                       d.username AS driver_username
                FROM food_order o
                JOIN restaurant r ON o.restaurant_id = r.id
                JOIN app_user c ON o.customer_id = c.id
                LEFT JOIN app_user d ON o.driver_id = d.id
                WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND o.status = ?");
            params.add(status.name());
        }

        if (from != null) {
            sql.append(" AND o.created_at >= ?");
            params.add(Timestamp.valueOf(from));
        }

        if (to != null) {
            sql.append(" AND o.created_at <= ?");
            params.add(Timestamp.valueOf(to));
        }

        if (customer != null && !customer.isBlank()) {
            sql.append(" AND LOWER(c.username) LIKE ?");
            params.add("%" + customer.toLowerCase() + "%");
        }

        if (restaurantName != null && !restaurantName.isBlank()) {
            sql.append(" AND LOWER(r.name) LIKE ?");
            params.add("%" + restaurantName.toLowerCase() + "%");
        }

        sql.append(" ORDER BY o.created_at DESC");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            List<Order> result = new ArrayList<>();

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRow(rs);
                    result.add(order);
                }
            }

            return result;
        }
    }

    public void insert(Order order) throws SQLException {
        String sql = "INSERT INTO food_order " +
                "(restaurant_id, customer_id, driver_id, status, total_price, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, order.getRestaurant().getId());
            ps.setLong(2, order.getCustomer().getId());
            if (order.getDriver() != null) {
                ps.setLong(3, order.getDriver().getId());
            } else {
                ps.setNull(3, Types.BIGINT);
            }
            ps.setString(4, order.getStatus().name());
            ps.setDouble(5, order.getTotalPrice());
            ps.setTimestamp(6, Timestamp.valueOf(order.getCreatedAt()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setId(keys.getLong(1));
                }
            }
        }
    }

    public void update(Order order) throws SQLException {
        String sql = "UPDATE food_order SET " +
                "restaurant_id = ?, customer_id = ?, driver_id = ?, status = ?, total_price = ? " +
                "WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, order.getRestaurant().getId());
            ps.setLong(2, order.getCustomer().getId());
            if (order.getDriver() != null) {
                ps.setLong(3, order.getDriver().getId());
            } else {
                ps.setNull(3, Types.BIGINT);
            }
            ps.setString(4, order.getStatus().name());
            ps.setDouble(5, order.getTotalPrice());
            ps.setLong(6, order.getId());

            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM food_order WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(rs.getLong("restaurant_id"));
        restaurant.setName(rs.getString("restaurant_name"));
        order.setRestaurant(restaurant);

        Customer customer = new Customer();
        customer.setId(rs.getLong("customer_id"));
        customer.setUsername(rs.getString("customer_username"));
        order.setCustomer(customer);

        long driverId = rs.getLong("driver_id");
        if (!rs.wasNull()) {
            Driver driver = new Driver();
            driver.setId(driverId);
            driver.setUsername(rs.getString("driver_username"));
            order.setDriver(driver);
        }

        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setTotalPrice(rs.getDouble("total_price"));
        Timestamp ts = rs.getTimestamp("created_at");
        order.setCreatedAt(ts.toLocalDateTime());

        return order;
    }
}
