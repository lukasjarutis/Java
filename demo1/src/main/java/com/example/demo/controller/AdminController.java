package com.example.demo.controller;

import com.example.demo.repository.AdminRepository;
import com.example.demo.entity.Admin;
import com.example.demo.entity.UserRole;
import com.example.demo.dto.AdminRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    // GET /admins
    @GetMapping
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    // GET /admins/{id}
    @GetMapping("/{id}")
    public Admin getOne(@PathVariable Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin " + id + " not found"));
    }

    // POST /admins
    @PostMapping
    public Admin create(@RequestBody AdminRequest request) {

        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setPasswordHash(request.getPasswordHash());
        admin.setFullName(request.getFullName());
        admin.setEmail(request.getEmail());
        admin.setPhone(request.getPhone());
        admin.setAccessLevel(request.getAccessLevel() != null ? request.getAccessLevel() : 1);
        admin.setRole(UserRole.ADMIN);

        return adminRepository.save(admin);
    }

    // PUT /admins/{id}
    @PutMapping("/{id}")
    public Admin update(@PathVariable Long id, @RequestBody AdminRequest request) {

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin " + id + " not found"));

        if (request.getUsername() != null) admin.setUsername(request.getUsername());
        if (request.getPasswordHash() != null) admin.setPasswordHash(request.getPasswordHash());
        if (request.getFullName() != null) admin.setFullName(request.getFullName());
        if (request.getEmail() != null) admin.setEmail(request.getEmail());
        if (request.getPhone() != null) admin.setPhone(request.getPhone());
        if (request.getAccessLevel() != null) admin.setAccessLevel(request.getAccessLevel());

        return adminRepository.save(admin);
    }

    // DELETE /admins/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        adminRepository.deleteById(id);
    }
}
