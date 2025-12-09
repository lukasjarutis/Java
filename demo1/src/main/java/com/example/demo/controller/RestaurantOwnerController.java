package com.example.demo.controller;

import com.example.demo.repository.RestaurantOwnerRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.entity.RestaurantOwner;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class RestaurantOwnerController {

    @Autowired
    private RestaurantOwnerRepository ownerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    // GET /owners
    @GetMapping
    public List<RestaurantOwner> getAllOwners() {
        return ownerRepository.findAll();
    }

    // GET /owners/{id}
    @GetMapping("/{id}")
    public RestaurantOwner getOwner(@PathVariable Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner " + id + " not found"));
    }

    // GET /owners/{id}/restaurants
    @GetMapping("/{id}/restaurants")
    public List<Restaurant> getRestaurantsByOwner(@PathVariable Long id) {
        return restaurantRepository.findByOwnerId(id);
    }

    // POST /owners  (создание владельца)
    @PostMapping
    public RestaurantOwner createOwner(@RequestBody RestaurantOwner owner) {
        owner.setRole(UserRole.OWNER); // на всякий случай
        return ownerRepository.save(owner);
    }
}
