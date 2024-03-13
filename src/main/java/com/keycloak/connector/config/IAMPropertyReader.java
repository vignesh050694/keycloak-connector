package com.keycloak.connector.config;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource({"classpath:application.properties"})
@NoArgsConstructor
public class IAMPropertyReader {
    @Autowired
    private Environment environment;

    public String getProperty(String key){
        Map map = new HashMap();
        return this.environment.getProperty(key);
    }

}
