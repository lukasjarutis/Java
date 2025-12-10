package org.foodapp;

public class Driver extends User{

    private String vehicleNumber;
    private DriverStatus currentStatus;

    public Driver() {
        setRole(UserRole.DRIVER);
        this.currentStatus = DriverStatus.OFFLINE;
    }

    public Driver(Long id,
                  String username,
                  String passwordHash,
                  String fullName,
                  String email,
                  String phone,
                  String vehicleNumber,
                  DriverStatus currentStatus) {
        super(id, username, passwordHash, fullName, email, phone, UserRole.DRIVER);
        this.vehicleNumber = vehicleNumber;
        this.currentStatus = currentStatus;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public DriverStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(DriverStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
