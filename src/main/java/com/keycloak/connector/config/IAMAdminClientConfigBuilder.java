package com.keycloak.connector.config;

import lombok.Builder;

@Builder
public class IAMAdminClientConfigBuilder {
    private String serverUrl;
    private String realm;
    private String clientId;
    private String clientSecret;

}
