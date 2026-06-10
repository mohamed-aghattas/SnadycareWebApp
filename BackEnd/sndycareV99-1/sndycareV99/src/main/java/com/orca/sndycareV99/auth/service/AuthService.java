package com.orca.sndycareV99.auth.service;


import com.orca.sndycareV99.auth.dto.AuthResponse;
import com.orca.sndycareV99.auth.dto.LoginRequest;
import com.orca.sndycareV99.auth.dto.RegisterRequest;
import com.orca.sndycareV99.auth.jwt.JwtService;
import com.orca.sndycareV99.auth.repository.RoleRepository;
import com.orca.sndycareV99.auth.repository.UserRepository;
import com.orca.sndycareV99.entity.Role;
import com.orca.sndycareV99.entity.User;
import com.orca.sndycareV99.security.user.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private RoleRepository roleRepository;


    public AuthResponse authenticate(LoginRequest loginRequest) {

        try {
            System.out.println("Attempting authentication for email: " + loginRequest.getEmail()+" "+loginRequest.getPassword());

            // 1. Authenticate the credentials
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            System.out.println("Authentication successful!");

            // 2. Fetch user safely
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // 3. Generate Token
            String token = jwtService.generateToken(user);
            System.out.println("Token generated successfully.");

            return new AuthResponse(token, "Bearer", user.getEmail(), user.getRole().getName());

        } catch (BadCredentialsException e) {
            System.out.println("ERROR: Invalid email or password.");
            throw new RuntimeException("Invalid email or password");
        } catch (Exception e) {
            System.out.println("ERROR occurred during authentication: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public AuthResponse createUser( RegisterRequest registerRequest) {

        Role defaultRole = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));


        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException( "This email already exists" );
        }

        User user =  User
                .builder()
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

        return new AuthResponse(token,"Bearer",user.getEmail(),user.getRole().getName());
    }


}
