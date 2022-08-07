package com.keycloak.connector.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler {

	@ExceptionHandler(KeycloakException.class)
	public ResponseEntity<Object> handleExceptions(KeycloakException exception) {
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
	}

}
