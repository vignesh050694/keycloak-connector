package com.keycloak.connector.dto;

import lombok.Data;

@Data
public class User {
    private String id;
    private float createdTimestamp;
    private String username;
    private boolean enabled;
    private boolean totp;
    private boolean emailVerified;
    private String firstName;
    private String lastName;
    private String email;
}
