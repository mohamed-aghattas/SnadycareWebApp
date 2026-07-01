package com.orca.sndycareV99.service;

import com.orca.sndycareV99.auth.repository.UserRepository;
import com.orca.sndycareV99.dto.residence.ResidenceRequest;
import com.orca.sndycareV99.dto.residence.ResidenceResponse;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.entity.User;
import com.orca.sndycareV99.repository.ResidenceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResidenceService {

    private final ResidenceRepository residenceRepository;
    private final UserRepository userRepository;

    public ResidenceResponse createResidence(ResidenceRequest residenceRequest) {
        User user ;
        if(residenceRequest.getUserId() !=null){
             user = userRepository.findById(residenceRequest.getUserId()).orElseThrow(
                    () -> new UsernameNotFoundException("User not found"));
        }else {
            throw new UsernameNotFoundException("User not found");
        }

        if(residenceRepository.existsByName(residenceRequest.getResidenceName())){
            throw  new UsernameNotFoundException("Change name of new residence ");

        }

        Residence residence = Residence.builder()
                .name(residenceRequest.getResidenceName())
                .address(residenceRequest.getAddress())
                .city(residenceRequest.getCity())
                .totalUnits(residenceRequest.getNumbreUnits())
                .createdBy(user).build();
        Residence newResidence = residenceRepository.save(residence);

        return toResponse(newResidence);
    }

    public ResidenceResponse getResidence(Long id) {
        if(id == null)
            throw new UsernameNotFoundException("Residence not found");

        Residence   residence = residenceRepository.findById(id).orElseThrow();

        return toResponse(residence);
    }
    public void  deleteResidence(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Residence ID must not be null");
        if(!residenceRepository.existsById(id))
            throw new UsernameNotFoundException("Residence not found");

        residenceRepository.deleteById(id);

    }

    public ResidenceResponse updateResidence(Long id, ResidenceRequest residenceRequest) {

        Residence residence =  residenceRepository.findById(id).orElseThrow() ;
        Residence updatedResidence;
        if(residenceRequest != null){
            residence.setAddress(residenceRequest.getAddress());
            residence.setCity(residenceRequest.getCity());
            residence.setTotalUnits(residenceRequest.getNumbreUnits());
            residence.setName(residenceRequest.getResidenceName());

             updatedResidence = residenceRepository.save(residence);

        }
        else{
            throw new UsernameNotFoundException("Residence not found");
        }

        return toResponse(updatedResidence);
    }

    public List<ResidenceResponse> getAllResidences() {

        List<Residence> residences = residenceRepository.findAll();
        return residences
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ResidenceResponse toResponse(Residence residence) {
        return ResidenceResponse.builder()
                .numbreUnits(residence.getTotalUnits())
                .city(residence.getCity())
                .address(residence.getAddress())
                .name(residence.getName())
                .build();

    }







}
