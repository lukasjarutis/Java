package org.foodapp.web;

import org.foodapp.AppUserRow;
import org.foodapp.service.UserService;
import org.foodapp.web.dto.AuthResponse;
import org.foodapp.web.dto.BasicUserRequest;
import org.foodapp.web.dto.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<AuthResponse> getUser(@PathVariable long id) {
        return ResponseEntity.ok(toResponse(userService.getUser(id)));
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<AuthResponse> updateUser(@PathVariable long id, @RequestBody UpdateUserRequest request) {
        AppUserRow updated = userService.updateUser(id, request);
        return ResponseEntity.ok(toResponse(updated));
    }

    @PostMapping("/insertBasic")
    public ResponseEntity<AuthResponse> createBasicUser(@RequestBody BasicUserRequest request) {
        AppUserRow created = userService.registerBasicUser(request);
        return ResponseEntity.ok(toResponse(created));
    }

    private AuthResponse toResponse(AppUserRow user) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}
