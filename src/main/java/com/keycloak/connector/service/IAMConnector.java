package com.keycloak.connector.service;

import com.keycloak.connector.config.IAMPropertyReader;
import com.keycloak.connector.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IAMConnector implements IIAMConnector{
    @Autowired
    private IAMPropertyReader propertyReader;

    @Autowired
    private CustomUserProvider customUserProvider;

    @Override
    public CurrentUser getCurrentUser() {
        return customUserProvider.getCurrentUser();
    }

}
