package com.orca.sndycareV99.dto.property;


import com.orca.sndycareV99.entity.Property;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyResponse {

    private Long id;

    private Long buildingId;

    private String propertyCode;

    private Property.PropertyType propertyType;

    private Integer floorNumber;

    private BigDecimal area;

    private BigDecimal coownershipFee;

    private Property.PropertyStatus status;

    private String buildingName;
}
