package com.example.services;

import com.example.models.User;

public interface EmailVerificationService {
    void scheduleEmailConfirmation(User user);
}
