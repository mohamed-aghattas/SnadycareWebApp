package com.orca.sndycareV99.service;

import com.orca.sndycareV99.dto.property.PropertyRequest;
import com.orca.sndycareV99.dto.property.PropertyResponse;
import com.orca.sndycareV99.entity.Building;
import com.orca.sndycareV99.entity.Property;
import com.orca.sndycareV99.repository.BuildingRepository;
import com.orca.sndycareV99.repository.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final BuildingRepository buildingRepository;
    private final ResidanceContextService residanceContextService;


    @Transactional
    public PropertyResponse createProperty(PropertyRequest request) {
        String code = request.getPropertyCode().trim();
        Long residenceId = currentResidenceId();

        Building building = buildingRepository.findByIdAndResidenceId(request.getBuildingId(), residenceId)
                .orElseThrow(() -> new EntityNotFoundException("Building not found in this residence."));

        if (propertyRepository.existsByPropertyCode(code)) {
            throw new IllegalArgumentException("Property code '" + code + "' already exists.");
        }

        Property property = Property.builder()
                .building(building)
                .propertyCode(code)
                .propertyType(request.getPropertyType())
                .floorNumber(request.getFloorNumber())
                .area(request.getArea())
                .coownershipFee(request.getCoownershipFee() != null ? request.getCoownershipFee() : BigDecimal.ZERO)
                .status(Property.PropertyStatus.AVAILABLE)
                .build();

        Property savedProperty = propertyRepository.save(property);
        return toResponse(savedProperty);
    }


    @Transactional(readOnly = true)
    public List<PropertyResponse> getAllPropertiesByResidence() {
        return propertyRepository.findByBuildingResidenceId(currentResidenceId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<PropertyResponse> getPropertiesPaginated(int page, int size, String sortBy) {
        List<String> allowedSortFields = List.of("id", "propertyCode", "area", "coownershipFee", "createdAt");
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "id";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return propertyRepository.findByBuildingResidenceId(currentResidenceId(), pageable)
                .map(this::toResponse);
    }


    @Transactional
    public PropertyResponse updateProperty(Long propertyId, PropertyRequest request) {
        Long residenceId = currentResidenceId();
        String code = request.getPropertyCode().trim();


        Property property = propertyRepository.findByIdAndBuildingResidenceId(propertyId, residenceId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found in this residence."));


        if (!property.getPropertyCode().equalsIgnoreCase(code)) {
            if (propertyRepository.existsByPropertyCode(code)) {
                throw new IllegalArgumentException("Property code '" + code + "' already exists.");
            }
            property.setPropertyCode(code);
        }


        if (!property.getBuilding().getId().equals(request.getBuildingId())) {
            Building newBuilding = buildingRepository.findByIdAndResidenceId(request.getBuildingId(), residenceId)
                    .orElseThrow(() -> new EntityNotFoundException("Building not found in this residence."));
            property.setBuilding(newBuilding);
        }

        property.setPropertyType(request.getPropertyType());
        property.setFloorNumber(request.getFloorNumber());
        property.setArea(request.getArea());
        property.setCoownershipFee(request.getCoownershipFee());

        Property updated = propertyRepository.save(property);
        return toResponse(updated);
    }

    @Transactional
    public PropertyResponse togglePropertyStatus(Long propertyId, Property.PropertyStatus newStatus) {
        Property property = propertyRepository.findByIdAndBuildingResidenceId(propertyId, currentResidenceId())
                .orElseThrow(() -> new EntityNotFoundException("Property not found."));

        property.setStatus(newStatus);
        return toResponse(propertyRepository.save(property));
    }

    @Transactional
    public void deletePropertySafely(Long propertyId) {
        Property property = propertyRepository.findByIdAndBuildingResidenceId(propertyId, currentResidenceId())
                .orElseThrow(() -> new EntityNotFoundException("Property not found."));

        propertyRepository.delete(property);
    }


    @Transactional(readOnly = true)
    public List<PropertyResponse> getPropertiesByBuilding(Long buildingId) {
        return propertyRepository.findByBuildingIdAndBuildingResidenceId(buildingId, currentResidenceId()).stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public Map<Property.PropertyStatus, Long> getPropertiesStatusSummary() {
        return propertyRepository.findByBuildingResidenceId(currentResidenceId()).stream()
                .collect(Collectors.groupingBy(Property::getStatus, Collectors.counting()));
    }

    @Transactional(readOnly = true)
    public List<PropertyResponse> searchProperties(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllPropertiesByResidence();
        }

        Long residenceId = currentResidenceId();
        return propertyRepository.findByBuildingResidenceIdAndPropertyCodeContainingIgnoreCase(residenceId, keyword.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PropertyResponse getOneProperty(Long id){
        Property property = propertyRepository.findByIdAndBuildingResidenceId(id,currentResidenceId())
                .orElseThrow(()-> new EntityNotFoundException("Property not found"));
        return toResponse(property);
    }

    @Transactional(readOnly = true)
    public List<PropertyResponse> searchPropertiesByStatus(Property.PropertyStatus propertyStatus) {
        if (propertyStatus == null) {
            return getAllPropertiesByResidence();
        }

        Long residenceId = currentResidenceId();

        return propertyRepository.findByBuildingResidenceIdAndStatus(residenceId, propertyStatus)
                .stream()
                .map(this::toResponse)
                .toList();
    }



    private Long currentResidenceId() {
        return residanceContextService.getResidanceId();
    }

    private PropertyResponse toResponse(Property property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .buildingId(property.getBuilding().getId())
                .buildingName(property.getBuilding().getName())
                .propertyCode(property.getPropertyCode())
                .propertyType(property.getPropertyType())
                .floorNumber(property.getFloorNumber())
                .area(property.getArea())
                .coownershipFee(property.getCoownershipFee())
                .status(property.getStatus())
                .build();
    }
}