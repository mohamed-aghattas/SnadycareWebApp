package com.orca.sndycareV99.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("@appSecurity")
public class GenericSecurityService {

    @PersistenceContext
    private EntityManager entityManager;

    private static final List<String> OPERATIONAL_ENTITIES = List.of(
            "Building", "Document", "Meeting", "Incident", "Reclamation", "News"
    );


    private static final List<String> FINANCIAL_ENTITIES = List.of(
            "Account", "Charge", "Expense", "Payment"
    );

    @Transactional
    public boolean hasAccessToEntity(Authentication authentication, String entityClassName, Long entityId) {
        if (authentication == null || entityClassName == null || entityId == null) {
            return false;
        }

        String currentUserName = authentication.getName();


        String currentUserRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        try {

            if ("ROLE_ADMIN".equals(currentUserRole)) {
                return true;
            }

            if (FINANCIAL_ENTITIES.contains(entityClassName) &&
                    ( "ROLE_STAFF".equals(currentUserRole) || "ROLE_TENANT".equals(currentUserRole) )) {
                return false;
            }

            String jpql;


            if ("Residence".equals(entityClassName)) {
                jpql = "SELECT COUNT(r) FROM Residence r " +
                        "WHERE r.id = :entityId " +
                        "AND (r.createdBy.email = :email " +
                        "OR :currentUserRole IN ('ROLE_MANAGER', 'ROLE_ACCOUNTANT', 'ROLE_AUDITOR'))";
            }


            else if (OPERATIONAL_ENTITIES.contains(entityClassName)) {
                jpql = "SELECT COUNT(e) FROM " + entityClassName + " e " +
                        "WHERE e.id = :entityId " +
                        "AND e.residence.createdBy.email = :email";
            }


            else if (FINANCIAL_ENTITIES.contains(entityClassName)) {
                jpql = "SELECT COUNT(e) FROM " + entityClassName + " e " +
                        "WHERE e.id = :entityId " +
                        "AND e.residence.createdBy.email = :email";
            }

            else if ("Property".equals(entityClassName)) {
                if (!"ROLE_OWNER".equals(currentUserRole) && !"ROLE_TENANT".equals(currentUserRole)) {
                    jpql = "SELECT COUNT(p) FROM Property p " +
                            "WHERE p.id = :entityId " +
                            "AND p.building.residence.createdBy.email = :email";
                } else {

                    jpql = "SELECT COUNT(po) FROM PropertyOwner po " +
                            "WHERE po.property.id = :entityId " +
                            "AND po.member.email = :email";
                }
            }


            else {
                return false;
            }


            Long count = entityManager.createQuery(jpql, Long.class)
                    .setParameter("entityId", entityId)
                    .setParameter("email", currentUserName)
                    .getSingleResult();

            return count > 0;

        } catch (Exception e) {
            return false;
        }
    }
}