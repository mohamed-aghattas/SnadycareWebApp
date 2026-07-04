package com.orca.sndycareV99.service;


import com.orca.sndycareV99.helper.CurrentResidence;
import com.orca.sndycareV99.repository.ResidenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class ResidanceContextService {

    private final CurrentResidence currentResidence;
    private final ResidenceRepository residenceRepository;

    public void initialize(Long residanceId , Long userId) throws AccessDeniedException {

        boolean allowed =
                residenceRepository.existsByIdAndUserId(
                        residanceId,
                        userId);

        if(!allowed){
            throw new AccessDeniedException("Access denied.");
        }

        currentResidence.setId(residanceId);

    }

    public long getResidanceId(){
        return currentResidence.getId();
    }


}
