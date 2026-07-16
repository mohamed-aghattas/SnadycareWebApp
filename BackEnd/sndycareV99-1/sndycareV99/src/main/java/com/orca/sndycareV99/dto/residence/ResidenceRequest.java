package com.orca.sndycareV99.dto.residence;

import com.orca.sndycareV99.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResidenceRequest {

    @NotBlank(message = "The residence name is required and cannot be empty.")
    @Size(min = 3 ,  max = 30 , message = "The residence name length must be between 3 and 30 characters.")
    private String residenceName;

    @NotBlank(message = "The residence address is required and cannot be empty.")
    @Size(min = 3 ,  max = 30 , message = "The residence address length must be between 3 and 30 characters.")
    private String address;

    @NotBlank(message = "The residence city is required and cannot be empty.")
    private String city;

    @NotNull(message = "The number of units  is required and cannot be empty.")
    @Min(1)
    private Integer numbreUnits;

}
