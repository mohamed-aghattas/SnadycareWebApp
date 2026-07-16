package com.orca.sndycareV99.dto.property;

import com.orca.sndycareV99.entity.Property;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyRequest {

    @NotNull(message = "Building is required")
    private Long buildingId;

    @NotBlank(message = "Property code is required")
    private String propertyCode;

    @NotNull(message = "Property type is required")
    private Property.PropertyType propertyType;

    private Integer floorNumber;

    @NotNull(message = "Area is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Area must be greater than zero")
    private BigDecimal area;

    @DecimalMin(value = "0.0", inclusive = true,
            message = "Co-ownership fee cannot be negative")
    @Builder.Default
    private BigDecimal coownershipFee = BigDecimal.ZERO;

    @Builder.Default
    private Property.PropertyStatus status = Property.PropertyStatus.AVAILABLE;
}