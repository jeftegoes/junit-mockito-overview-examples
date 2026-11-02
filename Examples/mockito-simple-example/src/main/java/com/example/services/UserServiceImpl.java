package com.example.services;

import com.example.exceptions.EmailNotificationServiceException;
import com.example.exceptions.UserServiceException;
import com.example.models.User;
import com.example.repositories.UserRepository;

import java.util.UUID;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;

    public UserServiceImpl(UserRepository userRepository,
                           EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public User createUser(String firstName, String lastName, String email, String password, String repeatPassword) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is empty.");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is empty.");
        }

        User user = new User(UUID.randomUUID().toString(), firstName, lastName, email);

        boolean isUserCreated;

        try {
            isUserCreated = this.userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new UserServiceException(ex.getMessage());
        }

        if (!isUserCreated)
            throw new UserServiceException("Could not create user.");

        try {
            this.emailVerificationService.scheduleEmailConfirmation(user);
        } catch (RuntimeException ex) {
            throw new EmailNotificationServiceException(ex.getMessage());
        }

        return user;
    }
}
