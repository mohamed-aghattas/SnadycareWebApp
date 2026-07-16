package com.orca.sndycareV99.dto.account;

import com.orca.sndycareV99.entity.Account.AccountType;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
    private AccountType accountType;
    private Boolean isActive;
}
