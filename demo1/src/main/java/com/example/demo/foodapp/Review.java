package com.example.demo.foodapp;

import java.time.LocalDateTime;

public class Review {

    private Long id;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private ReviewTargetType targetType;

    private Order order;
    private User fromUser;

    private Restaurant restaurantTarget;
    private Driver driverTarget;
    private Customer customerTarget;

    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    public Review(Long id,
                  int rating,
                  String comment,
                  ReviewTargetType targetType,
                  Order order,
                  User fromUser) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.targetType = targetType;
        this.order = order;
        this.fromUser = fromUser;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReviewTargetType getTargetType() {
        return targetType;
    }

    public Order getOrder() {
        return order;
    }

    public User getFromUser() {
        return fromUser;
    }

    public Restaurant getRestaurantTarget() {
        return restaurantTarget;
    }

    public Driver getDriverTarget() {
        return driverTarget;
    }

    public Customer getCustomerTarget() {
        return customerTarget;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTargetType(ReviewTargetType targetType) {
        this.targetType = targetType;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public void setRestaurantTarget(Restaurant restaurantTarget) {
        this.restaurantTarget = restaurantTarget;
    }

    public void setDriverTarget(Driver driverTarget) {
        this.driverTarget = driverTarget;
    }

    public void setCustomerTarget(Customer customerTarget) {
        this.customerTarget = customerTarget;
    }
}
