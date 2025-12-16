package com.example.kursinisapp.model;

import java.util.List;

public class OrderResponse {
    private long id;
    private String status;
    private double totalPrice;
    private UserSummary customer;
    private RestaurantResponse restaurant;
    private List<ChatMessageResponse> messages;

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public UserSummary getCustomer() {
        return customer;
    }

    public RestaurantResponse getRestaurant() {
        return restaurant;
    }

    public List<ChatMessageResponse> getMessages() {
        return messages;
    }

    public static class UserSummary {
        private long id;
        private String username;

        public long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
    }
}
