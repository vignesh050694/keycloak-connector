package com.keycloak.connector.dto;

import com.keycloak.connector.config.IAMPropertyReader;
import com.keycloak.connector.constants.KeycloakConstants;
import lombok.Getter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import static com.keycloak.connector.constants.KeycloakConstants.AUTH_PASSWORD;
import static com.keycloak.connector.constants.KeycloakConstants.AUTH_SERVER_URL;
import static com.keycloak.connector.constants.KeycloakConstants.AUTH_USER;
import static com.keycloak.connector.constants.KeycloakConstants.CLIENT_SECRET;
import static com.keycloak.connector.constants.KeycloakConstants.REALM;
import static com.keycloak.connector.constants.KeycloakConstants.RESOURCE;

@Configuration
@Getter
public class KeycloakProvider {
    @Autowired
    private IAMPropertyReader iamPropertyReader;

    private static Keycloak keycloak = null;

    public KeycloakProvider() {
    }

    public Keycloak getInstance() {
        if (keycloak == null) {

            keycloak = KeycloakBuilder.builder()
                    .realm(iamPropertyReader.getProperty(REALM))
                    .serverUrl(iamPropertyReader.getProperty(AUTH_SERVER_URL))
                    .clientId(iamPropertyReader.getProperty(RESOURCE))
                    .clientSecret(iamPropertyReader.getProperty(CLIENT_SECRET))
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(iamPropertyReader.getProperty(AUTH_USER))
                    .password(iamPropertyReader.getProperty(AUTH_PASSWORD))
                    .build();
        }
        return keycloak;
    }


}
