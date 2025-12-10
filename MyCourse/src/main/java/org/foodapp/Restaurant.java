package org.foodapp;

import java.util.List;
import java.util.ArrayList;

public class Restaurant {

    private Long id;
    private String name;
    private String address;
    private boolean open;
    private double popularityScore;

    private RestaurantOwner owner;
    private List<MenuItem>  menuItems = new ArrayList<>();

    public Restaurant() {}

    public Restaurant(Long id,
                      String name,
                      String address,
                      boolean open,
                      double popularityScore,
                      RestaurantOwner owner) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.open = open;
        this.popularityScore = popularityScore;
        this.owner = owner;
    }

    public void addMenuItem(MenuItem item) {
        if (item == null) {
            return;
        }
        if (!menuItems.contains(item)) {
            menuItems.add(item);
        }
        item.setRestaurant(this);
    }

    public void removeMenuItem(MenuItem item) {
        menuItems.remove(item);
        item.setRestaurant(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public double getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(double popularityScore) {
        this.popularityScore = popularityScore;
    }

    public RestaurantOwner getOwner() {
        return owner;
    }

    public void setOwner(RestaurantOwner owner) {
        this.owner = owner;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
