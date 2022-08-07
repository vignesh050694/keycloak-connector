package com.keycloak.connector.service;

import com.keycloak.connector.config.IAMPropertyReader;
import com.keycloak.connector.constants.KeycloakConstants;
import com.keycloak.connector.exception.KeycloakException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService implements IAuthenticationService{
    @Autowired
    private IAMPropertyReader propertyReader;

    public static enum Scope{
        offline_access,
        none;

        private Scope(){

        }
    }

    public static enum GrantType {
        password,
        refresh_token,
        logout;

        private GrantType() {
        }
    }

    @Override
    public String getUserAuthentication(UserCredentials credentials, Scope scope, GrantType grantType) throws KeycloakException {
        String response = "";
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("grant_type", grantType.toString()));
        nameValuePairs.add(new BasicNameValuePair("client_id", this.propertyReader.getProperty(KeycloakConstants.RESOURCE)));
        nameValuePairs.add(new BasicNameValuePair("client_secret", this.propertyReader.getProperty(KeycloakConstants.CLIENT_SECRET)));
        nameValuePairs.add(new BasicNameValuePair("username", credentials.getUserName()));
        nameValuePairs.add(new BasicNameValuePair("password", credentials.getPassword()));
        if(!scope.toString().equalsIgnoreCase(Scope.none.toString())){
            nameValuePairs.add(new BasicNameValuePair("scope", scope.toString()));
        }
        try {
            response = this.sendPost(nameValuePairs, "", grantType);
        } catch (Exception e) {
            throw new KeycloakException(e.getMessage());
        }

        return response;
    }

    @Override
    public String getUserRefreshToken(String _refreshToken, GrantType grandType) throws KeycloakException {
        String responseToken = null;

        try {
            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("grant_type", grandType.toString()));
            urlParameters.add(new BasicNameValuePair("refresh_token", _refreshToken));
            urlParameters.add(new BasicNameValuePair("client_id", this.propertyReader.getProperty("keycloak.resource")));
            urlParameters.add(new BasicNameValuePair("client_secret", this.propertyReader.getProperty("keycloak.credentials.secret")));
            responseToken = this.sendPost(urlParameters, "", grandType);
            return responseToken;
        } catch (Exception var6) {
            throw new KeycloakException(var6.getMessage());
        }
    }

    private String sendPost(List<NameValuePair> urlParameters, String _clientApp, GrantType grandType) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = this.getHttpPost(_clientApp, grandType);
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(httpPost);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";

        while((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    private HttpPost getHttpPost(String _clientApp, GrantType grandType) {
        String AUTHURL = this.propertyReader.getProperty(_clientApp + "keycloak.auth-server-url");
        String REALM = this.propertyReader.getProperty(_clientApp + "keycloak.realm");
        HttpPost httpPost = null;
        if (grandType.equals(GrantType.password) || grandType.equals(GrantType.refresh_token)) {
            httpPost = new HttpPost(AUTHURL + "/realms/" + REALM + "/protocol/openid-connect/token");
        }

        return httpPost;
    }
}
