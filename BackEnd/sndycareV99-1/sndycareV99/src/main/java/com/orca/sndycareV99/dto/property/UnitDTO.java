package com.orca.sndycareV99.dto.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UnitDTO {
    Long id;
    String propertyCode;
    String name;
    String uiStatus;
}
