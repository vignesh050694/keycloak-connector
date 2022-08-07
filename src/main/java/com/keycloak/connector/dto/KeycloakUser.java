package com.keycloak.connector.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class KeycloakUser {
    private String id;
    private String name;
    private String email;
    private String mobileNumber;
    private Boolean isActive;
    private String userName;
    private String password;

    private List<String> roles;
    private Map<String, List<String>> attributes;
}
