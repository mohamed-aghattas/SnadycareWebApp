package com.orca.sndycareV99.dto.account;


import com.orca.sndycareV99.entity.Account.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class AccountRequest {

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotNull(message = "Account type is required")
    private AccountType type;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", inclusive = true,
            message = "Balance cannot be negative")
    private BigDecimal balance;
}
