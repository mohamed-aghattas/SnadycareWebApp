package com.orca.sndycareV99.dto.building;

import com.orca.sndycareV99.dto.property.FloorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class BuildingLayoutResponse {

    Long id;
    String name;
    int floor;
    BuildingStats buildingStats;
    List<FloorDTO> floorDTOList;
}
