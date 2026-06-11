package com.orca.sndycareV99.dto.account;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AccountRequest {

    @NotBlank(message = "The account number is required and cannot be empty.")
    private String accountNumber;

    @NotBlank(message = "The account name is required and cannot be empty.")
    private String accountName;

    private BigDecimal balance ;

}
