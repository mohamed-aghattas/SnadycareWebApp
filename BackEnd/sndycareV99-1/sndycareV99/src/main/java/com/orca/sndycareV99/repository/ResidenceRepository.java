package com.orca.sndycareV99.repository;

import com.orca.sndycareV99.entity.Residence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Long> {
    Residence findByName( String residenceName);

    boolean existsByName( String residenceName);

    boolean existsByIdAndUserId(Long residanceId, Long userId);
}
