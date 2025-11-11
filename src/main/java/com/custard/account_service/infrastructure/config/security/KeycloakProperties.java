package com.custard.account_service.infrastructure.config.security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "keycloak")
@Getter
@Setter
@ToString
public class KeycloakProperties {

    private final Logger logger = LoggerFactory.getLogger(KeycloakProperties.class);

    private String authServerUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String adminUsername;
    private String adminPassword;


    @PostConstruct
    public void print() {
        logger.info("keycloak details {} ", this);
    }

}
