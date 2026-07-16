package com.orca.sndycareV99.controller;

import com.orca.sndycareV99.dto.account.AccountRequest;
import com.orca.sndycareV99.dto.account.AccountResponse;
import com.orca.sndycareV99.dto.residence.ResidenceFinancialSummary;
import com.orca.sndycareV99.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/residence/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountRequest));
    }


    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccountsByResidance());
    }


    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody AccountRequest accountRequest) {
        return ResponseEntity.ok(accountService.updateAccount(accountId, accountRequest));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getOneAccount(@PathVariable Long accountId) {
         return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/transfer")
    public ResponseEntity<Void> transferFunds(
            @RequestParam Long sourceAccountId,
            @RequestParam Long targetAccountId,
            @RequestParam BigDecimal amount) {
        accountService.transferFunds(sourceAccountId, targetAccountId, amount);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{accountId}/toggle-status")
    public ResponseEntity<Void> toggleAccountStatus(
            @PathVariable Long accountId,
            @RequestParam boolean isActive) {
        accountService.toggleAccountStatus(accountId, isActive);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/summary")
    public ResponseEntity<ResidenceFinancialSummary> getFinancialSummary() {
        return ResponseEntity.ok(accountService.getResidenceFinancialSummary());
    }
}