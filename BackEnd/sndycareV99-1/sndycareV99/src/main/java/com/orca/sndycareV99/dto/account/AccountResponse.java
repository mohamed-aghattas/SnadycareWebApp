package com.orca.sndycareV99.dto.account;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class AccountResponse {
   private Long id;
   private String accountNumber;
   private String accountName;
   private BigDecimal balance;
}
