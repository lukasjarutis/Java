package com.example.kursinisapp.model;

import java.util.List;

public class RestaurantResponse {
    private long id;
    private String name;
    private String address;
    private boolean open;
    private double popularityScore;
    private List<MenuItemResponse> menuItems;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public boolean isOpen() {
        return open;
    }

    public double getPopularityScore() {
        return popularityScore;
    }

    public List<MenuItemResponse> getMenuItems() {
        return menuItems;
    }
}
