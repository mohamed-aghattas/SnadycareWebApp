package com.orca.sndycareV99.repository;

import com.orca.sndycareV99.dto.charge.ChargeResponse;
import com.orca.sndycareV99.entity.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Repository
public interface ChargeRepository extends JpaRepository<Charge,Long> {

    @Query("SELECT SUM(c.amount) FROM Charge c WHERE c.residence.id = :residenceId " +
            "AND c.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumChargesByCycle(@Param("residenceId") Long residenceId,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(c.amount) FROM Charge c WHERE c.residence.id = :residenceId " +
            "AND c.createdAt < :cycleStart")
    BigDecimal sumOutstandingLateCharges(@Param("residenceId") Long residenceId,
                                         @Param("cycleStart") LocalDateTime cycleStart);


    Optional<Charge> findByIdAndResidenceId(Long chargeId, Long residanceId);

    List<Charge> findByResidenceId(Long residanceId);

    Page<Charge> findByResidenceId(Long residanceId , Pageable pageable);

    boolean existsByNameIgnoreCaseAndResidenceId(String chargeName, long residanceId);

    List<Charge> findByResidenceIdAndFrequency(Long aLong, Charge.Frequency frequency);



    List<Charge> findByResidenceIdAndNameContainingIgnoreCase(Long aLong, String trim);
}
