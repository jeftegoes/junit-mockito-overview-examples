package com.example.services;

import com.example.exceptions.EmailNotificationServiceException;
import com.example.exceptions.UserServiceException;
import com.example.models.User;
import com.example.repositories.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepositoryImpl userRepository;

    @Mock
    EmailVerificationServiceImpl emailVerificationService;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String repeatPassword;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository, emailVerificationService);
        firstName = "Jefté";
        lastName = "Goes";
        email = "test@test.com";
        password = "123456789";
        repeatPassword = "123456789";
    }

    @DisplayName("User object created.")
    @Test
    public void testCreateUser_whenUserDetailsProvided_returnsUserObject() {
        when(userRepository.save(any(User.class))).thenReturn(true);

        User user = userService.createUser(firstName, lastName, email, password, repeatPassword);

        assertNotNull(user, "The createUser() should not have returned null.");
        assertEquals(firstName, user.getFirstName(), "First name is incorrect.");
        assertEquals(lastName, user.getLastName(), "Last name is incorrect");
        assertEquals(email, user.getEmail(), "Email is incorrect.");
        assertNotNull(user.getId(), "User id is missing.");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("Empty first name causes correct exception")
    @Test
    void testCreateUser_whenFirstNameIsEmpty_throwsIllegalArgumentException() {
        String firstName = "";
        String expectedMessage = "First name is empty.";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.createUser(firstName, lastName, email, password, repeatPassword));
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @DisplayName("Null first name causes correct exception")
    @Test
    void testCreateUser_whenFirstNameIsNull_throwsIllegalArgumentException() {
        String firstName = null;
        String expectedMessage = "First name is empty.";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.createUser(firstName, lastName, email, password, repeatPassword));
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @DisplayName("Empty last name causes correct exception")
    @Test
    void testCreateUser_whenLastNameIsEmpty_throwsIllegalArgumentException() {
        String lastName = "";
        String expectedMessage = "Last name is empty.";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.createUser(firstName, lastName, email, password, repeatPassword));
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @DisplayName("Empty last name causes correct exception")
    @Test
    void testCreateUser_whenLastNameIsNull_throwsIllegalArgumentException() {
        String lastName = null;
        String expectedMessage = "Last name is empty.";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.createUser(firstName, lastName, email, password, repeatPassword));
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @DisplayName("If save() method causes RuntimeException, a UserServiceException is thrown")
    @Test
    void testCreateUser_whenSaveMethodThrowsException_thenThrowsUserServiceException() {
        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);

        assertThrows(UserServiceException.class, () -> {
            userService.createUser(firstName, lastName, email, password, repeatPassword);
        }, "Should have thrown UserServiceException instead");
    }

    @DisplayName("User did not save correctly.")
    @Test
    public void testCreateUser_didNotSaveCorrectly() {
        when(userRepository.save(any(User.class))).thenReturn(false);

        assertThrows(UserServiceException.class,
                () -> userService.createUser(firstName, lastName, email, password, repeatPassword));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("EmailNotificationException is handled")
    @Test
    void testCreateUser_whenEmailNotificationExceptionThrown_throwsUserServiceException() {
        when(userRepository.save(any(User.class))).thenReturn(true);

        doThrow(EmailNotificationServiceException.class).when(emailVerificationService).scheduleEmailConfirmation(any(User.class));

        assertThrows(EmailNotificationServiceException.class, () -> userService.createUser(firstName, lastName, email, password, repeatPassword));

        verify(emailVerificationService, times(1)).scheduleEmailConfirmation(any(User.class));
    }

    @DisplayName("Schedule Email Confirmation is executed")
    @Test
    void testCreateUser_whenUserCreated_schedulesEmailConfirmation() {
        when(userRepository.save(any(User.class))).thenReturn(true);

        doCallRealMethod().when(emailVerificationService).scheduleEmailConfirmation(any(User.class));

        User user = userService.createUser(firstName, lastName, email, password, repeatPassword);

        verify(emailVerificationService, times(1)).scheduleEmailConfirmation(any(User.class));
    }
}
