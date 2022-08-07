package com.keycloak.connector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keycloak.connector.config.IAMPropertyReader;
import com.keycloak.connector.constants.KeycloakConstants;
import com.keycloak.connector.dto.KeycloakProvider;
import com.keycloak.connector.dto.KeycloakUser;
import com.keycloak.connector.exception.KeycloakException;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.keycloak.connector.constants.KeycloakConstants.REALM;

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



    @Override
    public Integer createUser(KeycloakUser user) throws JsonProcessingException, KeycloakException {
        List<String> roles = new ArrayList<>();
        UsersResource usersResource = kcProvider.getInstance().realm(iamPropertyReader.getProperty(REALM)).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());
        UserRepresentation kcUser = new UserRepresentation();

        kcUser.setUsername(user.getUserName());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getName());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        kcUser.setRealmRoles(roles);
        kcUser.setLastName(user.getMobileNumber());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", customUserProvider.accessToken());

        HttpEntity<String> postEntity = new HttpEntity<>(new ObjectMapper().writeValueAsString(kcUser), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntityStr = restTemplate.exchange(iamPropertyReader.getProperty(KeycloakConstants.AUTH_SERVER_URL), HttpMethod.POST, postEntity, String.class);

        if (responseEntityStr.getStatusCode().value() != 201) {
            throw  new KeycloakException(responseEntityStr.getBody());
        }

/*        javax.ws.rs.core.Response iamResponse = usersResource.create(kcUser);

        String userId = usersResource.search(user.getMobileNumber())
                .get(0)
                .getId();

        List<RoleRepresentation> roleToAdd = new LinkedList<>();

        roleToAdd.add(
                kcProvider.getInstance()
                        .realm(iamPropertyReader.getProperty(REALM)).roles().get(roles.get(0)).toRepresentation()
        );

        usersResource.get(userId).roles().realmLevel().add(roleToAdd);*/

        return responseEntityStr.getStatusCode().value();
    }


    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
