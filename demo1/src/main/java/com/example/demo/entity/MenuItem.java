package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_item")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "base_price", nullable = false)
    private double basePrice;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public MenuItem() {
    }

    public MenuItem(Long id,
                    String name,
                    String description,
                    double basePrice,
                    double currentPrice,
                    String category,
                    boolean available,
                    Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.currentPrice = currentPrice;
        this.category = category;
        this.available = available;
        this.restaurant = restaurant;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
}
