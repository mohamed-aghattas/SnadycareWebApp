package com.orca.sndycareV99.controller;

import com.orca.sndycareV99.dto.account.AccountRequest;
import com.orca.sndycareV99.dto.account.AccountResponse;
import com.orca.sndycareV99.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/residences/{residenceId}/accounts")
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> addAccount(
              @RequestBody AccountRequest accountRequest
            , @Valid @PathVariable Long residenceId
    ){
        return ResponseEntity.ok(accountService.addAccountToResidence(residenceId, accountRequest));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccountsInResidence(
            @PathVariable Long residenceId
    ){
        return ResponseEntity.ok(accountService.getAllAccounts(residenceId));
    }

    @GetMapping("{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId){
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }


    @PutMapping("{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @RequestBody AccountRequest accountRequest,
            @Valid @PathVariable Long accountId){

        return ResponseEntity.ok(accountService.updateAccount(accountId, accountRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AccountResponse> deleteAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequest accountRequest){
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}


