package com.keycloak.connector.service;

import com.keycloak.connector.security.CurrentUser;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class CustomUserProvider {

    @Bean
    @Scope("request")
    public CurrentUser getCurrentUser() {
        CurrentUser currentUser = new CurrentUser();
        KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal = this.getCurrentUserPrincipal();
        if(principal!= null) {
            String userId = principal.getKeycloakSecurityContext().getToken().getSubject();
            String userName = principal.toString();
            String email = principal.getKeycloakSecurityContext().getToken().getEmail();

            currentUser.setUserId(userId);
            currentUser.setUserName(userName);
            currentUser.setEmail(email);
        }
        return currentUser;
    }

    public KeycloakPrincipal<RefreshableKeycloakSecurityContext> getCurrentUserPrincipal() {
        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            return (KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    public String accessToken() {
        return "Bearer " + this.getCurrentUserPrincipal().getKeycloakSecurityContext().getTokenString();
    }
}
