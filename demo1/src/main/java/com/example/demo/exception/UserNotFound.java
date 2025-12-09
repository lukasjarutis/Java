package com.example.demo.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound(Integer id) {
        super("Could not find user " + id);
    }
}