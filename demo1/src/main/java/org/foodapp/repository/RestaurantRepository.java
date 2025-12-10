package org.foodapp.repository;

import org.foodapp.Restaurant;
import org.foodapp.exception.NotFoundException;
import org.springframework.jdbc.core.GeneratedKeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class RestaurantRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedTemplate;

    private final RowMapper<Restaurant> rowMapper = (rs, rowNum) -> {
        Restaurant r = new Restaurant();
        r.setId(rs.getLong("id"));
        r.setName(rs.getString("name"));
        r.setAddress(rs.getString("address"));
        r.setOpen(rs.getBoolean("open"));
        r.setPopularityScore(rs.getDouble("popularity_score"));
        return r;
    };

    public RestaurantRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedTemplate = namedTemplate;
    }

    public List<Restaurant> findAll() {
        String sql = "SELECT id, name, address, open, popularity_score FROM restaurant";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Restaurant> findFiltered(String nameFilter, Boolean openOnly, Double minPopularity) {
        StringBuilder sql = new StringBuilder("SELECT id, name, address, open, popularity_score FROM restaurant WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (nameFilter != null && !nameFilter.isBlank()) {
            sql.append(" AND name LIKE :name");
            params.addValue("name", "%" + nameFilter + "%");
        }
        if (openOnly != null && openOnly) {
            sql.append(" AND open = TRUE");
        }
        if (minPopularity != null) {
            sql.append(" AND popularity_score >= :popularity");
            params.addValue("popularity", minPopularity);
        }

        return namedTemplate.query(sql.toString(), params, rowMapper);
    }

    public Optional<Restaurant> findById(long id) {
        String sql = "SELECT id, name, address, open, popularity_score FROM restaurant WHERE id = ?";
        List<Restaurant> restaurants = jdbcTemplate.query(sql, rowMapper, id);
        return restaurants.stream().findFirst();
    }

    public Restaurant save(Restaurant restaurant) {
        String sql = "INSERT INTO restaurant (name, address, open, popularity_score) VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getAddress());
            ps.setBoolean(3, restaurant.isOpen());
            ps.setDouble(4, restaurant.getPopularityScore());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            restaurant.setId(keyHolder.getKey().longValue());
        }
        return restaurant;
    }

    public Restaurant update(long id, Restaurant updated) {
        Restaurant existing = findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found: " + id));
        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        existing.setOpen(updated.isOpen());
        existing.setPopularityScore(updated.getPopularityScore());

        String sql = "UPDATE restaurant SET name = ?, address = ?, open = ?, popularity_score = ? WHERE id = ?";
        jdbcTemplate.update(sql, existing.getName(), existing.getAddress(), existing.isOpen(), existing.getPopularityScore(), id);
        return existing;
    }

    public void delete(long id) {
        int count = jdbcTemplate.update("DELETE FROM restaurant WHERE id = ?", id);
        if (count == 0) {
            throw new NotFoundException("Restaurant not found: " + id);
        }
    }
}
