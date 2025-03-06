package org.wcs.tripgather.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.wcs.tripgather.model.User;
import org.wcs.tripgather.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_WhenUserExists() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserById_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateUser_WhenUserExists() {
        User existingUser = new User();
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("password");
        updatedUser.setBio("Updated bio");
        updatedUser.setCountry("Updated country");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Updated", response.getBody().getFirstName());
    }

    @Test
    void testUpdateUser_WhenUserDoesNotExist() {
        User updatedUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteUser_WhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteUser_WhenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }
}
