package com.orca.sndycareV99.auth.dto;


import com.orca.sndycareV99.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "The firstname is required and cannot be empty.")
    @Email
    private String email;

    @ValidPassword
    private String password;
}
