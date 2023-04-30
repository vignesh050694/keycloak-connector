package com.keycloak.connector.security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class KeycloakConfiguration {
    @Bean
    @Primary
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

}
