package com.orca.sndycareV99.auth.controller;


import com.orca.sndycareV99.auth.dto.AuthResponse;
import com.orca.sndycareV99.auth.dto.LoginRequest;
import com.orca.sndycareV99.auth.dto.RegisterRequest;
import com.orca.sndycareV99.auth.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) throws Exception {
        return authService.createUser(request);
    }

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse login(@RequestBody LoginRequest request) throws Exception {
        return authService.authenticate(request);
    }

}
