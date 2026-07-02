package com.orca.sndycareV99.service;


import com.orca.sndycareV99.dto.account.AccountRequest;
import com.orca.sndycareV99.dto.account.AccountResponse;
import com.orca.sndycareV99.entity.Account;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.repository.AccountRepository;
import com.orca.sndycareV99.repository.ResidenceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ResidenceRepository residenceRepository;


    public AccountResponse createAccount(Long residanceId , AccountRequest accountRequest){

            String accountName = accountRequest.getAccountName().trim();
            String accountNumber = accountRequest.getAccountNumber().trim().replace(" ","");

            boolean isExists = accountRepository.existsByAccountNameIgnoreCaseAndResidenceId(accountName,residanceId);

            if(isExists){
                throw new IllegalArgumentException("A account with the name " + accountName + " already exists in this residence.");
            }

            Residence residence = getResidence(residanceId);

            Account account = Account.builder()
                    .accountName(accountRequest.getAccountName())
                    .accountNumber(accountRequest.getAccountNumber())
                    .type(accountRequest.getType())
                    .balance(accountRequest.getBalance()).build();

            Account savedAccount = accountRepository.save(account);

            return  toResponse(savedAccount);

    }

//    public AccountResponse updateAccount(Long accountId ,AccountRequest accountRequest){
//
//        String accountName = accountRequest.getAccountName().trim();
//        String accountNumber = accountRequest.getAccountNumber().trim().replace(" ","");
//
//        Account account = accountRepository.findById(accountId).orElseThrow(
//                () -> new EntityNotFoundException("Account Not Found")
//        );
//
////        if(account.getAccountName().equalsIgnoreCase(accountName))
//    }

    private Residence getResidence(Long residenceId) {
        return residenceRepository.findById(residenceId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Residence not found"));
    }

    private AccountResponse toResponse(Account account){

        return  AccountResponse.builder()
                .accountName(account.getAccountName())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getType())
                .id(account.getId())
                .balance(account.getBalance())
                .build();

    }
}
