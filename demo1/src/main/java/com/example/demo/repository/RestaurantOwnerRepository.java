package com.example.demo.repository;

import com.example.demo.entity.RestaurantOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantOwnerRepository extends JpaRepository<RestaurantOwner, Long> {

    List<RestaurantOwner> findByCompanyNameContainingIgnoreCase(String companyName);
}
