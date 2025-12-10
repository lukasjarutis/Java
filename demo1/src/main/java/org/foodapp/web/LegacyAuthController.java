package org.foodapp.web;

import org.foodapp.AppUserRow;
import org.foodapp.service.AuthService;
import org.foodapp.web.dto.LegacyAuthRequest;
import org.foodapp.web.dto.LegacyAuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LegacyAuthController {

    private final AuthService authService;

    public LegacyAuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Legacy login endpoint kept for the Android client that still posts to /validateUser
     * with a payload {"login": "...", "password": "..."}.
     */
    @PostMapping("/validateUser")
    public ResponseEntity<LegacyAuthResponse> legacyLogin(@RequestBody LegacyAuthRequest request) {
        String username = request.getLogin() != null ? request.getLogin() : request.getUsername();
        AppUserRow user = authService.authenticate(username, request.getPassword());

        String fullName = user.getFullName() != null ? user.getFullName().trim() : "";
        String firstName = fullName.isEmpty() ? user.getUsername() : fullName.split(" ", 2)[0];
        String lastName = "";
        if (!fullName.isEmpty()) {
            String[] parts = fullName.split(" ", 2);
            if (parts.length > 1) {
                lastName = parts[1];
            }
        }

        LegacyAuthResponse response = new LegacyAuthResponse(
                user.getId(),
                user.getUsername(),
                firstName,
                lastName,
                user.getPhone()
        );

        return ResponseEntity.ok(response);
    }
}
