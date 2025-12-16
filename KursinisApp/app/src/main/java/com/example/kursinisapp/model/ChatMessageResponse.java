package com.example.kursinisapp.model;

public class ChatMessageResponse {
    private long id;
    private String text;
    private String sentAt;

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getSentAt() {
        return sentAt;
    }

    @Override
    public String toString() {
        String timestamp = sentAt != null ? sentAt : "";
        return timestamp + ": " + (text != null ? text : "");
    }
}
