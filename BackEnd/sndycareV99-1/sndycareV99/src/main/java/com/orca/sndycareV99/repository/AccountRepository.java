package com.orca.sndycareV99.repository;

import com.orca.sndycareV99.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByAccountNameIgnoreCaseAndResidenceId(String accountName, Long residanceId);
}
