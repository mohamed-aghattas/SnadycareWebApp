package com.orca.sndycareV99.service;


import com.orca.sndycareV99.dto.building.BuildingRequest;
import com.orca.sndycareV99.dto.building.BuildingResponse;
import com.orca.sndycareV99.entity.Building;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.repository.BuildingRepository;
import com.orca.sndycareV99.repository.ResidenceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingService {

     private final BuildingRepository buildingRepository;
     private final ResidenceRepository residenceRepository;

     public BuildingResponse createBuilding(Long residanceId,BuildingRequest buildingRequest){

          String nameTrim = buildingRequest.getName().trim();
          boolean nameExists = buildingRepository. existsByNameIgnoreCaseAndResidenceId(
                  nameTrim,residanceId);
          if(nameExists){
               throw new IllegalArgumentException("A building with the name " + nameTrim + " already exists in this residence.");
          }

          Residence residence = residenceRepository.findById(residanceId).orElseThrow(
                  () -> new EntityNotFoundException("Residance "+residanceId+" not found")
          );

          Building building = Building.builder()
                  .name(nameTrim)
                  .floors(buildingRequest.getFloors())
                  .residence(residence)
                  .build();
          Building savedBuilding = buildingRepository.save(building);

          return toResonse(savedBuilding);

     }

     public BuildingResponse updateBuilding(Long buildingId , BuildingRequest buildingRequest){
          Building building = buildingRepository.findById(buildingId).orElseThrow(
                  ()-> new EntityNotFoundException("building id =  "+buildingId+" not found")
          );

          if (!building.getName().equalsIgnoreCase(buildingRequest.getName().trim())) {

               boolean nameExists = buildingRepository.existsByNameIgnoreCaseAndResidenceIdAndIdNot(
                       buildingRequest.getName().trim(), building.getResidence().getId(), buildingId);

               if (nameExists) {
                    throw new IllegalArgumentException("A building with the name " + buildingRequest.getName().trim() + " already exists in this residence.");
               }

          }
          building.setName(buildingRequest.getName().trim());
          building.setFloors(buildingRequest.getFloors());

          Building updatedBuilding = buildingRepository.save(building);

          return toResonse(updatedBuilding);

     }

     public List<BuildingResponse> getAllByResidence(Long residenceId){

          residenceRepository.findById(residenceId)
                  .orElseThrow(() ->
                          new EntityNotFoundException(
                                  "Residence " + residenceId + " not found"));

          return  buildingRepository.findByResidenceId(residenceId)
                  .stream()
                  .map(this::toResonse)
                  .toList();
     }

     public BuildingResponse getOneBuilding(Long residenceId, Long buildingId) {

          Building building = buildingRepository
                  .findByIdAndResidenceId(buildingId, residenceId)
                  .orElseThrow(() -> new EntityNotFoundException(
                          "Building id = " + buildingId +
                                  " not found in residence id = " + residenceId));

          return toResonse(building);
     }

     public void deleteBuilding(Long id) {

          Building building = buildingRepository.findById(id)
                  .orElseThrow(() ->
                          new EntityNotFoundException("Building id = " + id + " not found"));

          buildingRepository.delete(building);
     }

     public Long countBuildings(Long residanceId){
          return  buildingRepository.countByResidenceId(residanceId);
     }

     public List<BuildingResponse> search(Long residenceId, String keyword) {
          return buildingRepository
                  .findByNameContainingIgnoreCaseAndResidenceId(keyword, residenceId)
                  .stream()
                  .map(this::toResonse)
                  .toList();
     }

     public Page<BuildingResponse> getBuildings(
             Long residenceId,
             Pageable pageable) {

          residenceRepository.findById(residenceId)
                  .orElseThrow(() ->
                          new EntityNotFoundException(
                                  "Residence " + residenceId + " not found"));

          return buildingRepository
                  .findByResidenceId(residenceId,  pageable)
                  .map(this::toResonse);
     }



     public BuildingResponse toResonse(Building building){
         return BuildingResponse.builder()
                 .id(building.getId())
                 .name(building.getName())
                 .floors(building.getFloors())
                 .build();
     }


}
