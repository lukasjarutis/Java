package com.example.demo.foodapp;

public class RestaurantOwner extends User {

    private String companyName;

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
}
