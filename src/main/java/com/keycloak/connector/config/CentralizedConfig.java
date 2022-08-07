package com.keycloak.connector.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.keycloak.connector"})
public class CentralizedConfig {
    public CentralizedConfig(){

    }
}
