package com.orca.sndycareV99.repository;

import com.orca.sndycareV99.dto.account.AccountResponse;
import com.orca.sndycareV99.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRespository extends JpaRepository<Account,Long> {

    List<Account> findByResidenceId(Long residenceId);
}
