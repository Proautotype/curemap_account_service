package com.custard.account_service.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakClient {

    private final KeycloakProperties keycloakConfig;

    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .realm(keycloakConfig.getRealm())
                .serverUrl(keycloakConfig.getAuthServerUrl())
                .clientId(keycloakConfig.getClientId())
                .clientSecret(keycloakConfig.getClientSecret())
                .username(keycloakConfig.getAdminUsername())
                .password(keycloakConfig.getAdminPassword())
                .build();
    }

}

