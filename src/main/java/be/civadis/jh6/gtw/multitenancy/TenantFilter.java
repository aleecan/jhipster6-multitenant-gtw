package be.civadis.jh6.gtw.multitenancy;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * Filtre permettant d'extraire le tenant courant du token d'authentication
 */
public class TenantFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Attention, dans le cas du gateway, on doit gérer deux cas:
        // login avec tenant en param (pas de token)
        // traitement avec tenant dans le token

         //extraire le tenantId du param tenant de l'url (si url : login?tenant=tenantId)
         String[] realms = request.getParameterValues("realm");

         try {
             if (realms != null && realms.length > 0){
                 // tenant en param
                 TenantContext.setCurrentTenant(realms[0]);

             } else {

                 // extraire le token
                 HttpServletRequest httpRequest = (HttpServletRequest) request;
                 String authHeader = httpRequest.getHeader("Authorization");
                 String token = null;
                 if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ") && authHeader.length() > 7){
                     token = authHeader.substring(7);
                 }

                 //extraire le tenant du token
                 String tenant = null;
                 if (token != null && !token.isEmpty()){
                    tenant = TokenDecoder.getInstance().getTenant(token);
                 }

                 if (tenant != null){
                    TenantContext.setCurrentTenant(tenant);
                 } else {
                    TenantContext.clear();
                 }

             }

             chain.doFilter(request, response);
 
         } finally {
             TenantContext.clear();
         }

    }

}
