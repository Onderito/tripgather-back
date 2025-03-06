package org.wcs.tripgather.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wcs.tripgather.exception.EmailAlreadyInUseException;
import org.wcs.tripgather.model.User;
import org.wcs.tripgather.service.JwtService;
import org.wcs.tripgather.service.UserService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("test@example.com");

        when(userService.register(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(savedUser);

        ResponseEntity<?> response = authController.registerUser(user);

        assertEquals(CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals(1L, ((Map<?, ?>) response.getBody()).get("id"));
    }

    @Test
    void testRegisterUser_EmailAlreadyInUse() {
        User user = new User();
        user.setEmail("duplicate@example.com");
        user.setPassword("password123");

        when(userService.register(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new EmailAlreadyInUseException("Email is already in use"));

        ResponseEntity<?> response = authController.registerUser(user);

        assertEquals(CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("Email is already in use", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void testRegisterUser_MissingPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("");

        ResponseEntity<?> response = authController.registerUser(user);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("Password is required", ((Map<?, ?>) response.getBody()).get("message"));
    }
}