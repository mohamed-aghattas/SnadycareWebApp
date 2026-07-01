package com.orca.sndycareV99.repository;

import com.orca.sndycareV99.entity.Building;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building,Long> {

   Optional<Building> existsByName(String name);

    boolean existsByNameAndResidenceId(String name , Long id);

    boolean existsByNameAndResidenceIdAndIdNot(String name,  Long residenceId ,Long id);

    boolean existsByNameIgnoreCaseAndResidenceId(String nameTrim, Long residanceId);

    boolean existsByNameIgnoreCaseAndResidenceIdAndIdNot(String trim, Long id, Long buildingId);

    List<Building> findByResidenceId(Long residenceId );

    Page<Building> findByResidenceId(Long residenceId , Pageable pageable);


    Optional<Building>  findByIdAndResidenceId(Long buildingId, Long residenceId);

    Long countByResidenceId(Long residanceId);

    List<Building> findByNameContainingIgnoreCaseAndResidenceId(String keyword, Long residenceId);
}
