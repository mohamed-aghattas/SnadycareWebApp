package com.orca.sndycareV99.dto.residence;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResidenceResponse {

    private Long id;
    private String name;
    private String address;
    private String city;
    private Integer numbreUnits;


}
