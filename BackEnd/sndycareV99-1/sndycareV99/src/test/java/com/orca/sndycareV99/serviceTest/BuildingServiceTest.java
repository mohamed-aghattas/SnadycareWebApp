package com.orca.sndycareV99.serviceTest;

import com.orca.sndycareV99.dto.building.BuildingRequest;
import com.orca.sndycareV99.dto.building.BuildingResponse;
import com.orca.sndycareV99.entity.Building;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.repository.BuildingRepository;
import com.orca.sndycareV99.repository.ResidenceRepository;
import com.orca.sndycareV99.service.BuildingService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildingServiceTest {

    @Mock
    private BuildingRepository buildingRepository;

    @Mock
    private ResidenceRepository residenceRepository;

    @InjectMocks
    private BuildingService buildingService;

    private Residence residence;
    private Building building;
    private BuildingRequest request;

    @BeforeEach
    void setUp() {

        residence = Residence.builder()
                .id(1L)
                .build();

        building = Building.builder()
                .id(1L)
                .name("Tower A")
                .floors(5)
                .residence(residence)
                .build();

        request = new BuildingRequest();
        request.setName("Tower A");
        request.setFloors(5);
    }

    @Test
    void shouldCreateBuildingSuccessfully() {

        when(buildingRepository.existsByNameIgnoreCaseAndResidenceId("Tower A", 1L))
                .thenReturn(false);

        when(residenceRepository.findById(1L))
                .thenReturn(Optional.of(residence));

        when(buildingRepository.save(any(Building.class)))
                .thenReturn(building);

        BuildingResponse response = buildingService.createBuilding(1L, request);

        assertNotNull(response);
        assertEquals("Tower A", response.getName());
        assertEquals(5, response.getFloors());

        verify(buildingRepository).save(any(Building.class));
    }

    @Test
    void shouldThrowExceptionWhenBuildingAlreadyExists() {

        when(buildingRepository.existsByNameIgnoreCaseAndResidenceId("Tower A", 1L))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> buildingService.createBuilding(1L, request));

        verify(buildingRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenResidenceNotFound() {

        when(buildingRepository.existsByNameIgnoreCaseAndResidenceId("Tower A", 1L))
                .thenReturn(false);

        when(residenceRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> buildingService.createBuilding(1L, request));
    }

    @Test
    void shouldUpdateBuildingSuccessfully() {

        request.setName("Tower B");

        when(buildingRepository.findById(1L))
                .thenReturn(Optional.of(building));

        when(buildingRepository.existsByNameIgnoreCaseAndResidenceIdAndIdNot(
                "Tower B",
                1L,
                1L))
                .thenReturn(false);

        Building updated = Building.builder()
                .id(1L)
                .name("Tower B")
                .floors(5)
                .residence(residence)
                .build();

        when(buildingRepository.save(any(Building.class)))
                .thenReturn(updated);

        BuildingResponse response = buildingService.updateBuilding(1L, request);

        assertEquals("Tower B", response.getName());

        verify(buildingRepository).save(any(Building.class));
    }

    @Test
    void shouldThrowExceptionWhenBuildingNotFoundForUpdate() {

        when(buildingRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> buildingService.updateBuilding(1L, request));
    }

    @Test
    void shouldNotCheckDuplicateWhenNameIsUnchanged() {

        when(buildingRepository.findById(1L))
                .thenReturn(Optional.of(building));

        when(buildingRepository.save(any(Building.class)))
                .thenReturn(building);

        buildingService.updateBuilding(1L, request);

        verify(buildingRepository, never())
                .existsByNameIgnoreCaseAndResidenceIdAndIdNot(any(), any(), any());

        verify(buildingRepository).save(any(Building.class));
    }

    @Test
    void shouldDeleteBuilding() {

        when(buildingRepository.findById(1L))
                .thenReturn(Optional.of(building));

        buildingService.deleteBuilding(1L);

        verify(buildingRepository).delete(building);
    }

    @Test
    void shouldThrowExceptionWhenDeletingUnknownBuilding() {

        when(buildingRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> buildingService.deleteBuilding(1L));
    }

    @Test
    void shouldReturnOneBuilding() {

        when(buildingRepository.findByIdAndResidenceId(1L, 1L))
                .thenReturn(Optional.of(building));

        BuildingResponse response =
                buildingService.getOneBuilding(1L, 1L);

        assertEquals("Tower A", response.getName());
    }

    @Test
    void shouldThrowExceptionWhenBuildingNotFound() {

        when(buildingRepository.findByIdAndResidenceId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> buildingService.getOneBuilding(1L, 1L));
    }

    @Test
    void shouldCountBuildings() {

        when(buildingRepository.countByResidenceId(1L))
                .thenReturn(5L);

        Long count = buildingService.countBuildings(1L);

        assertEquals(5L, count);
    }

    @Test
    void shouldSearchBuildings() {

        when(buildingRepository.findByNameContainingIgnoreCaseAndResidenceId(
                "Tower",
                1L))
                .thenReturn(List.of(building));

        List<BuildingResponse> result =
                buildingService.search(1L, "Tower");

        assertEquals(1, result.size());
        assertEquals("Tower A", result.get(0).getName());
    }

    @Test
    void shouldReturnBuildingsPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Building> page = new PageImpl<>(List.of(building));

        when(residenceRepository.findById(1L))
                .thenReturn(Optional.of(residence));

        when(buildingRepository.findByResidenceId(1L, pageable))
                .thenReturn(page);

        Page<BuildingResponse> result =
                buildingService.getBuildings(1L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Tower A", result.getContent().get(0).getName());
    }
}