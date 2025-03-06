package org.wcs.tripgather.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.wcs.tripgather.model.User;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private User mockUser;

    private final String secretKey = "mysecretkeymysecretkeymysecretkeymysecretkey";
    private final long expirationMs = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "expirationMs", expirationMs);

        when(mockUser.getEmail()).thenReturn("test@example.com");
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(mockUser);
        assertNotNull(token, "Token should not be null");

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("test@example.com", claims.getSubject(), "Token subject should match user email");
        assertTrue(claims.getExpiration().after(new Date()), "Token expiration should be in the future");
    }
}
