package com.keycloak.connector.security;

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomKCSBConfigResolver extends KeycloakSpringBootConfigResolver {
    private final KeycloakDeployment keycloakDeployment;


    public CustomKCSBConfigResolver(KeycloakSpringBootProperties properties) {

        this.keycloakDeployment = KeycloakDeploymentBuilder.build(properties);
    }

    public KeycloakDeployment resolve(HttpFacade.Request request){
        return this.keycloakDeployment;
    }
}
