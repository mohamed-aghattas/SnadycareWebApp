package com.orca.sndycareV99.dto.residence;


import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class ResidenceFinancialSummary {
    private Long residenceId;
    private BigDecimal totalLiquidity;
    private long activeAccountsCount;
}
