package com.example.demo.repository;

import com.example.demo.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findByCategoryIgnoreCase(String category);

    List<MenuItem> findByAvailableTrue();
}
