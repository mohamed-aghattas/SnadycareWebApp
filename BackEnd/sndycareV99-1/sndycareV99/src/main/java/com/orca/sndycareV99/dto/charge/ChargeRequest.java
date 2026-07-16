package com.orca.sndycareV99.dto.charge;


import com.orca.sndycareV99.entity.Charge;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ChargeRequest {

    @NotBlank(message = "Charge name is required and cannot be empty.")
    @Size(min = 10, max = 255, message = "Charge name cannot exceed 255 characters.")
    private String name;

    private String description;

    @NotNull(message = "Charge amount is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Charge amount must be strictly greater than zero.")
    private BigDecimal amount;

    @NotNull(message = "Charge frequency (MONTHLY, QUARTERLY, YEARLY) is required.")
    private Charge.Frequency frequency;

}
