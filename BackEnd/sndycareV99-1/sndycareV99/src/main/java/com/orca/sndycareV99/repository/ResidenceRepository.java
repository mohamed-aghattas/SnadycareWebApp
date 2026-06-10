package com.orca.sndycareV99.repository;

import com.orca.sndycareV99.entity.Residence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Long> {
    Residence findByName( String residenceName);

    boolean existsByName( String residenceName);
}
