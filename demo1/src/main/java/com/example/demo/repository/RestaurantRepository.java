package com.example.demo.repository;

import com.example.demo.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByOpenTrue();

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    List<Restaurant> findByOwnerId(Long ownerId);
}
