package be.civadis.jh6.gtw.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.context.WebApplicationContext;

import be.civadis.jh6.gtw.security.oauth2.AudienceValidator;

/**
 * Beans spring nécessaire au multitenant
 */
@Configuration
public class MultiTenantConfig {

    private final Logger log = LoggerFactory.getLogger(MultiTenantConfig.class);

    private ClientRegistrationRepository registrations;
    private TenantUtils tenantUtils;

    public MultiTenantConfig(ClientRegistrationRepository registrations, TenantUtils tenantUtils) {
        this.registrations = registrations;
        this.tenantUtils = tenantUtils;
    }

    @Primary
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public JwtDecoder jwtDecoder(){
        log.warn("*************************************************");
        log.warn("Creating JwtDecoder for tenant " + this.tenantUtils.getTenant());
        log.warn("*************************************************");
    
        ClientRegistration registration = this.registrations.findByRegistrationId(this.tenantUtils.getTenant());

        String issuerUri = (String) registration.getProviderDetails().getConfigurationMetadata().get("issuer");
        
        NimbusJwtDecoderJwkSupport jwtDecoder = (NimbusJwtDecoderJwkSupport) JwtDecoders.fromOidcIssuerLocation(issuerUri);
        
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator();
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;

    }

}
