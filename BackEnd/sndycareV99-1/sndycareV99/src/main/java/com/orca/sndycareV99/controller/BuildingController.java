package com.orca.sndycareV99.controller;

import com.orca.sndycareV99.dto.building.BuildingRequest;
import com.orca.sndycareV99.dto.building.BuildingResponse;
import com.orca.sndycareV99.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residences/{residenceId}/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping
    public ResponseEntity<BuildingResponse> createBuilding(
            @PathVariable Long residenceId,
            @Valid @RequestBody BuildingRequest buildingRequest) {

        BuildingResponse response =
                buildingService.createBuilding(residenceId, buildingRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{buildingId}")
    public ResponseEntity<BuildingResponse> updateBuilding(
            @PathVariable Long buildingId,
            @Valid @RequestBody BuildingRequest buildingRequest) {

        return ResponseEntity.ok(
                buildingService.updateBuilding(buildingId, buildingRequest));
    }

    @GetMapping
    public ResponseEntity<List<BuildingResponse>> getAllBuildings(
            @PathVariable Long residenceId) {

        return ResponseEntity.ok(
                buildingService.getAllByResidence(residenceId));
    }

    @GetMapping("/{buildingId}")
    public ResponseEntity<BuildingResponse> getOneBuilding(
            @PathVariable Long residenceId,
            @PathVariable Long buildingId) {

        return ResponseEntity.ok(
                buildingService.getOneBuilding(residenceId, buildingId));
    }

    @DeleteMapping("/{buildingId}")
    public ResponseEntity<Void> deleteBuilding(
            @PathVariable Long buildingId) {

        buildingService.deleteBuilding(buildingId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countBuildings(
            @PathVariable Long residenceId) {

        return ResponseEntity.ok(
                buildingService.countBuildings(residenceId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BuildingResponse>> searchBuildings(
            @PathVariable Long residenceId,
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                buildingService.search(residenceId, keyword));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<BuildingResponse>> getBuildingsPage(
            @PathVariable Long residenceId,
            Pageable pageable) {

        return ResponseEntity.ok(
                buildingService.getBuildings(residenceId, pageable));
    }
}