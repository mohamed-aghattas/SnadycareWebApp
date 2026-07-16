package com.orca.sndycareV99.dto.account;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountFinancialSummary {

    private BigDecimal totalBalance;
    private BigDecimal chargeThisCycle;
    private double collectionRate;
    private BigDecimal outstandingLate;

}
