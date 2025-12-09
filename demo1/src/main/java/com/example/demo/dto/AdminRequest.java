package com.example.demo.dto;

public class AdminRequest {

    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;
    private Integer accessLevel;

    public AdminRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getAccessLevel() { return accessLevel; }
    public void setAccessLevel(Integer accessLevel) { this.accessLevel = accessLevel; }
}
