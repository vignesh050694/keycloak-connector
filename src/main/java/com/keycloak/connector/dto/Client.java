package com.keycloak.connector.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Client {

    private String id;
    private String clientId;
    private boolean surrogateAuthRequired;
    private boolean enabled;
    private boolean alwaysDisplayInConsole;
    private String clientAuthenticatorType;
    private List<Object> redirectUris = new ArrayList < Object > ();
    private  List<Object> webOrigins = new ArrayList < Object > ();
    private float notBefore;
    private boolean bearerOnly;
    private boolean consentRequired;
    private boolean standardFlowEnabled;
    private boolean implicitFlowEnabled;
    private boolean directAccessGrantsEnabled;
    private boolean serviceAccountsEnabled;
    private boolean publicClient;
    private boolean frontchannelLogout;
    private String protocol;
}
