package com.example.demo.controller;

import com.example.demo.entity.MenuItem;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.entity.Restaurant;
import com.example.demo.dto.MenuItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Все пункты меню
    @GetMapping
    public List<MenuItem> getAll() {
        return menuItemRepository.findAll();
    }

    // Пункты меню конкретного ресторана
    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItem> getByRestaurant(@PathVariable Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    // Один пункт меню
    @GetMapping("/{id}")
    public MenuItem getOne(@PathVariable Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuItem " + id + " not found"));
    }

    // Создать пункт меню
    @PostMapping
    public MenuItem create(@RequestBody MenuItemRequest request) {

        if (request.getRestaurantId() == null) {
            throw new IllegalArgumentException("restaurantId is required");
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant " + request.getRestaurantId() + " not found"));

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setBasePrice(request.getBasePrice() != null ? request.getBasePrice() : 0.0);
        item.setCurrentPrice(request.getCurrentPrice() != null ? request.getCurrentPrice() : item.getBasePrice());
        item.setCategory(request.getCategory());
        item.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        item.setRestaurant(restaurant);

        return menuItemRepository.save(item);
    }

    // Обновить
    @PutMapping("/{id}")
    public MenuItem update(@PathVariable Long id,
                           @RequestBody MenuItemRequest request) {

        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuItem " + id + " not found"));

        if (request.getName() != null) item.setName(request.getName());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getBasePrice() != null) item.setBasePrice(request.getBasePrice());
        if (request.getCurrentPrice() != null) item.setCurrentPrice(request.getCurrentPrice());
        if (request.getCategory() != null) item.setCategory(request.getCategory());
        if (request.getAvailable() != null) item.setAvailable(request.getAvailable());

        if (request.getRestaurantId() != null) {
            Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("Restaurant " + request.getRestaurantId() + " not found"));
            item.setRestaurant(restaurant);
        }

        return menuItemRepository.save(item);
    }

    // Удалить
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        menuItemRepository.deleteById(id);
    }
}
