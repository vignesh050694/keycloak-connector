package com.keycloak.connector.config;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource({"classpath:application.properties"})
@NoArgsConstructor
public class IAMPropertyReader {
    @Autowired
    private Environment environment;

    public String getProperty(String key){
        return this.environment.getProperty(key);
    }

}
