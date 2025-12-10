package org.foodapp.repository;

import org.foodapp.Customer;
import org.foodapp.Driver;
import org.foodapp.Order;
import org.foodapp.OrderStatus;
import org.foodapp.Restaurant;
import org.foodapp.exception.NotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Order> rowMapper = (rs, rowNum) -> {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setTotalPrice(rs.getDouble("total_price"));
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

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

        return order;
    };

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Order> findAll() {
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
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Order findById(long id) {
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
                WHERE o.id = ?
                """;
        List<Order> orders = jdbcTemplate.query(sql, rowMapper, id);
        return orders.stream().findFirst().orElseThrow(() -> new NotFoundException("Order not found: " + id));
    }

    public Order create(Order order) {
        String sql = "INSERT INTO food_order (restaurant_id, customer_id, driver_id, status, total_price, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, order.getRestaurant().getId());
            ps.setLong(2, order.getCustomer().getId());
            if (order.getDriver() != null) {
                ps.setLong(3, order.getDriver().getId());
            } else {
                ps.setNull(3, java.sql.Types.BIGINT);
            }
            ps.setString(4, order.getStatus().name());
            ps.setDouble(5, order.getTotalPrice());
            ps.setTimestamp(6, Timestamp.valueOf(order.getCreatedAt()));
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            order.setId(keyHolder.getKey().longValue());
        }
        return order;
    }

    public Order updateStatus(long id, OrderStatus status) {
        Order existing = findById(id);
        jdbcTemplate.update("UPDATE food_order SET status = ? WHERE id = ?", status.name(), id);
        existing.setStatus(status);
        return existing;
    }
}
