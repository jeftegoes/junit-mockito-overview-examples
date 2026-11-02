package com.example.exceptions;

public class EmailNotificationServiceException extends RuntimeException {
    public EmailNotificationServiceException(String message) {
        super(message);
    }
}
