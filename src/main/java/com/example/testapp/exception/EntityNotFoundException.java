package com.example.testapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND) 
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5195266003490483401L;

	public EntityNotFoundException(String msg) {
		super(msg);
	}
	
	public EntityNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}
}
