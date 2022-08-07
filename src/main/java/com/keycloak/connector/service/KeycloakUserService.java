package com.keycloak.connector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keycloak.connector.dto.KeycloakUser;
import com.keycloak.connector.exception.KeycloakException;

public interface KeycloakUserService {
    Integer createUser(KeycloakUser user) throws JsonProcessingException, KeycloakException;
}
