package com.custard.account_service.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app")
public class AppInfoConfiguration {
    private String mode;
    private String message;
    private ContactDetails contactDetails = new ContactDetails();

    @Getter
    @Setter
    public static class ContactDetails{
        private String name;
    }
}
