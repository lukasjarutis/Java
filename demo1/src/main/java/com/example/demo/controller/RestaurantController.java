package com.example.demo.controller;

import com.example.demo.entity.Restaurant;
import com.example.demo.entity.RestaurantOwner;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.RestaurantOwnerRepository;
import com.example.demo.exception.RestaurantNotFound;
import com.example.demo.dto.RestaurantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantOwnerRepository ownerRepository;

    // GET /restaurants – все рестораны
    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    // GET /restaurants/{id} – один ресторан
    @GetMapping("/{id}")
    public Restaurant getOne(@PathVariable Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFound(id));
    }

    // GET /restaurants/open – только открытые
    @GetMapping("/open")
    public List<Restaurant> getOpen() {
        return restaurantRepository.findByOpenTrue();
    }

    // GET /restaurants/owner/{ownerId} – рестораны конкретного владельца
    @GetMapping("/owner/{ownerId}")
    public List<Restaurant> getByOwner(@PathVariable Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId);
    }

    // POST /restaurants – создаём ресторан с ownerId
    @PostMapping
    public Restaurant create(@RequestBody RestaurantRequest request) {

        if (request.getOwnerId() == null) {
            throw new IllegalArgumentException("ownerId is required");
        }

        RestaurantOwner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner " + request.getOwnerId() + " not found"));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setOpen(request.getOpen() != null ? request.getOpen() : true);
        restaurant.setPopularityScore(request.getPopularityScore() != null ? request.getPopularityScore() : 0.0);
        restaurant.setOwner(owner);

        return restaurantRepository.save(restaurant);
    }

    // PUT /restaurants/{id} – обновить ресторан (в т.ч. сменить владельца)
    @PutMapping("/{id}")
    public Restaurant update(@PathVariable Long id,
                             @RequestBody RestaurantRequest request) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFound(id));

        if (request.getName() != null) {
            restaurant.setName(request.getName());
        }
        if (request.getAddress() != null) {
            restaurant.setAddress(request.getAddress());
        }
        if (request.getOpen() != null) {
            restaurant.setOpen(request.getOpen());
        }
        if (request.getPopularityScore() != null) {
            restaurant.setPopularityScore(request.getPopularityScore());
        }
        if (request.getOwnerId() != null) {
            RestaurantOwner owner = ownerRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner " + request.getOwnerId() + " not found"));
            restaurant.setOwner(owner);
        }

        return restaurantRepository.save(restaurant);
    }

    // DELETE /restaurants/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        restaurantRepository.deleteById(id);
    }
}
