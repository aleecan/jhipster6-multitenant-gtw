package be.civadis.jh6.gtw.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.context.WebApplicationContext;

import be.civadis.jh6.gtw.config.ApplicationProperties;
import be.civadis.jh6.gtw.security.oauth2.AudienceValidator;

/**
 * Beans spring nécessaire au multitenant
 */
@Configuration
public class MultiTenantConfig {

    private final Logger log = LoggerFactory.getLogger(MultiTenantConfig.class);

    private ApplicationProperties applicationProperties;
    private TenantUtils tenantUtils;

    @Value("${spring.security.oauth2.client.registration.oidc.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.oidc.client-secret}")
    private String clientSecret;

    public MultiTenantConfig(ApplicationProperties applicationProperties, TenantUtils tenantUtils) {
        this.applicationProperties = applicationProperties;
        this.tenantUtils = tenantUtils;
    }

    @Primary
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public JwtDecoder jwtDecoder(TenantUtils tenantUtils){
        log.warn("*************************************************");
        log.warn("*************************************************");
        log.warn("*************************************************");
        log.warn("Creating JwtDecoder for tenant " + tenantUtils.getTenant());
        log.warn("*************************************************");
        log.warn("*************************************************");
        log.warn("*************************************************");

        String issuerUri = applicationProperties.getIssuerBaseUri() + tenantUtils.getTenant();
        
        NimbusJwtDecoderJwkSupport jwtDecoder = (NimbusJwtDecoderJwkSupport) JwtDecoders.fromOidcIssuerLocation(issuerUri);
        
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator();
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;

    }

    @Primary
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ClientRegistrationRepository clientRegistrations() {
        ClientRegistration clientRegistration = ClientRegistrations
                .fromOidcIssuerLocation(this.applicationProperties.getIssuerBaseUri() + tenantUtils.getTenant())
                .clientId(this.clientId)
                .clientSecret(this.clientSecret)
                .clientName("oidc_tenant")
                .build();
        //this.clientRegistrationRepository.findByRegistrationId("oidc").
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

}
