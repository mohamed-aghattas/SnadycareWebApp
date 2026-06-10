package com.orca.sndycareV99.auth.dto;

import com.orca.sndycareV99.annotation.ValidPassword;
import com.orca.sndycareV99.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "The firstname is required and cannot be empty.")
    @Size(min = 3 ,  max = 30 , message = "The name length must be between 3 and 30 characters.")
    private String firstName;

    @NotBlank(message = "the lastname is required and cannot be empty ")
    @Size(min = 3 , max = 30 , message = "The lastname length must be between 3 and 30 characters.")
    private String lastName;

    @NotBlank(message = "The firstname is required and cannot be empty.")
    @Email
    private String email;

    @ValidPassword
    private String password;

    @NotBlank(message = "The phone is required and cannot be empty")
    @Size(min = 10,max = 10 , message = "The phone lenght is 10.")
    private String phone;


}
