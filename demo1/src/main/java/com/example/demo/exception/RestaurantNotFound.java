package com.example.demo.exception;
public class RestaurantNotFound extends RuntimeException {
    public RestaurantNotFound(Long id) {
        super("Could not find restaurant " + id);
    }
}