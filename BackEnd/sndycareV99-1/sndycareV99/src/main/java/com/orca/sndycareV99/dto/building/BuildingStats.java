package com.orca.sndycareV99.dto.building;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class BuildingStats {
    long totalPaid;
    long totalUnpaid;
    long totalEmpty;
}
