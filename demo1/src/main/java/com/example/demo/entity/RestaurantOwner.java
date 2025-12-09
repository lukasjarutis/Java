package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_owner")
@PrimaryKeyJoinColumn(name = "id")
public class RestaurantOwner extends User {

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @OneToMany(mappedBy = "owner")
    private List<Restaurant> restaurants = new ArrayList<>();

    public RestaurantOwner() {
        setRole(UserRole.OWNER);
    }

    public RestaurantOwner(Long id,
                           String username,
                           String passwordHash,
                           String fullName,
                           String email,
                           String phone,
                           String companyName) {
        super(id, username, passwordHash, fullName, email, phone, UserRole.OWNER);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
