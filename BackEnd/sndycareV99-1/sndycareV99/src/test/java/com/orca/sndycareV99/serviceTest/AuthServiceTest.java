package com.orca.sndycareV99.serviceTest;

import com.orca.sndycareV99.auth.dto.AuthResponse;
import com.orca.sndycareV99.auth.dto.LoginRequest;
import com.orca.sndycareV99.auth.dto.RegisterRequest;
import com.orca.sndycareV99.auth.jwt.JwtService;
import com.orca.sndycareV99.auth.repository.RoleRepository;
import com.orca.sndycareV99.auth.repository.UserRepository;
import com.orca.sndycareV99.auth.service.AuthService;
import com.orca.sndycareV99.entity.Role;
import com.orca.sndycareV99.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;
    @InjectMocks private  AuthService authService;

    @Test
    void testAuthenticate_Success() {
        // 1. Arrange
        LoginRequest request = new LoginRequest("test@test.com", "password");
        User mockUser = User.builder().email("test@test.com").role(new Role("USER")).build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any())).thenReturn("fake-jwt-token");

        // 2. Act
        AuthResponse response = authService.authenticate(request);

        // 3. Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
    }

}
