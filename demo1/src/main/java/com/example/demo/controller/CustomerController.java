package com.example.demo.controller;

import com.example.demo.repository.CustomerRepository;
import com.example.demo.entity.Customer;
import com.example.demo.dto.CustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Customer getOne(@PathVariable Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer " + id + " not found"));
    }

    @PostMapping
    public Customer create(@RequestBody CustomerRequest request) {

        Customer c = new Customer();
        c.setUsername(request.getUsername());
        c.setPasswordHash(request.getPasswordHash());
        c.setFullName(request.getFullName());
        c.setEmail(request.getEmail());
        c.setPhone(request.getPhone());
        c.setLoyaltyPoints(request.getLoyaltyPoints() != null ? request.getLoyaltyPoints() : 0);
        c.setDefaultAddress(request.getDefaultAddress());

        return customerRepository.save(c);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody CustomerRequest request) {

        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer " + id + " not found"));

        if (request.getUsername() != null) c.setUsername(request.getUsername());
        if (request.getPasswordHash() != null) c.setPasswordHash(request.getPasswordHash());
        if (request.getFullName() != null) c.setFullName(request.getFullName());
        if (request.getEmail() != null) c.setEmail(request.getEmail());
        if (request.getPhone() != null) c.setPhone(request.getPhone());
        if (request.getLoyaltyPoints() != null) c.setLoyaltyPoints(request.getLoyaltyPoints());
        if (request.getDefaultAddress() != null) c.setDefaultAddress(request.getDefaultAddress());

        return customerRepository.save(c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        customerRepository.deleteById(id);
    }
}
