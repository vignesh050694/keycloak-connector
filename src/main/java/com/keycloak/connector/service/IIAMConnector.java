package com.keycloak.connector.service;

import com.keycloak.connector.security.CurrentUser;

public interface IIAMConnector {
    CurrentUser getCurrentUser();
}
