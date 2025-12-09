package com.example.demo.foodapp;

import java.time.LocalDateTime;

public class ChatMessage {

    private Long id;
    private String text;
    private LocalDateTime sentAt;

    private Order order;
    private User fromUser;
    private User toUser;

    public ChatMessage() {
        this.sentAt = LocalDateTime.now();
    }

    public ChatMessage(Long id,
                       String text,
                       Order order,
                       User fromUser,
                       User toUser) {
        this.id = id;
        this.text = text;
        this.order = order;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public Order getOrder() {
        return order;
    }

    public User getFromUser() {
        return fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
}
