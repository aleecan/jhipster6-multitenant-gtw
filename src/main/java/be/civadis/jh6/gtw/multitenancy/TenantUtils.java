package be.civadis.jh6.gtw.multitenancy;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.civadis.jh6.gtw.security.oauth2.AuthorizationHeaderUtil;

/**
 * Permet de rechercher le tenant courant
 * Recherche dans la secu spring puis dans le TenantContext
 */
@Component
public class TenantUtils {

    public static final String TENANT_PATH_VAR = "{realm}";
    public static final String TENANT_PATH_PREFIX = "realms";

    //@Autowired
    private AuthorizationHeaderUtil authorizationHeaderUtil;

    public TenantUtils(AuthorizationHeaderUtil authorizationHeaderUtil){
        this.authorizationHeaderUtil = authorizationHeaderUtil;
    }    

    public String getTenant(){
        return Optional.ofNullable(TenantContext.getCurrentTenant()).orElse("toto");
    }


}
