package com.keycloak.connector.exception;

public class KeycloakException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 2479857663048967463L;

    public KeycloakException(String message) {
        super(message);
    }

    public KeycloakException(String e, Throwable cause) {
        super(e);
    }
}

