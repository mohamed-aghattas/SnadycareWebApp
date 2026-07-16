package com.orca.sndycareV99.controller;

import com.orca.sndycareV99.dto.residence.ResidenceRequest;
import com.orca.sndycareV99.dto.residence.ResidenceResponse;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.service.ResidenceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/residence")
@AllArgsConstructor
public class ResidenceController {

    private ResidenceService residenceService;

    @GetMapping
    public ResponseEntity<List<ResidenceResponse>> getAllResidence(){
      return ResponseEntity.ok(residenceService.getAllResidencesRolatedToUser());
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ResidenceResponse> getResidence(@PathVariable Long id){
        return ResponseEntity.ok(residenceService.getResidence(id));
    }

    @PostMapping
    public ResponseEntity<ResidenceResponse> createResidence(@Valid @RequestBody ResidenceRequest residence){
        return  ResponseEntity.ok(residenceService.createResidence(residence));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResidenceResponse> deleteResidence(@PathVariable Long id){
        residenceService.deleteResidence(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResidenceResponse> updateResidence(
            @PathVariable Long id, @Valid @RequestBody ResidenceRequest residenceRequest){
        return  ResponseEntity.ok(residenceService.updateResidence(id, residenceRequest));
    }



}
