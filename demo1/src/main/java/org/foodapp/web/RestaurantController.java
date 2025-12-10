package org.foodapp.web;

import org.foodapp.Restaurant;
import org.foodapp.service.RestaurantService;
import org.foodapp.web.dto.RestaurantRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getRestaurants(@RequestParam(required = false) String name,
                                                           @RequestParam(required = false) Boolean openOnly,
                                                           @RequestParam(required = false) Double minPopularity) {
        return ResponseEntity.ok(restaurantService.list(name, openOnly, minPopularity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.get(id));
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setOpen(Boolean.TRUE.equals(request.getOpen()));
        restaurant.setPopularityScore(request.getPopularityScore() != null ? request.getPopularityScore() : 0.0);
        return ResponseEntity.ok(restaurantService.create(restaurant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable long id, @RequestBody RestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setOpen(Boolean.TRUE.equals(request.getOpen()));
        restaurant.setPopularityScore(request.getPopularityScore() != null ? request.getPopularityScore() : 0.0);
        return ResponseEntity.ok(restaurantService.update(id, restaurant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
