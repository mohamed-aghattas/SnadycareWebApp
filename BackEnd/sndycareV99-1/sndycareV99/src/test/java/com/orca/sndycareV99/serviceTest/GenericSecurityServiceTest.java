package com.orca.sndycareV99.serviceTest;


import com.orca.sndycareV99.service.GenericSecurityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenericSecurityServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> typedQuery;

    @InjectMocks
    private GenericSecurityService genericSecurityService;

    private Authentication adminAuth;
    private Authentication managerAuth;

    @BeforeEach
    void setUp() {

        adminAuth = new UsernamePasswordAuthenticationToken(
                "admin@syndicare.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        managerAuth = new UsernamePasswordAuthenticationToken(
                "manager@syndicare.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_MANAGER"))
        );
    }

    @Test
    void shouldGrantAccess_WhenUserIsAdmin() {
        boolean result = genericSecurityService.hasAccessToEntity(adminAuth, "Residence", 1L);
        assertTrue(result);
    }

    @Test
    void shouldGrantAccess_WhenManagerOwnsTheResidence() {
        when(entityManager.createQuery(any(String.class), eq(Long.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(any(String.class), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(1L);

        boolean result = genericSecurityService.hasAccessToEntity(managerAuth, "Residence", 1L);

        assertTrue(result);
    }

    @Test
    void shouldDenyAccess_WhenManagerDoesNotOwnTheResidence() {
        when(entityManager.createQuery(any(String.class), eq(Long.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(any(String.class), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(0L);

        boolean result = genericSecurityService.hasAccessToEntity(managerAuth, "Residence", 99L);

        assertFalse(result);
    }

    @Test
    void shouldDenyAccess_WhenEntityIsNotInWhiteList() {
        boolean result = genericSecurityService.hasAccessToEntity(managerAuth, "InvalidEntityName", 1L);

        assertFalse(result);
    }
}
