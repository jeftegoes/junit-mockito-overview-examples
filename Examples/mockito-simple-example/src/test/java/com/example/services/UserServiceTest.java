package com.example.services;

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
    public void saveCorrectlyTest() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(true);

        User user = userService.createUser(firstName, lastName, email, password, repeatPassword);

        assertNotNull(user, "The createUser() should not have returned null.");
        assertEquals(firstName, user.getFirstName(), "First name is incorrect.");
        assertEquals(lastName, user.getLastName(), "Last name is incorrect");
        assertEquals(email, user.getEmail(), "Email is incorrect.");
        assertNotNull(user.getId(), "User id is missing.");

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @DisplayName("When userRepository.save() returns false and throw IllegalArgumentException.")
    @Test
    public void didNotSaveCorrectlyAndThrowExceptionTest() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenThrow(RuntimeException.class);

        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(firstName, lastName, email, password, repeatPassword));

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @DisplayName("User did not save correctly.")
    @Test
    public void didNotSaveCorrectlyTest() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(false);

        assertThrows(UserServiceException.class,
                () -> userService.createUser(firstName, lastName, email, password, repeatPassword));

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }
}
