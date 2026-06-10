package com.orca.sndycareV99.service;

import com.orca.sndycareV99.interfaces.BelongsToResidence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component("@appSecurity")
public class GenericSecurityService {

    @PersistenceContext
   private EntityManager entityManager;

    @Transactional
    public boolean haveAccesToEntity(Authentication authentication , String entityClassName , Long entityId) {
        if(authentication == null || entityClassName == null || entityId == null){return false;}
        String currentUserName = authentication.getName();

        try{
            Class<?> entityClass = Class.forName("com.orca.sndycareV99.entity."+entityClassName);
            Object entity = entityManager.find(entityClass, entityId);
            if(entity == null){return false;}
            if(entity instanceof BelongsToResidence){
                BelongsToResidence belongsToResidence = (BelongsToResidence)entity;

                return belongsToResidence.getResidence()
                        .getCreatedBy()
                        .getEmail()
                        .equals(currentUserName);
            }
            return false;
        }
        catch ( ClassNotFoundException e ){
            return false;
        }

    }

    @Transactional
    public boolean accesToResidence(Authentication authentication  , Long entityId) {
        return haveAccesToEntity(authentication,"Residence",entityId);
    }
}
