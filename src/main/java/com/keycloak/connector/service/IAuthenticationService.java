package com.keycloak.connector.service;

import com.keycloak.connector.exception.KeycloakException;

public interface IAuthenticationService {
    String getUserAuthentication(UserCredentials credentials, AuthenticationService.Scope scope, AuthenticationService.GrantType grantType) throws KeycloakException;
    String getUserRefreshToken(String _refreshToken, AuthenticationService.GrantType grandType) throws KeycloakException;
}
