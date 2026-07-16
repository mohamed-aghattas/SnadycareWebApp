package com.orca.sndycareV99.dto.property;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FloorDTO {
    private Integer floorNum;
    private String floorName;
    private List<UnitDTO> units;
}
