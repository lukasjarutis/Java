package com.example.kursinisapp.model;

public class MenuItemResponse {
    private long id;
    private String name;
    private String description;
    private double currentPrice;
    private boolean available;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public boolean isAvailable() {
        return available;
    }
}
