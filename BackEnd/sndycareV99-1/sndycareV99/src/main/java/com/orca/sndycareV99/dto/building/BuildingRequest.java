package com.orca.sndycareV99.dto.building;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuildingRequest {


    @NotBlank(message = "Building name cannot be blank")
    private String name;

    @Min(value = 1, message = "Floors must be at least 1")
    private Integer floors;

}
