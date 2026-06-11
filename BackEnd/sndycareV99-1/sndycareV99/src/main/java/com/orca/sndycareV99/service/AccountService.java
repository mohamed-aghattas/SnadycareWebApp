package com.orca.sndycareV99.service;


import com.orca.sndycareV99.dto.account.AccountRequest;
import com.orca.sndycareV99.dto.account.AccountResponse;
import com.orca.sndycareV99.entity.Account;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.repository.AccountRespository;
import com.orca.sndycareV99.repository.ResidenceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRespository accountRespository;
    private final ResidenceRepository residenceRepository;


    @Transactional
    public AccountResponse addAccountToResidence(Long residanceId  ,AccountRequest accountRequest) {
        Residence residence = residenceRepository.findById(residanceId)
                .orElseThrow(() -> new EntityNotFoundException("Residence not found"));

        Account account = Account.builder()
                .accountName(accountRequest.getAccountName())
                .accountNumber(accountRequest.getAccountNumber())
                .residence(residence)
                .build();

        residence.addAcount(account);
        Account savedAccount = accountRespository.save(account);

    return toResponse(savedAccount);
    }

    @Transactional
    public AccountResponse updateAccount(Long accountId ,AccountRequest accountRequest) {
        Account existingAccount = accountRespository.findById(accountId).orElseThrow(
                (() -> new EntityNotFoundException("Residence not found"))
        );

        existingAccount.setAccountName(accountRequest.getAccountName());
        existingAccount.setAccountNumber(accountRequest.getAccountNumber());

        Account updatedAccount = accountRespository.save(existingAccount);

        return toResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        if(!accountRespository.existsById(accountId)) {
            throw new EntityNotFoundException("Account not found with id: " + accountId);
        }

        accountRespository.deleteById(accountId);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts(Long residenceId) {
        return accountRespository.findByResidenceId(residenceId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRespository.findById(accountId).orElseThrow(
                (() -> new EntityNotFoundException("Account not found"))
        );

        return toResponse(account);
    }

    @Transactional
    private AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .accountName(account.getAccountName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .id(account.getId())
                .build();
    }

}
