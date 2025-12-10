package org.foodapp.service;

import org.foodapp.Restaurant;
import org.foodapp.exception.BadRequestException;
import org.foodapp.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public List<Restaurant> list(String name, Boolean openOnly, Double minPopularity) {
        if (name == null && openOnly == null && minPopularity == null) {
            return repository.findAll();
        }
        return repository.findFiltered(name, openOnly, minPopularity);
    }

    public Restaurant get(long id) {
        return repository.findById(id).orElseThrow(() -> new org.foodapp.exception.NotFoundException("Restaurant not found: " + id));
    }

    public Restaurant create(Restaurant restaurant) {
        validate(restaurant);
        return repository.save(restaurant);
    }

    public Restaurant update(long id, Restaurant restaurant) {
        validate(restaurant);
        return repository.update(id, restaurant);
    }

    public void delete(long id) {
        repository.delete(id);
    }

    private void validate(Restaurant restaurant) {
        if (restaurant.getName() == null || restaurant.getName().isBlank()) {
            throw new BadRequestException("Restaurant name is required");
        }
        if (restaurant.getAddress() == null || restaurant.getAddress().isBlank()) {
            throw new BadRequestException("Restaurant address is required");
        }
    }
}
