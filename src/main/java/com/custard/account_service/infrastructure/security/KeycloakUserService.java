package com.custard.account_service.infrastructure.security;

import com.custard.account_service.application.commands.CreateUserCommand;
import com.custard.account_service.application.exceptions.AuthenticationException;
import com.custard.account_service.infrastructure.config.security.KeycloakProperties;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final Logger logger = LoggerFactory.getLogger(KeycloakUserService.class);
    private final Keycloak keycloak;
    private final KeycloakProperties config;

    public void createUser(CreateUserCommand command) {
        logger.info("creating keycloak user  ");
        UserRepresentation user = getUserRepresentation(command);

        try (Response response = keycloak.realm(config.getRealm()).users().create(user)) {
            logger.info("response status  -> {} ", response.getStatus());
            logger.info("response body -> {} ", response.getEntity());
        } catch (Exception e) {
            logger.error(" error creating user -> {}", e.getMessage());
            throw e;
        }
    }

    private static UserRepresentation getUserRepresentation(CreateUserCommand command) {
        CredentialRepresentation cr = new CredentialRepresentation();
        cr.setTemporary(false);
        cr.setType(CredentialRepresentation.PASSWORD);
        cr.setValue(command.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(command.getUsername());
        user.setEmail(command.getEmail());
        user.setFirstName(command.getFirstName());
        user.setLastName(command.getLastName());
        user.setEnabled(true);
        user.setCredentials(java.util.List.of(cr));
        user.setEnabled(true);
        user.setEmailVerified(true);
        return user;
    }

    public Map<String, Object> login(String username, String password) {
        try {
            Keycloak keycloakLogin = keycloakBuilder()
                    .username(username)
                    .password(password)
                    .grantType("password")
                    .build();

            AccessTokenResponse accessTokenResponse = keycloakLogin.tokenManager().grantToken();

            if (accessTokenResponse == null || accessTokenResponse.getToken() == null) {
                throw new AuthenticationException("Authentication failed: Invalid token response");
            }

            return Map.of("accessToken", accessTokenResponse.getToken(),
                    "refreshToken", accessTokenResponse.getRefreshToken(),
                    "expiresIn", accessTokenResponse.getExpiresIn());

        } catch (NotAuthorizedException e) {
            throw new AuthenticationException("Invalid username or password");
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
            throw new AuthenticationException("Authentication failed: " + e.getMessage());
        }

    }

    public Map<String, Object> refresh(String refreshToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Add logging to verify the refresh token
            logger.info("Attempting to refresh token. Refresh token: {}",
                    refreshToken != null ? refreshToken.substring(0, 10) + "..." : "null");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "refresh_token");
            params.add("client_id", config.getClientId());
            params.add("client_secret", config.getClientSecret());
            params.add("refresh_token", refreshToken);

            String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token",
                    config.getAuthServerUrl().replaceAll("/$", ""),
                    config.getRealm()
            );

            logger.info("Using token URL: {}", tokenUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            try {
                // Log the full request for debugging
                logger.debug("Sending request with params: client_id={}, grant_type=refresh_token",
                        config.getClientId());

                ResponseEntity<Map> response = restTemplate.postForEntity(
                        tokenUrl,
                        request,
                        Map.class
                );

                Map responseBody = response.getBody();
                logger.info("Token refresh successful");
                return Map.of(
                        "accessToken", responseBody.get("access_token"),
                        "refreshToken", responseBody.get("refresh_token"),
                        "expiresIn", responseBody.get("expires_in")
                );

            } catch (HttpClientErrorException e) {
                logger.error("Token refresh failed with status {}: {}",
                        e.getStatusCode(),
                        e.getResponseBodyAsString());
                throw new AuthenticationException("Failed to refresh token: " +
                        (e.getResponseBodyAsString() != null ?
                                e.getResponseBodyAsString() : e.getMessage()));
            }

        } catch (Exception e) {
            logger.error("Token refresh error", e);
            throw new AuthenticationException("Failed to refresh token: " + e.getMessage());
        }
    }

    private KeycloakBuilder keycloakBuilder() {
        return KeycloakBuilder
                .builder()
                .serverUrl(config.getAuthServerUrl())
                .realm(config.getRealm())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret());
    }

}
