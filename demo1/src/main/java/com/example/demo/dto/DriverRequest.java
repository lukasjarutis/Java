package com.example.demo.dto;

import com.example.demo.entity.DriverStatus;

public class DriverRequest {

    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;

    private String vehicleNumber;
    private DriverStatus currentStatus;

    public DriverRequest() {}

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

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public DriverStatus getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(DriverStatus currentStatus) { this.currentStatus = currentStatus; }
}

