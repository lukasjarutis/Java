package org.foodapp.service;

import org.foodapp.AppUserRow;
import org.foodapp.UserRole;
import org.foodapp.exception.BadRequestException;
import org.foodapp.exception.NotFoundException;
import org.foodapp.repository.UserRepository;
import org.foodapp.web.dto.BasicUserRequest;
import org.foodapp.web.dto.UpdateUserRequest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUserRow getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    public AppUserRow updateUser(long id, UpdateUserRequest request) {
        if (request == null) {
            throw new BadRequestException("Update body is required");
        }
        AppUserRow existing = getUser(id);

        String fullName = request.getFullName();
        String email = request.getEmail();
        String phone = request.getPhone();

        if (fullName == null || fullName.isBlank()) {
            fullName = existing.getFullName();
        }
        if (email == null || email.isBlank()) {
            email = existing.getEmail();
        }
        if (phone == null || phone.isBlank()) {
            phone = existing.getPhone();
        }

        return userRepository.updateContactInfo(id, fullName, email, phone);
    }

    public AppUserRow registerBasicUser(BasicUserRequest request) {
        if (request == null) {
            throw new BadRequestException("User payload is required");
        }
        if (request.getLogin() == null || request.getLogin().isBlank()) {
            throw new BadRequestException("Login is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }

        String fullName = buildFullName(request.getName(), request.getSurname());
        String phone = request.getPhoneNumber();

        try {
            return userRepository.createUser(
                    request.getLogin(),
                    request.getPassword(),
                    fullName,
                    null,
                    phone,
                    UserRole.CUSTOMER
            );
        } catch (DuplicateKeyException ex) {
            throw new BadRequestException("User with login '" + request.getLogin() + "' already exists");
        }
    }

    private String buildFullName(String name, String surname) {
        StringBuilder builder = new StringBuilder();
        if (name != null && !name.isBlank()) {
            builder.append(name.trim());
        }
        if (surname != null && !surname.isBlank()) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(surname.trim());
        }
        String result = builder.toString();
        return result.isBlank() ? null : result;
    }
}
