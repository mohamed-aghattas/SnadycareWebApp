package com.orca.sndycareV99.serviceTest;

import com.orca.sndycareV99.dto.account.AccountRequest;
import com.orca.sndycareV99.dto.account.AccountResponse;
import com.orca.sndycareV99.dto.residence.ResidenceRequest;
import com.orca.sndycareV99.entity.Account;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.repository.AccountRespository;
import com.orca.sndycareV99.repository.ResidenceRepository;
import com.orca.sndycareV99.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private ResidenceRepository residenceRepository;

    @Mock
    private AccountRespository accountRepository;

    @InjectMocks
    private AccountService accountService;


    Residence residence;
    private AccountRequest accountRequest;
    private Account svedAccount;

    @BeforeEach
    public void setUp() {

        residence = Residence.builder()
                .id(1L)
                .name("El Nor")
                .city("tetouan")
                .totalUnits(10)
                .address("Tetouan morocco")
                .build();

        accountRequest = AccountRequest.builder()
                .accountName("CIH")
                .accountNumber("1234567890")
                .build();

        svedAccount = Account.builder()
                .residence(residence)
                .accountNumber("1234567890")
                .accountName("CIH")
                .build();

    }

    @Test
    public void createAccount() {

        when(residenceRepository.findById(1l)).thenReturn(Optional.of(residence));
        when(accountRepository.save(any(Account.class))).thenReturn(svedAccount);

        AccountResponse result = accountService
                .addAccountToResidence(residence.getId(), accountRequest);


        assertNotNull(result);
        assertEquals("CIH",result.getAccountName());
        assertEquals("1234567890",result.getAccountNumber());


    }




}
