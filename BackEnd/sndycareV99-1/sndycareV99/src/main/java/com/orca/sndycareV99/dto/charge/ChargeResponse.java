package com.orca.sndycareV99.dto.charge;


import com.orca.sndycareV99.entity.Charge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargeResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private Charge.Frequency frequency;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
