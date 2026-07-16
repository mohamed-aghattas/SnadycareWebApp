package com.orca.sndycareV99.controller;

import com.orca.sndycareV99.dto.property.PropertyRequest;
import com.orca.sndycareV99.dto.property.PropertyResponse;
import com.orca.sndycareV99.entity.Property;
import com.orca.sndycareV99.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/residence/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(@Valid @RequestBody PropertyRequest request) {
        PropertyResponse response = propertyService.createProperty(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PropertyResponse>> getAllPropertiesByResidence() {
        List<PropertyResponse> properties = propertyService.getAllPropertiesByResidence();
        return ResponseEntity.ok(properties);
    }

    @GetMapping
    public ResponseEntity<Page<PropertyResponse>> getPropertiesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Page<PropertyResponse> paginatedProperties = propertyService.getPropertiesPaginated(page, size, sortBy);
        return ResponseEntity.ok(paginatedProperties);
    }

    @GetMapping("{id}")
    public ResponseEntity<PropertyResponse> getOnePropertie(
            @PathVariable Long id

    ) {
        return ResponseEntity.ok(propertyService.getOneProperty(id));
    }

    @PutMapping("/{propertyId}")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable Long propertyId,
            @Valid @RequestBody PropertyRequest request
    ) {
        PropertyResponse response = propertyService.updateProperty(propertyId, request);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{propertyId}/status")
    public ResponseEntity<PropertyResponse> togglePropertyStatus(
            @PathVariable Long propertyId,
            @RequestParam Property.PropertyStatus newStatus
    ) {
        PropertyResponse response = propertyService.togglePropertyStatus(propertyId, newStatus);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/building/{buildingId}")
    public ResponseEntity<List<PropertyResponse>> getPropertiesByBuilding(@PathVariable Long buildingId) {
        List<PropertyResponse> properties = propertyService.getPropertiesByBuilding(buildingId);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/filter-status")
    public ResponseEntity<List<PropertyResponse>> filterPropertiesByStatus(
            @RequestParam(required = false) Property.PropertyStatus status) {

        List<PropertyResponse> properties = propertyService.searchPropertiesByStatus(status);
        return ResponseEntity.ok(properties);
    }


    @GetMapping("/summary")
    public ResponseEntity<Map<Property.PropertyStatus, Long>> getPropertiesStatusSummary() {
        Map<Property.PropertyStatus, Long> summary = propertyService.getPropertiesStatusSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PropertyResponse>> searchProperties(@RequestParam String keyword) {
        List<PropertyResponse> properties = propertyService.searchProperties(keyword);
        return ResponseEntity.ok(properties);
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long propertyId) {
        propertyService.deletePropertySafely(propertyId);
        return ResponseEntity.noContent().build();
    }
}