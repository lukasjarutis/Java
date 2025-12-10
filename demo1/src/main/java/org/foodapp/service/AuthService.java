package org.foodapp.service;

import org.foodapp.AppUserRow;
import org.foodapp.exception.AuthenticationException;
import org.foodapp.UserRole;
import org.foodapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUserRow authenticate(String username, String password) {
        return authenticate(username, password, new UserRole[0]);
    }

    public AppUserRow authenticate(String username, String password, UserRole... allowedRoles) {
        AppUserRow user = userRepository.authenticate(username, password);

        if (allowedRoles != null && allowedRoles.length > 0) {
            UserRole role = UserRole.valueOf(user.getRole());
            for (UserRole allowed : allowedRoles) {
                if (allowed == role) {
                    return user;
                }
            }
            throw new AuthenticationException("Vaidmuo nėra leidžiamas šioje aplikacijoje");
        }

        return user;
    }
}
