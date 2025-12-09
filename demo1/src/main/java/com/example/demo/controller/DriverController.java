package com.example.demo.controller;

import com.example.demo.repository.DriverRepository;
import com.example.demo.entity.Driver;
import com.example.demo.entity.DriverStatus;
import com.example.demo.dto.DriverRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    @GetMapping
    public List<Driver> getAll() {
        return driverRepository.findAll();
    }

    @GetMapping("/{id}")
    public Driver getOne(@PathVariable Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver " + id + " not found"));
    }

    @PostMapping
    public Driver create(@RequestBody DriverRequest request) {

        Driver driver = new Driver();
        driver.setUsername(request.getUsername());
        driver.setPasswordHash(request.getPasswordHash());
        driver.setFullName(request.getFullName());
        driver.setEmail(request.getEmail());
        driver.setPhone(request.getPhone());
        driver.setVehicleNumber(request.getVehicleNumber());
        driver.setCurrentStatus(
                request.getCurrentStatus() != null ? request.getCurrentStatus() : DriverStatus.OFFLINE
        );

        return driverRepository.save(driver);
    }

    @PutMapping("/{id}")
    public Driver update(@PathVariable Long id, @RequestBody DriverRequest request) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver " + id + " not found"));

        if (request.getUsername() != null) driver.setUsername(request.getUsername());
        if (request.getPasswordHash() != null) driver.setPasswordHash(request.getPasswordHash());
        if (request.getFullName() != null) driver.setFullName(request.getFullName());
        if (request.getEmail() != null) driver.setEmail(request.getEmail());
        if (request.getPhone() != null) driver.setPhone(request.getPhone());
        if (request.getVehicleNumber() != null) driver.setVehicleNumber(request.getVehicleNumber());
        if (request.getCurrentStatus() != null) driver.setCurrentStatus(request.getCurrentStatus());

        return driverRepository.save(driver);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        driverRepository.deleteById(id);
    }
}
