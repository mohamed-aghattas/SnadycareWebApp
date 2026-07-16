package com.orca.sndycareV99.repository;

import com.orca.sndycareV99.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property,Long> {
    boolean existsByPropertyCode(String code);

    Page<Property> findByBuildingResidenceId(Long aLong , Pageable pageable);

    List<Property> findByBuildingResidenceId(Long aLong );

    Optional<Property> findByIdAndBuildingResidenceId(Long propertyId, Long residenceId);

    List<Property> findByBuildingIdAndBuildingResidenceId(Long buildingId, Long aLong);

    List<Property> findByBuildingIdOrderByFloorNumberDescPropertyCodeAsc(Long buildingId);

    List<Property> findByBuildingResidenceIdAndPropertyCodeContainingIgnoreCase(Long residenceId, String trim);

    List<Property> findByBuildingResidenceIdAndStatus(Long residenceId, Property.PropertyStatus propertyStatus);
}
