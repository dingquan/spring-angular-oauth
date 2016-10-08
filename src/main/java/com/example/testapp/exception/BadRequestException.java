package com.example.testapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST) 
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 8206672399871273989L;
	
	public BadRequestException(String msg) {
		super(msg);
	}
	
	public BadRequestException(String msg, Throwable e) {
		super(msg, e);
	}
}
