package com.keycloak.connector.security;

import lombok.Data;

@Data
public class CurrentUser {
    private String userId;
    private String userName;
    private String email;
}
