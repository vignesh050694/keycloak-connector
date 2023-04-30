package com.keycloak.connector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.keycloak.connector.config.IAMPropertyReader;
import com.keycloak.connector.constants.KeycloakConstants;
import com.keycloak.connector.dto.KeycloakProvider;
import com.keycloak.connector.dto.KeycloakUser;
import com.keycloak.connector.dto.Roles;
import com.keycloak.connector.dto.User;
import com.keycloak.connector.exception.KeycloakException;
import com.keycloak.connector.security.CurrentUser;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.keycloak.connector.constants.KeycloakConstants.AUTH_PASSWORD;
import static com.keycloak.connector.constants.KeycloakConstants.AUTH_USER;
import static com.keycloak.connector.constants.KeycloakConstants.REALM;
import static com.keycloak.connector.constants.KeycloakConstants.RESOURCE;

@Service
public class KeycloakUserServiceImpl implements KeycloakUserService {

    @Autowired
    private KeycloakProvider kcProvider;

    @Autowired
    private CustomUserProvider customUserProvider;

    @Autowired
    private IAMPropertyReader iamPropertyReader;

    @Autowired
    private IAMConnector iamConnector;

    @Autowired
    private AuthenticationService authenticationService;

    public static final String ACCESS_TOKEN = "access_token";



    @Override
    public String createUser(KeycloakUser user) throws JsonProcessingException, KeycloakException {
        UserRepresentation kcUser = new UserRepresentation();

        List<String> roles = new ArrayList<>();
        if(!CollectionUtils.isEmpty(user.getRoles())) {
            roles.addAll(user.getRoles());
        }

        //UsersResource usersResource = kcProvider.getInstance().realm(iamPropertyReader.getProperty(REALM)).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        String userName = iamPropertyReader.getProperty(AUTH_USER);
        String password = iamPropertyReader.getProperty(AUTH_PASSWORD);
        String client = iamPropertyReader.getProperty(RESOURCE);

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUserName(userName);
        userCredentials.setPassword(password);

        String token = authenticationService.getUserAuthentication(userCredentials, AuthenticationService.Scope.none, AuthenticationService.GrantType.password);
        JsonObject jsonObject = JsonParser.parseString(token).getAsJsonObject();
        String bearer = jsonObject.get(ACCESS_TOKEN).getAsString();

        Map<String, List<String>> roleMap = new HashMap<>();
        roleMap.put(client, roles);

        kcUser.setUsername(user.getUserName());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getName());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        kcUser.setRealmRoles(roles);
        kcUser.setLastName(user.getMobileNumber());
        kcUser.setRealmRoles(roles);
        kcUser.setClientRoles(roleMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + bearer);

        HttpEntity<String> postEntity = new HttpEntity<>(new ObjectMapper().writeValueAsString(kcUser), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntityStr = restTemplate.exchange(iamPropertyReader.getProperty(KeycloakConstants.AUTH_SERVER_URL), HttpMethod.POST, postEntity, String.class);

        if (responseEntityStr.getStatusCode().value() != 201) {
            throw  new KeycloakException(responseEntityStr.getBody());
        }

        String userId = setRole(user, roles, client, headers, postEntity, restTemplate);

        return userId;
    }

    private String setRole(KeycloakUser user, List<String> roles, String client, HttpHeaders headers, HttpEntity<String> postEntity, RestTemplate restTemplate) throws JsonProcessingException {
        ResponseEntity<List> userResponseEntity = restTemplate.exchange(iamPropertyReader.getProperty(KeycloakConstants.AUTH_SERVER_URL) + "?username=" + user.getUserName(), HttpMethod.GET, postEntity, List.class);
        Map currentUserMap = (Map) userResponseEntity.getBody().get(0);
        String userId = (String) currentUserMap.get("id");

        ResponseEntity<List> clientResponseEntity = restTemplate.exchange(iamPropertyReader.getProperty(KeycloakConstants.AUTH_SERVER_BASE_URL) + "/clients?clientId=" + client, HttpMethod.GET, postEntity, List.class);
        Map<String, Object> clientMap = (Map) clientResponseEntity.getBody().get(0);
        String clientId = (String) clientMap.get("id");

        ResponseEntity<Map> roleResponseEntity = restTemplate.exchange(iamPropertyReader.getProperty(KeycloakConstants.AUTH_SERVER_BASE_URL) + "/clients/" + clientId + "/roles/" + roles.get(0), HttpMethod.GET, postEntity, Map.class);

        List<Map> keycloakRoleMap = new ArrayList<>();
        keycloakRoleMap.add(roleResponseEntity.getBody());

        HttpEntity<String> postRoleEntity = new HttpEntity<>(new ObjectMapper().writeValueAsString(keycloakRoleMap), headers);
        restTemplate.exchange(iamPropertyReader.getProperty(KeycloakConstants.AUTH_SERVER_BASE_URL) + "/users/" + userId + "/role-mappings/clients/" + clientId, HttpMethod.POST, postRoleEntity, String.class);
        return userId;
    }


    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
