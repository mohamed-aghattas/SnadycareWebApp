package com.orca.sndycareV99.auth.service;

import com.orca.sndycareV99.auth.dto.AuthResponse;
import com.orca.sndycareV99.auth.dto.LoginRequest;
import com.orca.sndycareV99.auth.dto.RegisterRequest;
import com.orca.sndycareV99.auth.jwt.JwtService;
import com.orca.sndycareV99.auth.repository.RoleRepository;
import com.orca.sndycareV99.auth.repository.UserRepository;
import com.orca.sndycareV99.entity.Role;
import com.orca.sndycareV99.entity.User;
import com.orca.sndycareV99.exception.ResourceAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public AuthResponse authenticate(LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );


            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail()));


            String token = jwtService.generateToken(user);

            return new AuthResponse(token, "Bearer", user.getEmail(), user.getRole().getName());

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Transactional
    public AuthResponse createUser(RegisterRequest registerRequest) {

        Role defaultRole = roleRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Error: Default Role is not found in database."));


        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("This email is already registered in the system.");
        }


        User user = User.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(defaultRole)
                .active(true)
                .phone(registerRequest.getPhone())
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(token, "Bearer", savedUser.getEmail(), savedUser.getRole().getName());
    }
}