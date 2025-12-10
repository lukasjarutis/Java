package org.foodapp.service;

import org.foodapp.AppUserRow;
import org.foodapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUserRow authenticate(String username, String password) {
        return userRepository.authenticate(username, password);
    }
}
