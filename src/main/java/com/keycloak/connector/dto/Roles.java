package com.keycloak.connector.dto;

import lombok.Data;

@Data
public class Roles {
    private String id;
    private String name;
    private boolean composite;
    private boolean clientRole;
    private String containerId;
}
