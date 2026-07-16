package com.orca.sndycareV99.repository;

import com.orca.sndycareV99.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    boolean existsByChargeId(Long chargeId);

    @Query("SELECT p FROM Payment p " +
            "JOIN FETCH p.property prop " +
            "WHERE prop.building.id = :buildingId " +
            "AND p.charge.id = :chargeId")
    List<Payment> findPaymentsByBuildingAndCharge(
            @Param("buildingId") Long buildingId,
            @Param("chargeId") Long chargeId
    );
}
