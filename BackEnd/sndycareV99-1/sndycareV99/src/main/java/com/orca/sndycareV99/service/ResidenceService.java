package com.orca.sndycareV99.service;

import com.orca.sndycareV99.auth.repository.UserRepository;
import com.orca.sndycareV99.auth.service.CustomUserDetailsService;
import com.orca.sndycareV99.dto.residence.ResidenceRequest;
import com.orca.sndycareV99.dto.residence.ResidenceResponse;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.entity.User;
import com.orca.sndycareV99.exception.ResourceAlreadyExistsException;
import com.orca.sndycareV99.repository.ResidenceRepository;
import com.orca.sndycareV99.security.user.CustomUserPrincipal;
import jakarta.persistence.EntityNotFoundException;
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
    private final CurrentUserService currentUserService;

    public ResidenceResponse createResidence(ResidenceRequest request) {

        User currentUser = currentUserService.getUser();
        String cleanedName = request.getResidenceName().trim() ;
        if (residenceRepository.existsByNameIgnoreCaseAndCreatedById(cleanedName,currentUser.getId())) {
            throw new ResourceAlreadyExistsException("Residence with name "+ cleanedName +  " already exists for this user.");
        }

        Residence residence = Residence.builder()
                .name(request.getResidenceName().trim())
                .address(request.getAddress())
                .city(request.getCity())
                .totalUnits(request.getNumbreUnits())
                .createdBy(currentUser)
                .build();

        Residence savedResidence = residenceRepository.save(residence);

        return toResponse(savedResidence);
    }

    public ResidenceResponse getResidence(Long id) {

        Long currentUserId = currentUserService.getCurrentUser().getUser().getId();

        Residence residence = residenceRepository.findByIdAndCreatedBy_Id(id, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Residence not found or you don't have permission to access it."));

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

    public List<ResidenceResponse> getAllResidencesRolatedToUser() {

        Long userId = currentUserService.getCurrentUserId();

        List<Residence> residences = residenceRepository.findAllByCreatedById(userId);
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
                .id(residence.getId())
                .build();

    }







}
