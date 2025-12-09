package com.example.kursinisapp.foodapp;

public class Customer extends User {

    private int loyaltyPoints;
    private String defaultAddress;

    public Customer() {
        setRole(UserRole.CUSTOMER);
    }

    public Customer(Long id,
                    String username,
                    String passwordHash,
                    String fullName,
                    String email,
                    String phone,
                    int loyaltyPoints,
                    String defaultAddress) {
        super(id, username, passwordHash, fullName, email, phone, UserRole.CUSTOMER);
        this.loyaltyPoints = loyaltyPoints;
        this.defaultAddress = defaultAddress;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
