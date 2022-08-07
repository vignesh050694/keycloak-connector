package com.keycloak.connector.config;

import org.springframework.context.annotation.Import;

import javax.xml.bind.Element;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CentralizedConfig.class})
public @interface EnableKeycloak {
}
