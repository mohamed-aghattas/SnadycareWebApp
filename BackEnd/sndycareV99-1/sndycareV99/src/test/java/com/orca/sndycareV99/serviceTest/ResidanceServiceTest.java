package com.orca.sndycareV99.serviceTest;

import com.orca.sndycareV99.auth.repository.RoleRepository;
import com.orca.sndycareV99.auth.repository.UserRepository;
import com.orca.sndycareV99.dto.residence.ResidenceRequest;
import com.orca.sndycareV99.dto.residence.ResidenceResponse;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.entity.Role;
import com.orca.sndycareV99.entity.User;
import com.orca.sndycareV99.repository.ResidenceRepository;
import com.orca.sndycareV99.service.ResidenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResidanceServiceTest {

     @Mock
     private ResidenceRepository residenceRepository;
     @Mock
     private UserRepository userRepository;

     @InjectMocks
     private ResidenceService residenceService;

    private User user;
    private ResidenceRequest residenceRequest;
    private Residence savedResidence;


    @BeforeEach
    public void setUp(){
        user = User.builder()
                .id(1L)
                .email("agattasm@gmail.com")
                .firstName("Mohamed")
                .lastName("Aghattas")
                .phone("0709687643")
                .password("mohamed123!!Mm").build();

        residenceRequest = ResidenceRequest.builder()
                .residenceName("El Nor")
                .city("tetouan")
                .numbreUnits(10)
                .userId(user.getId())
                .address("Tetouan morocco")
                .build();

        savedResidence = Residence.builder()
                .name("El Nor")
                .createdBy(user)
                .totalUnits(10)
                .city("tetouan")
                .address("Tetouan morocco")
                .build();
    }


    @Test
    public void createResidanceTest_Secces(){

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(residenceRepository.save(any(Residence.class))).thenReturn(savedResidence);

        ResidenceResponse result = residenceService.createResidence(residenceRequest);

        assertNotNull(result);
        assertEquals("El Nor",result.getName());
        assertEquals("tetouan",result.getCity());
        assertEquals("Tetouan morocco",result.getAddress());
        assertEquals(10,result.getNumbreUnits());

        verify(residenceRepository, times(1)).save(any(Residence.class));
    }

    @Test
    public void getOneResidanceTest_Succes(){
        when(residenceRepository.findById(1L)).thenReturn(Optional.of(savedResidence));

        ResidenceResponse result = residenceService.getResidence(1L);

        assertNotNull(result);
        assertEquals("El Nor",result.getName());

    }
}
